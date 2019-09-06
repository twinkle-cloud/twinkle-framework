package com.twinkle.framework.connector.http.server.classloader;

import com.twinkle.framework.asm.classloader.EnhancedClassLoader;
import com.twinkle.framework.asm.define.GeneralClassTypeDef;
import com.twinkle.framework.asm.define.GeneralClassTypeDefImpl;
import com.twinkle.framework.asm.descriptor.GeneralClassTypeDescriptor;
import com.twinkle.framework.asm.designer.ClassDesigner;
import com.twinkle.framework.connector.http.server.designer.RestControllerClassDesigner;

import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-17 14:52<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class RestControllerClassLoader extends EnhancedClassLoader {
    private GeneralClassTypeDescriptor typeDescriptor;
    public RestControllerClassLoader(ClassLoader _parentLoader, GeneralClassTypeDescriptor _typeDescriptoer) {
        super(_parentLoader);
        this.typeDescriptor = _typeDescriptoer;
    }
    @Override
    protected Class<?> findClass(String _className) throws ClassNotFoundException {
        if(_className.equals(typeDescriptor.getClassName())) {
            GeneralClassTypeDef tempTypeDef = new GeneralClassTypeDefImpl(this.typeDescriptor, this);
            ClassDesigner tempDesigner = new RestControllerClassDesigner(_className, tempTypeDef);
            Class<?> tempClass = this.defineClass(_className, tempDesigner);
            return tempClass;
        }
        Class<?> tempClass = super.findClass(_className);
        return tempClass;
    }
}
