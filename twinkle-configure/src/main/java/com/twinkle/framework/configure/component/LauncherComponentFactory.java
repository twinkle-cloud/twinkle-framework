package com.twinkle.framework.configure.component;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.IConfigurableComponent;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.asm.Bean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.DigestUtils;

/**
 * Load twinkle components.
 *
 * @author chenxj
 * @date 2024/09/10
 */
@Slf4j
public class LauncherComponentFactory extends AbstractComponentFactory {
    private ConfigurableListableBeanFactory beanFactory;
    private static LauncherComponentFactory componentFactory;

    public LauncherComponentFactory(ConfigurableListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        componentFactory = this;
    }

    public static LauncherComponentFactory getInstance() {
        if (componentFactory == null) {
            throw new ConfigurationException(ExceptionCode.COMPONENT_FACTORY_NOT_INITIALIZED, "Component Factory has not been initialized.");
        }
        return componentFactory;
    }

    @Override
    public <T extends IConfigurableComponent> T loadComponent(String _parentPath, JSONObject _obj) {
        if (_obj.isEmpty()) {
            return null;
        }
        String tempClassName = _obj.getString(KEY_CLASS_NAME);
        String tempComponentName = this.getComponentName(_parentPath, _obj);
        try {
            ClassLoader tempClassLoader = this.getClass().getClassLoader();
            Class<T> tempClass = (Class<T>) tempClassLoader.loadClass(tempClassName);
            if (null != tempClass) {
                log.debug("The component name is:{}->{}", _parentPath, tempComponentName);
                String tempBeanName = DigestUtils.md5DigestAsHex(tempComponentName.getBytes());
                try {
                    T tempObj = this.beanFactory.getBean(tempBeanName, tempClass);
                    log.info("Found the registered bean [{}]->[{}]", tempBeanName, tempClassName);
                    return tempObj;
                } catch (NoSuchBeanDefinitionException ex) {
                    log.debug("Did not find the bean with name [{}]->[{}].", tempComponentName, tempClassName);
                }
                T tempComponent = tempClass.getDeclaredConstructor().newInstance();
                tempComponent.setParentPath(_parentPath);
                tempComponent.setAliasName(tempComponentName.substring(tempComponentName.lastIndexOf("\\") + 1));

                tempComponent.configure(_obj);

                BeanDefinitionBuilder tempBeanDefinitionBuilder = BeanDefinitionBuilder
                        .genericBeanDefinition(tempClass, () -> {
                            log.debug("Supplier the instance [{}]->[{}].", tempComponentName, tempClassName);
                            return tempComponent;
                        });
                BeanDefinition tempComponentBeanDefinition = tempBeanDefinitionBuilder
                        .getRawBeanDefinition();
                ((DefaultListableBeanFactory) this.beanFactory).registerBeanDefinition(tempBeanName, tempComponentBeanDefinition);

                log.debug("Load the bean [{}] -> [{}] has been successfully.",
                        tempComponentName, tempClassName);
                return tempComponent;
            }
        } catch (NoClassDefFoundError ex) {
            throw new IllegalArgumentException("Cannot resolve dependencies for class: " + tempClassName, ex);
        } catch (Throwable te) {
            throw new IllegalArgumentException("Problem loading class:" + tempClassName, te);
        } finally {
        }
        return null;
    }

    @Override
    public <T extends IConfigurableComponent> T loadPrototypeComponent(String _parentPath, JSONObject _obj, int _index) {
        if (_obj.isEmpty()) {
            return null;
        }
        String tempClassName = _obj.getString(KEY_CLASS_NAME);
        String tempComponentName = this.getComponentName(_parentPath, _obj, _index);
        try {
            ClassLoader tempClassLoader = this.getClass().getClassLoader();
            Class<T> tempClass = (Class<T>) tempClassLoader.loadClass(tempClassName);
            if (null != tempClass) {
                String tempBeanName = DigestUtils.md5DigestAsHex(tempComponentName.getBytes());
                T tempComponent = tempClass.getDeclaredConstructor().newInstance();
                tempComponent.setParentPath(_parentPath);
                tempComponent.setAliasName(tempComponentName.substring(tempComponentName.lastIndexOf("\\") + 1));
//                tempComponent.setAliasName(tempComponentName.substring(tempComponentName.lastIndexOf("\\") + 1));

                tempComponent.configure(_obj);

                BeanDefinitionBuilder tempBeanDefinitionBuilder = BeanDefinitionBuilder
                        .genericBeanDefinition(tempClass, () -> tempComponent);
                BeanDefinition tempComponentBeanDefinition = tempBeanDefinitionBuilder
                        .getRawBeanDefinition();
                tempComponentBeanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
                ((DefaultListableBeanFactory) this.beanFactory).registerBeanDefinition(tempBeanName, tempComponentBeanDefinition);

                log.debug("Load the prototype bean [{}] -> [{}] successfully.",
                        tempComponentName, tempClassName);
                return tempComponent;
            }
        } catch (NoClassDefFoundError ex) {
            throw new IllegalArgumentException("Cannot resolve dependencies for class: " + tempClassName, ex);
        } catch (Throwable te) {
            throw new IllegalArgumentException("Problem loading class:" + tempClassName, te);
        } finally {
        }
        return null;
    }

    @Override
    public void registerCustomizeBean(Bean _bean) {

    }

    @Override
    public void registerBeanDefinition(String _beanName, BeanDefinition _definition) {

    }
}
