package com.twinkle.framework.configure.factory;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.core.asm.classloader.BeanClassLoader;
import com.twinkle.framework.core.asm.factory.BeanFactoryImpl;

import javax.naming.ConfigurationException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 15:00<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ConfigurableBeanFactoryImpl extends BeanFactoryImpl {
    private final ClassLoader _parentLoader;

    public ConfigurableBeanFactoryImpl(ClassLoader var1) {
        super((BeanClassLoader)null);
        this._parentLoader = var1;
    }

    public ConfigurableBeanFactoryImpl() {
        super((BeanClassLoader)null);
        this._parentLoader = null;
    }
    @Override
    public BeanClassLoader getBeanClassLoader() {
        BeanClassLoader var1 = super.getBeanClassLoader();
        if (var1 == null) {
            throw new IllegalStateException("BeanFactory wasn't initialized");
        } else {
            return var1;
        }
    }

    public void configure(JSONObject var1) throws ConfigurationException {
        if (super.getBeanClassLoader() == null) {

//            TypeDescriptors var3;
//            try {
//            } catch (IOException var5) {
//                throw new ConfigurationException(var1, "Failed loading bean types", var5);
//            }

//            if (this._parentLoader != null) {
//                super.setBeanClassLoader(this.initBeanClassLoader(this._parentLoader, var3));
//            } else {
//                super.setBeanClassLoader(this.initBeanClassLoader(var3));
//            }
        }

    }
}
