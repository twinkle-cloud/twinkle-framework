package com.twinkle.framework.bootstarter;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.rule.IRuleManager;
import com.twinkle.framework.configure.component.IComponentFactory;
import com.twinkle.framework.connector.ConnectorManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.context.support.GenericWebApplicationContext;

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
public class TwinkleInitializer implements BeanDefinitionRegistryPostProcessor{
    private final static String KEY_CONNECTOR_MANAGER = "Connectors";
    private final static String KEY_RULECHAIN_MANAGER = "RuleChains";

    /**
     * Used to get the logic configuration content from ENV.
     */
    @Autowired
    private Environment env;

    @Autowired
    private IComponentFactory componentFactory;

    @Autowired
    private GenericWebApplicationContext genericWebApplicationContext;
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        // TODO Auto-generated method stub

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // TODO Auto-generated method stub
        String tempConfiguration = this.env.getProperty("");
        JSONObject tempObj = JSONObject.parseObject(tempConfiguration);
        JSONObject tempConnectorObj = tempObj.getJSONObject(KEY_CONNECTOR_MANAGER);

        ConnectorManager tempConnectorManager = componentFactory.loadComponent(tempConnectorObj);

        if(tempConnectorManager == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_CONNECTOR ,"Did not find valid connector obj in the logic configuration.");
        }

        IRuleManager tempRuleManager = componentFactory.loadComponent(tempObj.getJSONObject(KEY_RULECHAIN_MANAGER));
        if(tempRuleManager == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_RULECHAIN ,"Did not find valid IRuleChain obj in the logic configuration.");
        }
    }
}
