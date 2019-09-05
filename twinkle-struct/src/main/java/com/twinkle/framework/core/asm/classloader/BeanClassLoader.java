package com.twinkle.framework.core.asm.classloader;

import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.TypeDescriptors;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-08 11:37<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class BeanClassLoader extends AbstractBeanClassLoader {
    private final TypeDescriptors typeDescriptors;

    public BeanClassLoader(ClassLoader _classLoader, TypeDescriptors _typeDescriptors, Class<? extends Bean> _class) {
        super(_classLoader, _class);
        this.typeDescriptors = _typeDescriptors;
    }

    public BeanClassLoader(ClassLoader _classLoader, TypeDescriptors _typeDescriptors) {
        super(_classLoader);
        this.typeDescriptors = _typeDescriptors;
    }

    public BeanClassLoader(TypeDescriptors _typeDescriptors) {
        this.typeDescriptors = _typeDescriptors;
    }
    @Override
    protected TypeDescriptor getTypeDescriptor(String _typeName) {
        return this.typeDescriptors.getType(_typeName);
    }

    public TypeDescriptors getTypeDescriptors() {
        return this.typeDescriptors;
    }

}
