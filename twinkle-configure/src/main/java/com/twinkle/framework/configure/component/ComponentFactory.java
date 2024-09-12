package com.twinkle.framework.configure.component;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.IComponentFactory;
import com.twinkle.framework.api.component.IConfigurableComponent;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.asm.Bean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
public class ComponentFactory extends AbstractComponentFactory {
    private BeanDefinitionRegistry registry;

    private static IComponentFactory componentFactory;

    public ComponentFactory(BeanDefinitionRegistry _registry) {
        this.registry = _registry;
        componentFactory = this;
    }

    public ComponentFactory(ConfigurableListableBeanFactory beanFactory) {
        componentFactory = new LauncherComponentFactory(beanFactory);
    }

    public static IComponentFactory getInstance() {
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
                    this.registry.getBeanDefinition(tempBeanName);
                    log.debug("Found the duplicate bean [{}] -> [{}] had been registered already.",
                            tempComponentName, tempClassName);
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_DUPLICATE_COMPONENT_FOUND, "Found the duplicate bean [" + tempComponentName + "] had been registered already.");
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
                this.registry.registerBeanDefinition(tempBeanName, tempComponentBeanDefinition);

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
                this.registry.registerBeanDefinition(tempBeanName, tempComponentBeanDefinition);

                log.debug("Load the prototype bean [{}] -> [{}] has been successfully.",
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
