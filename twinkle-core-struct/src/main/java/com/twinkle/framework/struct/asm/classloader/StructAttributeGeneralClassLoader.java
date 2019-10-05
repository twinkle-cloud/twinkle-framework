package com.twinkle.framework.struct.asm.classloader;

import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.classloader.EnhancedClassLoader;
import com.twinkle.framework.asm.designer.ClassDesigner;
import com.twinkle.framework.asm.designer.GeneralBeanClassDesigner;
import com.twinkle.framework.asm.utils.BeanUtil;
import com.twinkle.framework.struct.asm.define.StructAttributeBeanTypeDef;
import com.twinkle.framework.struct.asm.define.StructAttributeGeneralBeanTypeDefImpl;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.resolver.StructAttributeTypeResolver;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.utils.StructTypeUtil;
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
        if (_className != null && _className.startsWith(Bean.STRUCT_ATTRIBUTE_GENERAL_PACKAGE)) {
            StructType tempStructAttrType = null;

            try {
                tempStructAttrType = this.getStructAttributeType(BeanUtil.getStructAttributeQualifiedTypeName(_className));
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

    protected StructType getStructAttributeType(String _attrName) {
        return this.typeResolver.getStructAttributeType(_attrName);
    }

    public String getStructAttributeGeneralClassName(String _className) {
        return BeanUtil.getStructAttributeGeneralClassName(_className);
    }

    protected StructAttributeBeanTypeDef createStructAttributeBeanTypeDef(StructType _attrType) throws ClassNotFoundException {
        Type tempStructType = StructTypeUtil.getType(_attrType);
        return new StructAttributeGeneralBeanTypeDefImpl(_attrType, tempStructType, this);
    }

    protected ClassDesigner getClassDesigner(String _className, StructAttributeBeanTypeDef _typeDef) {
        ClassDesigner tempDesigner = new GeneralBeanClassDesigner(_className, _typeDef);
        return tempDesigner;
    }
}
