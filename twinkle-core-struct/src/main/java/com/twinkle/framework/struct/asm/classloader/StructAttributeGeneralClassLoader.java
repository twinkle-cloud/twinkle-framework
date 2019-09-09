package com.twinkle.framework.struct.asm.classloader;

import com.twinkle.framework.asm.classloader.EnhancedClassLoader;
import com.twinkle.framework.asm.designer.ClassDesigner;
import com.twinkle.framework.asm.designer.GeneralBeanClassDesigner;
import com.twinkle.framework.struct.asm.define.StructAttributeBeanTypeDef;
import com.twinkle.framework.struct.asm.define.StructAttributeGeneralBeanTypeDefImpl;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.resolver.StructAttributeTypeResolver;
import com.twinkle.framework.struct.type.StructAttributeType;
import org.objectweb.asm.Type;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/9/19 3:14 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeGeneralClassLoader extends EnhancedClassLoader {
    public static final String PACKAGE_PREFIX = "com.twinkle.framework.struct.beans.general.";
    private final StructAttributeTypeResolver typeResolver;

    public StructAttributeGeneralClassLoader(ClassLoader _classLoader, StructAttributeTypeResolver _resolver) {
        super(_classLoader);
        this.typeResolver = _resolver;
    }

    public StructAttributeGeneralClassLoader(StructAttributeTypeResolver _resolver) {
        this.typeResolver = _resolver;
    }

    @Override
    protected Class<?> findClass(String _className) throws ClassNotFoundException {
        if (_className != null && _className.startsWith(PACKAGE_PREFIX)) {
            StructAttributeType tempStructAttrType = null;

            try {
                tempStructAttrType = this.getStructAttributeType(StructAttributeGeneralBeanTypeDefImpl.getStructAttributeQualifiedTypeName(_className));
            } catch (StructAttributeException e) {
            }

            if (tempStructAttrType != null) {
                StructAttributeBeanTypeDef tempStructAttrBeanTypeDef;
                try {
                    tempStructAttrBeanTypeDef = this.createStructAttributeBeanTypeDef(tempStructAttrType);
                } catch (ClassNotFoundException e) {
                    throw new ClassNotFoundException("Failed to load class " + _className + " due to the nested class loading failure: " + e.getMessage(), e);
                }

                ClassDesigner tempDesigner = this.getClassDesigner(_className, tempStructAttrBeanTypeDef);
                Class<?> tempClass = this.defineClass(_className, tempDesigner);
                return tempClass;
            }
        }

        return super.findClass(_className);
    }

    protected StructAttributeType getStructAttributeType(String _attrName) {
        return this.typeResolver.getStructAttributeType(_attrName);
    }

    public String getStructAttributeGeneralClassName(String _className) {
        return StructAttributeGeneralBeanTypeDefImpl.getStructAttributeGeneralClassName(_className);
    }

    protected StructAttributeBeanTypeDef createStructAttributeBeanTypeDef(StructAttributeType _attrType) throws ClassNotFoundException {
        Type tempStructType = StructAttributeGeneralBeanTypeDefImpl.getType(_attrType);
        return new StructAttributeGeneralBeanTypeDefImpl(_attrType, tempStructType, this);
    }

    protected ClassDesigner getClassDesigner(String _className, StructAttributeBeanTypeDef _typeDef) {
        ClassDesigner tempDesigner = new GeneralBeanClassDesigner(_className, _typeDef);
        return tempDesigner;
    }
}
