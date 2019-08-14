package com.twinkle.framework.configure.component;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.core.datastruct.Bean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.support.GenericWebApplicationContext;

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
@Configuration
public class ComponentFactory implements IComponentFactory {
    @Autowired
    private GenericWebApplicationContext context;

    @Override
    public <T extends Configurable> T loadComponent(JSONObject _obj) {
        String tempClassName = _obj.getString(KEY_CLASS_NAME);

        try {
            ClassLoader tempClassLoader = Thread.currentThread().getContextClassLoader();
            Class tempClass = tempClassLoader.loadClass(tempClassName);
            if (null != tempClass) {
                String tempMd5Hash = DigestUtils.md5DigestAsHex(_obj.toString().getBytes());
                Object tempObj = context.getBean(tempMd5Hash);
                if(tempObj != null) {
                    log.debug("Found the duplicate bean [{}] -> [{}] had been registered already.",
                            tempMd5Hash, tempClassName);
                    return (T) tempObj;
                }
                T tempComponnet = (T)tempClass.newInstance();
                tempComponnet.configure(_obj);
                context.registerBean(tempMd5Hash,
                        tempClass, () -> tempComponnet);

                log.debug("Load the bean [{}] -> [{}] had been successfully.",
                        tempMd5Hash, tempClassName);
                return tempComponnet;
            }
        } catch (NoClassDefFoundError ex) {
            throw new IllegalArgumentException("Cannot resolve dependencies for class: " + tempClassName);
        } catch (Throwable te) {
            throw new IllegalArgumentException("Problem loading class:" + tempClassName);
        }
        return null;
    }

    @Override
    public void registerCustomizeBean(Bean _bean) {
//        try {
//            if (null != _bean) {
//                context.registerBean("",
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
}
