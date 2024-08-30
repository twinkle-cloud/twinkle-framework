package com.twinkle.framework.api.component;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.asm.Bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-17 16:12<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface IComponentFactory {
    String KEY_COMPONENT_NAME = "Name";
    String KEY_CLASS_NAME = "ClassName";

    /**
     * Load the configurable component.
     *
     * @param _obj
     * @param <T>
     * @return
     */
    <T extends Configurable> T loadGeneralComponent(JSONObject _obj);

    /**
     * Load the configurable component.
     *
     * @param _obj
     * @return
     */
    <T extends Configurable> T loadComponent(JSONObject _obj);

    /**
     * Load the configurable component and register into Spring context.
     *
     * @param _componentName
     * @param _obj
     * @param <T>
     * @return
     */
    <T extends Configurable> T loadComponent(String _componentName, JSONObject _obj);

    void registerCustomizeBean(Bean _bean);

    /**
     * register the bean, refer Spring GenericWebApplicationContext
     *  .registerBeanDefinition(String _beanName, BeanDefinition _definition)
     *
     * @param _beanName
     * @param _definition
     */
    void registerBeanDefinition(String _beanName, BeanDefinition _definition);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentNamePair {
        /**
         * Component name.
         */
        private String name;
        /**
         * Component full path name.
         */
        private String fullPathName;
    }
}
