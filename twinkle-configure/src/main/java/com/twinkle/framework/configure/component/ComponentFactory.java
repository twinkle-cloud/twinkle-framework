package com.twinkle.framework.configure.component;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.component.IComponent;
import com.twinkle.framework.api.component.IComponentFactory;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.asm.Bean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.util.DigestUtils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-17 16:15<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class ComponentFactory implements IComponentFactory {
    private BeanDefinitionRegistry registry;

    private static ComponentFactory componentFactory;

    public ComponentFactory(BeanDefinitionRegistry _registry) {
        this.registry = _registry;
        componentFactory = this;
    }

    public static ComponentFactory getInstance() {
        if (componentFactory == null) {
            throw new ConfigurationException(ExceptionCode.COMPONENT_FACTORY_NOT_INITIALIZED, "Component Factory has not been initialized.");
        }
        return componentFactory;
    }

    @Override
    public <T extends Configurable> T loadComponent(JSONObject _obj) {
        return this.loadComponent(null, _obj);
    }

    @Override
    public <T extends Configurable> T loadGeneralComponent(JSONObject _obj) {
        if (_obj.isEmpty()) {
            return null;
        }
        String tempClassName = _obj.getString(KEY_CLASS_NAME);
        if (StringUtils.isBlank(tempClassName)) {
            throw new ConfigurationException(ExceptionCode.COMPONENT_CLASS_MISSED, "The [" + _obj + "]component class is empty");
        }
        try {
            ClassLoader tempClassLoader = this.getClass().getClassLoader();
            Class tempClass = tempClassLoader.loadClass(tempClassName);
            if (null != tempClass) {
                T tempComponent = (T) tempClass.newInstance();
                tempComponent.configure(_obj);
                return tempComponent;
            }
        } catch (NoClassDefFoundError | ClassNotFoundException ex) {
            throw new IllegalArgumentException("Cannot resolve dependencies for class: " + tempClassName, ex);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot resolve dependencies for class: " + tempClassName, e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Cannot resolve dependencies for class: " + tempClassName, e);
        }
        return null;
    }

    @Override
    public <T extends Configurable> T loadComponent(String _componentName, JSONObject _obj) {
        if (_obj.isEmpty()) {
            return null;
        }
        String tempClassName = _obj.getString(KEY_CLASS_NAME);
        if (StringUtils.isBlank(tempClassName)) {
            throw new ConfigurationException(ExceptionCode.COMPONENT_CLASS_MISSED, "The [" + _obj + "]component class is empty");
        }
        String tempComponentName = _componentName;
        if (StringUtils.isBlank(tempComponentName)) {
            tempComponentName = _obj.getString(KEY_COMPONENT_NAME);
            if (StringUtils.isBlank(tempComponentName)) {
                int tempIndex = tempClassName.lastIndexOf(".");
                if (tempIndex > 0) {
                    tempComponentName = tempClassName.substring(tempIndex + 1);
                } else {
                    tempComponentName = tempClassName;
                }
            }
        }
        try {
            ClassLoader tempClassLoader = this.getClass().getClassLoader();
            Class tempClass = tempClassLoader.loadClass(tempClassName);
            if (null != tempClass) {
                String tempBeanName = DigestUtils.md5DigestAsHex(tempComponentName.getBytes());
                try {
                    this.registry.getBeanDefinition(tempBeanName);
                    log.debug("Found the duplicate bean [{}] -> [{}] had been registered already.",
                            tempComponentName, tempClassName);
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_DUPLICATE_COMPONENT_FOUND, "Found the duplicate bean [" + tempComponentName + "] had been registered already.");
                } catch (NoSuchBeanDefinitionException ex) {
                    log.debug("Did not find the bean with name [{}].", tempComponentName);
                }
                T tempComponent = (T) tempClass.newInstance();
                if (tempComponent instanceof IComponent) {
                    ((IComponent) tempComponent).setFullPathName(tempComponentName);
                }
                tempComponent.configure(_obj);

                BeanDefinitionBuilder tempBeanDefinitionBuilder = BeanDefinitionBuilder
                        .genericBeanDefinition(tempClass, () -> tempComponent);
                BeanDefinition tempComponentBeanDefinition = tempBeanDefinitionBuilder
                        .getRawBeanDefinition();
                this.registry.registerBeanDefinition(tempBeanName, tempComponentBeanDefinition);

                log.debug("Load the bean [{}] -> [{}] had been successfully.",
                        tempComponentName, tempClassName);
                return tempComponent;
            }
        } catch (NoClassDefFoundError ex) {
            throw new IllegalArgumentException("Cannot resolve dependencies for class: " + tempClassName, ex);
        } catch (Throwable te) {
            throw new IllegalArgumentException("Problem loading class:" + tempClassName, te);
        }
        return null;
    }

    @Override
    public void registerCustomizeBean(Bean _bean) {
//        try {
//            if (null != _bean) {
//                String tempName = _bean.getClass().getName();
//                int tempIndex = tempName.lastIndexOf(".");
//                if(tempIndex > 0) {
//                    tempName = tempName.substring(tempIndex + 1);
//                }
//                context.registerBean(StringUtils.uncapitalize(tempName),
//                        _bean.getClass(), () -> _bean);
//
//                log.debug("Load the bean [{}] had been successfully.",
//                        _bean);
//            }
//        } catch (NoClassDefFoundError ex) {
//            throw new IllegalArgumentException("Cannot resolve dependencies for class: " + tempClassName);
//        } catch (Throwable te) {
//            throw new IllegalArgumentException("Problem loading class:" + tempClassName);
//        }
    }

    @Override
    public void registerBeanDefinition(String _beanName, BeanDefinition _definition) {
        this.registry.registerBeanDefinition(_beanName, _definition);
    }
}
