package com.twinkle.framework.bootstarter.config;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.configure.component.IComponentFactory;
import com.twinkle.framework.connector.ConnectorManager;
import com.twinkle.framework.core.asm.classloader.BeanClassLoader;
import com.twinkle.framework.core.asm.factory.BeanFactoryImpl;
import com.twinkle.framework.core.datastruct.BeanFactory;
import com.twinkle.framework.core.datastruct.descriptor.*;
import com.twinkle.framework.ruleengine.RuleChainManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Twinkle 初始化器
 * Date:    2019年7月14日 下午7:55:11 <br/>
 *
 * @author yukang
 * @see
 * @since JDK 1.8
 */
@Slf4j
//@Configuration
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
//        String tempConfiguration = this.env.getProperty("");
//        JSONObject tempObj = JSONObject.parseObject(tempConfiguration);
//        JSONObject tempConnectorObj = tempObj.getJSONObject(KEY_CONNECTOR_MANAGER);
//
//        ConnectorManager tempConnectorManager = componentFactory.loadComponent(tempConnectorObj);
//
//        if(tempConnectorManager == null) {
//            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_CONNECTOR ,"Did not find valid connector obj in the logic configuration.");
//        }
//
//        RuleChainManager tempRuleManager = componentFactory.loadComponent(tempObj.getJSONObject(KEY_RULECHAIN_MANAGER));
//        if(tempRuleManager == null) {
//            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_RULECHAIN ,"Did not find valid IRuleChain obj in the logic configuration.");
//        }
//        ClassLoader currentLoader = this.getClass().getClassLoader();
//
//        BeanClassLoader tempLoader = new BeanClassLoader(currentLoader, this.packDescriptors());
//
//        BeanFactory tempBeanFactory = new BeanFactoryImpl(tempLoader);
//        Object tempObj = tempBeanFactory.newInstance("HelloWorld");
//        log.debug("The new obj is:{}", tempObj);
//        Class<?> tempClass = tempObj.getClass();
//        log.debug("The new obj Class is:{}", tempClass);

    }
    private TypeDescriptors packDescriptors() {
        List<TypeDescriptor> tempDecriptorList = new ArrayList<>();
        tempDecriptorList.add(this.packDescriptor());
        TypeDescriptors tempDescriptors = new TypeDescriptorsImpl(tempDecriptorList);

        return tempDescriptors;
    }

    private BeanTypeDescriptor packDescriptor(){
        Set<String> tempAnnotationSet = new HashSet();
        tempAnnotationSet.add("@org.springframework.web.bind.annotation.RestController");
        tempAnnotationSet.add("@lombok.extern.slf4j.Slf4j");
        tempAnnotationSet.add("@io.swagger.annotations.Api");
        List<AttributeDescriptor> tempAttrList = new ArrayList<>();
        Set<String> tempAttr1Set = new HashSet();
        tempAttr1Set.add("@org.springframework.beans.factory.annotation.Autowired");
        TypeDescriptor tempAttrType = TypeDescriptorImpl.builder()
                .className("com.twinkle.framework.bootstarter.service.HelloWorldService")
                .name("HelloWorldService")
                .description("").build();
        AttributeDescriptor tempAttr1 = AttributeDescriptorImpl.builder()
                .name("tempResult")
                .annotations(tempAttr1Set)
                .type(tempAttrType).build();
        tempAttrList.add(tempAttr1);

        List<BeanTypeDescriptor> tempInterfaceList = new ArrayList<>();
        BeanTypeDescriptor tempInterfaceType1 = new BeanInterfaceTypeDescriptorImpl("Bean");
        tempInterfaceList.add(tempInterfaceType1);

//        BeanTypeDescriptor tempObjDescriptor = BeanTypeDescriptorImpl.builder().className("java.lang.Object")
//                .name("Object").build();
//        Set<BeanTypeDescriptor> tempParentSet = new HashSet<>();
//        tempParentSet.add(tempObjDescriptor);

        BeanTypeDescriptor tempDescriptor = BeanTypeDescriptorImpl.builder().className(
                "com.twinkle.framework.core.datastruct.beans.HelloWorld"
        ).name("HelloWorld").description("com.twinkle.framework.core.datastruct.beans.HelloWorld")
                .annotations(tempAnnotationSet)
                .attributes(tempAttrList)
//                .parents(tempParentSet)
                .interfaces(tempInterfaceList).build();
        return tempDescriptor;
    }
}
