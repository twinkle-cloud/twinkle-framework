package com.twinkle.framework.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/19/19 5:11 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
//@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@Slf4j
public class SpringUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    public static void setRootApplicationContext(ApplicationContext applicationContext) {
        log.debug("Setting root application context");
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * Going to get the application context.
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * Get bean from spring context with bean name.
     *
     * @param _name
     * @return
     */
    public static Object getBean(String _name) {
        return getApplicationContext().getBean(_name);
    }

    /**
     * Get bean from spring context with class.
     *
     * @param _clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> _clazz) {
        return getApplicationContext().getBean(_clazz);
    }

    /**
     * Get bean from spring context with name and the bean's class.
     *
     * @param _name
     * @param _clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String _name, Class<T> _clazz) {
        return getApplicationContext().getBean(_name, _clazz);
    }
}
