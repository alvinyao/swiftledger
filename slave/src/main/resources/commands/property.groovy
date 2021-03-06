package commands

import com.higgschain.trust.slave.core.service.datahandler.manage.SystemPropertyHandler
import lombok.extern.slf4j.Slf4j
import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Required
import org.crsh.cli.Usage
import org.crsh.command.InvocationContext
import org.springframework.beans.factory.BeanFactory

/*
 * lingchao
 */

/**
 *  The type Property.
 */
@Slf4j
@Usage("about system property operation")
class property {

    @Usage('get system property')
    @Command
    def get(InvocationContext context, @Usage("property key")
    @Required @Argument String key) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        def systemPropertyHandler = beans.getBean(SystemPropertyHandler.class)
        def msg = systemPropertyHandler.get(key)
        out.println(msg)
    }

    @Usage('set system property')
    @Command
    def set(InvocationContext context, @Usage("property key")
    @Required @Argument String key,
            @Usage("property value") @Required @Argument String value, @Usage("property desc") @Argument String desc) {
        BeanFactory beans = context.attributes['spring.beanfactory']
        def systemPropertyHandler = beans.getBean(SystemPropertyHandler.class)
        def msg = systemPropertyHandler.set(key, value, desc)
        out.println(msg)
    }
}
