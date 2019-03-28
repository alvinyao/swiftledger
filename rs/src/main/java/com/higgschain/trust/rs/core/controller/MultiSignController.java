package com.higgschain.trust.rs.core.controller;

import com.higgschain.trust.rs.common.enums.RsCoreErrorEnum;
import com.higgschain.trust.rs.common.exception.RsCoreException;
import com.higgschain.trust.rs.core.api.MultiSignService;
import com.higgschain.trust.slave.api.vo.RespData;
import com.higgschain.trust.rs.core.vo.CreateCurrencyVO;
import com.higgschain.trust.rs.core.vo.MultiSignHashVO;
import com.higgschain.trust.rs.core.vo.MultiSignRuleVO;
import com.higgschain.trust.rs.core.vo.MultiSignTxVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuyu
 * @date 2019/3/21
 */

@RequestMapping(path = "/multiSign") @RestController @Slf4j public class MultiSignController {
    @Autowired private MultiSignService multiSignService;

    /**
     * create address
     *
     * @param rule
     * @return
     */
    @RequestMapping(value = "/create") RespData<String> create(@RequestBody MultiSignRuleVO rule) {
        try {
            return multiSignService.createAddress(rule);
        } catch (RsCoreException e) {
            log.error("create has error", e);
            return RespData.error(e.getCode().getCode(), e.getCode().getDescription(), null);
        } catch (Throwable t) {
            log.error("create has error", t);
        }
        return RespData.error(RsCoreErrorEnum.RS_CORE_UNKNOWN_EXCEPTION.getCode(),
            RsCoreErrorEnum.RS_CORE_UNKNOWN_EXCEPTION.getDescription(), null);
    }

    /**
     * create currency
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/createCurrency") RespData<Boolean> createCurrency(@RequestBody CreateCurrencyVO vo) {
        try {
            return multiSignService.createCurrencyContract(vo);
        } catch (RsCoreException e) {
            log.error("createCurrency has error", e);
            return RespData.error(e.getCode().getCode(), e.getCode().getDescription(), null);
        } catch (Throwable t) {
            log.error("createCurrency has error", t);
        }
        return RespData.error(RsCoreErrorEnum.RS_CORE_UNKNOWN_EXCEPTION.getCode(),
            RsCoreErrorEnum.RS_CORE_UNKNOWN_EXCEPTION.getDescription(), null);
    }

    /**
     * get sign hash
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/getSignHash") RespData<String> getSignHashValue(@RequestBody MultiSignHashVO vo) {
        try {
            return multiSignService.getSignHashValue(vo);
        } catch (RsCoreException e) {
            log.error("getSignHashValue has error", e);
            return RespData.error(e.getCode().getCode(), e.getCode().getDescription(), null);
        } catch (Throwable t) {
            log.error("getSignHashValue has error", t);
        }
        return RespData.error(RsCoreErrorEnum.RS_CORE_UNKNOWN_EXCEPTION.getCode(),
            RsCoreErrorEnum.RS_CORE_UNKNOWN_EXCEPTION.getDescription(), null);
    }

    /**
     * transfer
     *
     * @param vo
     * @return
     */
    @RequestMapping(value = "/transfer") RespData<Boolean> transfer(@RequestBody MultiSignTxVO vo) {
        try {
            return multiSignService.transfer(vo);
        } catch (RsCoreException e) {
            log.error("transfer has error", e);
            return RespData.error(e.getCode().getCode(), e.getCode().getDescription(), null);
        } catch (Throwable t) {
            log.error("transfer has error", t);
        }
        return RespData.error(RsCoreErrorEnum.RS_CORE_UNKNOWN_EXCEPTION.getCode(),
            RsCoreErrorEnum.RS_CORE_UNKNOWN_EXCEPTION.getDescription(), null);
    }
}
