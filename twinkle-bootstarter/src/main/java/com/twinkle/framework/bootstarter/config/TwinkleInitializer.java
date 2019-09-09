package com.twinkle.framework.bootstarter.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.struct.manager.StructAttributeManager;
import com.twinkle.framework.configure.component.ComponentFactory;
import com.twinkle.framework.configure.component.IComponentFactory;
import com.twinkle.framework.connector.ConnectorManager;
import com.twinkle.framework.core.context.ContextSchema;
import com.twinkle.framework.ruleengine.RuleChainManager;
import com.twinkle.framework.struct.context.StructAttributeSchema;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.factory.StructAttributeFactoryCenter;
import com.twinkle.framework.struct.factory.StructAttributeFactoryCenterImpl;
import com.twinkle.framework.struct.type.StructAttributeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Twinkle 初始化器
 * Date:    2019年7月14日 下午7:55:11 <br/>
 *
 * @author yukang
 * @see
 * @since JDK 1.8
 */
@Slf4j
@Configuration
public class TwinkleInitializer implements BeanDefinitionRegistryPostProcessor {
    private final static String KEY_CONNECTOR_MANAGER = "ConnectorManager";
    private final static String KEY_RULECHAIN_MANAGER = "RuleChainManager";
    private final static String KEY_STRUCT_ATTRIBUTE_MANAGER = "StructAttributeManager";
    private final static String KEY_ATTRIBUTE_SET = "AttributeSet";

    /**
     * Used to get the logic configuration content from ENV.
     */
    @Autowired
    private Environment env;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        // TODO Auto-generated method stub

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathResource classPathResource = new ClassPathResource("Test.json");
        //String tempConfiguration = this.env.getProperty("");

        String tempConfiguration = null;
        try {
            tempConfiguration = IOUtils.toString(classPathResource.getInputStream(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject tempObj = JSONObject.parseObject(tempConfiguration);

        //Initialize the attributes in ContextSchema.
        JSONArray tempAttrArray = tempObj.getJSONArray(KEY_ATTRIBUTE_SET);
        ContextSchema tempSchema = ContextSchema.getInstance();
        tempSchema.configure(tempAttrArray);

        IComponentFactory componentFactory = new ComponentFactory(registry);

        //Initialize the Struct Attribute' Manager.
        StructAttributeManager tempStructAttributeManager = componentFactory.loadComponent(tempObj.getJSONObject(KEY_STRUCT_ATTRIBUTE_MANAGER));
        if (tempStructAttributeManager == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_CONNECTOR, "Did not find valid connector obj in the logic configuration.");
        }
        StructAttributeSchema tempStructSchema = StructAttributeSchemaManager.getStructAttributeSchema();
        StructAttributeFactoryCenter tempCenter = new StructAttributeFactoryCenterImpl(
                tempStructSchema, this.getClass().getClassLoader()
        );
        StructAttributeSchemaManager.registerStructAttributeImpl(tempCenter);

        StructAttributeType tempType = tempStructSchema.getStructAttributeType("TestDemo", "TestRequest");
//        try {
//            StructAttributeBeanTypeDef tempTypeDef = new StructAttributeBeanTypeDefImpl(tempType, this.getClass().getClassLoader());
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        tempCenter.getStructAttributeFactory().newStructAttribute(tempType);
        tempCenter.getStructAttributeFactory().loadGeneralBeanClass(tempType);


        //Initialize the connectors' Manager.
        ConnectorManager tempConnectorManager = componentFactory.loadComponent(tempObj.getJSONObject(KEY_CONNECTOR_MANAGER));
        if (tempConnectorManager == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_CONNECTOR, "Did not find valid connector obj in the logic configuration.");
        }
        //Initialize the rule Manager.
        RuleChainManager tempRuleManager = componentFactory.loadComponent(tempObj.getJSONObject(KEY_RULECHAIN_MANAGER));
        if (tempRuleManager == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_RULECHAIN, "Did not find valid IRuleChain obj in the logic configuration.");
        }
    }
}
