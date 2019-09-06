package com.twinkle.framework.asm.factory;

import com.twinkle.framework.asm.classloader.BeanClassLoader;
import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.descriptor.TypeDescriptors;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 14:54<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class BeanFactoryUnsafeImpl extends BeanFactoryReflectionImpl {
    private static final Unsafe _unsafe = getUnsafe();

    private static Unsafe getUnsafe() {
        try {
            Field tempField = Unsafe.class.getDeclaredField("theUnsafe");
            tempField.setAccessible(true);
            return (Unsafe)tempField.get(null);
        } catch (Exception e) {
            throw new RuntimeException("can't get Unsafe instance", e);
        }
    }

    public BeanFactoryUnsafeImpl(BeanClassLoader var1) {
        super(var1);
    }

    public BeanFactoryUnsafeImpl(ClassLoader var1, TypeDescriptors var2) {
        super(var1, var2);
    }

    public BeanFactoryUnsafeImpl(TypeDescriptors var1) {
        super(var1);
    }

    @Override
    protected <T extends Bean> T createNewInstance(Class<T> _class) {
        try {
            return (T)_unsafe.allocateInstance(_class);
        } catch (InstantiationException e) {
            _unsafe.throwException(e);
            return null;
        }
    }
}
