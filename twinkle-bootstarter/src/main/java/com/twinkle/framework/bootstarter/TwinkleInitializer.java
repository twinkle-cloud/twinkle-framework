package com.twinkle.framework.bootstarter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * Twinkle 初始化器
 * Date:    2019年7月14日 下午7:55:11 <br/>
 *
 * @author yukang
 * @see
 * @since JDK 1.8
 */
public class TwinkleInitializer implements BeanDefinitionRegistryPostProcessor{

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // TODO Auto-generated method stub
        initDataCenter();
        initClientConnector();
        initRule();
        intiRuleChain();
        initServerConnector(registry);
    }

    /**
     * @param registry
     */
    private void initServerConnector(BeanDefinitionRegistry registry) {
        // TODO Auto-generated method stub
    }


    /**
     * 初始化 RuleChain
     */
    private void intiRuleChain() {
        // TODO Auto-generated method stub
        
    }

    /**
     * 初始化 Rule
     */
    private void initRule() {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * 初始化 ClientConnector
     */
    private void initClientConnector() {
        // TODO Auto-generated method stub
        
    }

    /**
     * 初始化 DataCenter
     */
    private void initDataCenter() {
        // TODO Auto-generated method stub
        
    }

}
