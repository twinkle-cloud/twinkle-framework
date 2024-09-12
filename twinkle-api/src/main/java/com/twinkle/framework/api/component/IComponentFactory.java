package com.twinkle.framework.api.component;

import com.alibaba.fastjson2.JSONObject;
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
    String BEAN_SCOPE_TYPE = "IsPrototype";
    String KEY_CONNECTOR_MANAGER = "ConnectorManager";
    String KEY_RULECHAIN_MANAGER = "RuleChainManager";
    String KEY_DATACENTER_MANAGER = "DataCenterManager";
    String KEY_STRUCT_ATTRIBUTE_MANAGER = "StructAttributeManager";
    String KEY_ATTRIBUTE_SET = "AttributeSet";

    /**
     * Load the configurable component.
     *
     * @param _obj
     * @param <T>
     * @return
     */
    <T extends IConfigurableComponent> T loadGeneralComponent(JSONObject _obj);

    /**
     * Load the configurable component.
     *
     * @param _obj
     * @return
     */
    <T extends IConfigurableComponent> T loadComponent(JSONObject _obj);

    /**
     * Load the configurable component and register into Spring context.
     *
     * @param _parentPath
     * @param _obj
     * @param <T>
     * @return
     */
    <T extends IConfigurableComponent> T loadComponent(String _parentPath, JSONObject _obj);

    /**
     * Load the prototype component with the given instance index.
     *
     * @param <T>
     * @param _parentPath
     * @param _obj
     * @param _index
     * @return
     */
    <T extends IConfigurableComponent> T loadPrototypeComponent(String _parentPath, JSONObject _obj, int _index);

    /**
     * Load the prototype component.
     *
     * @param _parentPath
     * @param _obj
     * @return
     * @param <T>
     */
    <T extends IConfigurableComponent> T loadPrototypeComponent(String _parentPath, JSONObject _obj);

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
