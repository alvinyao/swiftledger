package com.higgschain.trust.consensus.config;

import com.higgschain.trust.consensus.listener.MasterChangeListener;
import com.higgschain.trust.consensus.listener.StateChangeListener;
import com.higgschain.trust.consensus.listener.StateChangeListenerAdaptor;
import com.higgschain.trust.consensus.listener.StateListener;
import com.higgschain.trust.consensus.exception.ConsensusError;
import com.higgschain.trust.consensus.exception.ConsensusException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.higgschain.trust.consensus.config.NodeStateEnum.*;

/**
 * The type Node state.
 */
@Component
@Scope("singleton")
@Slf4j
public class NodeState implements InitializingBean {

    /**
     * The constant MASTER_NA.
     */
    public static final String MASTER_NA = "N/A";

    @Autowired
    private NodeProperties properties;

    @Autowired
    private ApplicationContext applicationContext;

    private Set<MasterChangeListener> masterListeners = new LinkedHashSet<>();

    private Map<NodeStateEnum, LinkedHashSet<StateChangeListenerAdaptor>> stateListeners = new ConcurrentHashMap<>();

    @Getter
    private NodeStateEnum state = Starting;

    /**
     * 当前节点是否为master
     */
    @Getter
    private boolean master;

    /**
     * 节点名
     */
    @Getter
    private String nodeName;

    /**
     * master名
     */
    @Getter
    private String masterName = MASTER_NA;

    /**
     * private key for biz
     */
    @Getter
    @Setter
    private String privateKey;

    /**
     * private key for consensus
     */
    @Getter
    @Setter
    private String consensusPrivateKey;

    /**
     * cluster name, as the prefix of cluster nodes
     */
    @Getter
    private String clusterName;

    @Getter
    @Setter
    private long currentTerm = 0;

    private static final Object masterLock = new Object();

    private static final Object stateLock = new Object();

    @Override
    public void afterPropertiesSet() {
        this.nodeName = properties.getNodeName();
        this.clusterName = properties.getPrefix();

        registerStateListener();

        List<MasterChangeListener> masterChangeListeners = new ArrayList<>();
        masterChangeListeners.addAll(applicationContext.getBeansOfType(MasterChangeListener.class).values());
        AnnotationAwareOrderComparator.sort(masterChangeListeners);
        masterListeners.addAll(masterChangeListeners);
        initStateful();


    }

    /**
     * init stateful
     */
    private void initStateful() {
        for (NodeStatefulService statefulService : applicationContext.getBeansOfType(NodeStatefulService.class).values()) {
            statefulService.init();
        }
    }

    /**
     * register state change listener
     */
    private void registerStateListener() {
        Map<NodeStateEnum, List<StateChangeListenerAdaptor>> stateListeners = new ConcurrentHashMap<>();
        applicationContext.getBeansWithAnnotation(StateListener.class).forEach((beanName, bean) -> {
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
            Map<Method, StateChangeListener> methods = MethodIntrospector.selectMethods(targetClass, (MethodIntrospector.MetadataLookup<StateChangeListener>) method -> AnnotatedElementUtils.findMergedAnnotation(method, StateChangeListener.class));
            if (methods == null || methods.isEmpty()) {
                return;
            }
            methods.forEach((method, value) -> {
                NodeStateEnum[] stateEnums = value.value();
                Arrays.stream(stateEnums).forEach(state -> {
                    List<StateChangeListenerAdaptor> stateChangeListenerAdaptors = stateListeners.computeIfAbsent(state, e -> new ArrayList<>());
                    stateChangeListenerAdaptors.add(new StateChangeListenerAdaptor(bean, method));
                });
            });
        });
        stateListeners.forEach((state, listeners) -> {
            AnnotationAwareOrderComparator.sort(listeners);
            this.stateListeners.put(state, new LinkedHashSet<>(listeners));
        });
    }

    /**
     * register master listener
     *
     * @param listener the listener
     */
    public void registerMasterListener(MasterChangeListener listener) {
        masterListeners.add(listener);
    }

    /**
     * node state change from -> to
     *
     * @param from the from
     * @param to   the to
     */
    public void changeState(NodeStateEnum from, NodeStateEnum to) {
        Assert.notNull(from, "from state can't be null");
        Assert.notNull(to, "to state can't be null");
        try {
            synchronized (stateLock) {
                if (from != state || !checkState(from, to)) {
                    throw new ConsensusException(ConsensusError.CONFIG_NODE_STATE_CHANGE_FAILED);
                }
                LinkedHashSet<StateChangeListenerAdaptor> stateChangeListenerAdaptors = stateListeners.get(to);
                if (stateChangeListenerAdaptors != null) {
                    stateChangeListenerAdaptors.stream().filter(StateChangeListenerAdaptor::isBefore).forEach(StateChangeListenerAdaptor::invoke);
                }
                state = to;
                log.info("Node state changed from:{} to:{}", from, to);
                if (stateChangeListenerAdaptors != null) {
                    stateChangeListenerAdaptors.stream().filter(adaptor -> !adaptor.isBefore()).forEach(StateChangeListenerAdaptor::invoke);
                }
            }
        } catch (Exception e) {
            log.error("change state error", e);
            throw e;
        }
    }

    /**
     * 检查状态迁移是否正确
     *
     * @param from
     * @param to
     * @return
     */
    private boolean checkState(NodeStateEnum from, NodeStateEnum to) {
        boolean result = false;
        switch (from) {
            case Starting:
                result = Initialize == to || Offline == to;
                break;
            case Initialize:
                result = StartingConsensus == to || SelfChecking == to || Offline == to;
                break;
            case StartingConsensus:
                result = SelfChecking == to || Offline == to;
                break;
            case SelfChecking:
                result = AutoSync == to || ArtificialSync == to || Running == to && !properties.isStandby() || Standby == to && properties.isStandby() || Offline == to;
                break;
            case AutoSync:
            case ArtificialSync:
                result = Running == to && !properties.isStandby() || SelfChecking == to || Offline == to || Standby == to && properties.isStandby();
                break;
            case Standby:
                result = AutoSync == to || ArtificialSync == to || Offline == to || SelfChecking == to;
                break;
            case Running:
                result = SelfChecking == to || Offline == to;
                break;
            case Offline:
                result = SelfChecking == to || Initialize == to;
                break;
        }
        return result;
    }

    /**
     * node change the master
     *
     * @param masterName the master name
     */
    public void changeMaster(String masterName) {
        Assert.isTrue(StringUtils.isNotBlank(masterName), "master name can't be null");
        synchronized (masterLock) {
            masterListeners.forEach(listener -> listener.beforeChange(masterName));
            this.masterName = masterName;
            log.info("Node master changed to {}", masterName);
            master = masterName.equalsIgnoreCase(nodeName);
            masterListeners.forEach(listener -> listener.masterChanged(masterName));
        }
    }

    /**
     * Is master boolean.
     *
     * @return the boolean
     */
    public boolean isMaster() {
        return master;
    }

    /**
     * 是否给定的其中状态
     *
     * @param state the state
     * @return boolean
     */
    public boolean isState(NodeStateEnum... state) {
        for (int i = 0; i < state.length; i++) {
            if (this.state == state[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * the regex of node name which exclude me
     *
     * @return string
     */
    public String notMeNodeNameReg() {
        return "(?!" + this.nodeName.toUpperCase(Locale.ROOT) + ")" + this.clusterName.toUpperCase(Locale.ROOT) + "(\\S)*";
    }
}
