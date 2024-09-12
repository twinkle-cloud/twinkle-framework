package com.twinkle.framework.bootstarter.config;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.IComponentFactory;
import com.twinkle.framework.api.component.datacenter.IDataCenterManager;
import com.twinkle.framework.api.component.rule.IRuleChainManager;
import com.twinkle.framework.api.constant.CommonConstant;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.configure.component.ComponentFactory;
import com.twinkle.framework.connector.ConnectorManager;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.context.manager.StructAttributeManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
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
public class TwinkleInitializer implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private final static String KEY_CONNECTOR_MANAGER = "ConnectorManager";
    private final static String KEY_RULECHAIN_MANAGER = "RuleChainManager";
    private final static String KEY_DATACENTER_MANAGER = "DataCenterManager";
    private final static String KEY_STRUCT_ATTRIBUTE_MANAGER = "StructAttributeManager";
    private final static String KEY_ATTRIBUTE_SET = "AttributeSet";

    /**
     * Used to get the logic configuration content from ENV.
     */
    private Environment env;
    private JSONObject fileJSONObj;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        // TODO Auto-generated method stub
//        IComponentFactory componentFactory = new ComponentFactory(factory);
//        IDataCenterManager tempDataCenterManager = ComponentFactory.getInstance().loadComponent(this.fileJSONObj.getJSONObject(KEY_DATACENTER_MANAGER));

        //Initialize the rule Manager.
//        IRuleChainManager tempRuleManager = ComponentFactory.getInstance().loadComponent(this.fileJSONObj.getJSONObject(KEY_RULECHAIN_MANAGER));
//        if (tempRuleManager == null) {
//            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_RULECHAIN, "Did not find valid IRuleChain obj in the logic configuration.");
//        }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        IComponentFactory componentFactory = new ComponentFactory(registry);
        //Initialize the Struct Attribute' Manager.
        StructAttributeManager tempStructAttributeManager = componentFactory.loadComponent(this.fileJSONObj.getJSONObject(KEY_STRUCT_ATTRIBUTE_MANAGER));
        if (tempStructAttributeManager == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_CONNECTOR, "Did not find valid connector obj in the logic configuration.");
        }

        //Initialize the attributes in PrimitiveAttributeSchema.
        JSONArray tempAttrArray = this.fileJSONObj.getJSONArray(KEY_ATTRIBUTE_SET);
        PrimitiveAttributeSchema tempSchema = PrimitiveAttributeSchema.getInstance();
        tempSchema.configure(tempAttrArray);

        //Initialize the connectors' Manager.
        ConnectorManager tempConnectorManager = componentFactory.loadComponent(this.fileJSONObj.getJSONObject(KEY_CONNECTOR_MANAGER));
        if (tempConnectorManager == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_CONNECTOR, "Did not find valid connector obj in the logic configuration.");
        }

        IDataCenterManager tempDataCenterManager = componentFactory.loadComponent(this.fileJSONObj.getJSONObject(KEY_DATACENTER_MANAGER));

        //Initialize the rule Manager.
        IRuleChainManager tempRuleManager = componentFactory.loadComponent(this.fileJSONObj.getJSONObject(KEY_RULECHAIN_MANAGER));
        if (tempRuleManager == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_RULECHAIN, "Did not find valid IRuleChain obj in the logic configuration.");
        }
    }
    @Override
    public void setEnvironment(Environment _env) {
        this.env = _env;
        String tempFilePath = this.env.getProperty(CommonConstant.TWINKLE_PROPERTY_LAUNCH_FILE);
        if(StringUtils.isBlank(tempFilePath)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_FILE_MISSING, "The low code file is empty.");
        }
        ClassPathResource classPathResource = new ClassPathResource(tempFilePath);

        String tempConfiguration = null;
        try {
            tempConfiguration = IOUtils.toString(classPathResource.getInputStream(), CommonConstant.DEFAULT_ENCODING);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_FILE_LOAD_FAILED, "Encountered Exception while loading the low code file.", e);
        }
        this.fileJSONObj = JSONObject.parseObject(tempConfiguration);
    }
}
