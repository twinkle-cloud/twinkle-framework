package com.twinkle.framework.core.asm.classloader;

import com.twinkle.framework.core.asm.designer.ClassDesigner;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 4:12 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class SerializerClassLoader extends EnhancedClassLoader {
    public SerializerClassLoader(boolean _isInitialize, ClassLoader _classLoader) {
        super(_isInitialize, _classLoader);
    }

    public SerializerClassLoader(ClassLoader _classLoader) {
        super(_classLoader);
    }

    public SerializerClassLoader() {
    }

    public boolean isClassDefined(String _className) {
        return this.findLoadedClass(_className) != null;
    }

    public Class<?> defineClass(ClassDesigner _designer) {
        return super.defineClass(_designer.getCanonicalClassName(), _designer);
    }

}
