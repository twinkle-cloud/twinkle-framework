package com.twinkle.framework.bootstarter.config;

import com.twinkle.framework.core.asm.classloader.BeanClassLoader;
import com.twinkle.framework.core.asm.factory.BeanFactoryImpl;
import com.twinkle.framework.core.datastruct.BeanFactory;
import com.twinkle.framework.core.datastruct.descriptor.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 11:07<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
//@Configuration
public class AsmBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
//        ClassLoader currentLoader = ClassUtils.getDefaultClassLoader();
//
//        BeanClassLoader tempLoader = new BeanClassLoader(currentLoader, this.packDescriptors());
//
//        BeanFactory tempBeanFactory = new BeanFactoryImpl(tempLoader);
//        Object tempObj = tempBeanFactory.newInstance("HelloWorld");
//        log.debug("The new obj is:{}", tempObj);
//        Class<?> tempClass = tempObj.getClass();
//        log.debug("The new obj Class is:{}", tempClass);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


}
