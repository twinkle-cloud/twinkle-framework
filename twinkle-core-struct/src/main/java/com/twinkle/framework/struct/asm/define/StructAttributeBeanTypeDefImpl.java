package com.twinkle.framework.struct.asm.define;

import com.twinkle.framework.asm.builder.TypeDefBuilder;
import com.twinkle.framework.asm.define.BeanRefTypeDefImpl;
import com.twinkle.framework.asm.define.TypeDef;
import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.struct.type.BeanStructType;
import com.twinkle.framework.struct.type.StructType;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 8:18 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeBeanTypeDefImpl extends StructAttributeGeneralBeanTypeDefImpl {
    private static final String PACKAGE_PREFIX = "com.twinkle.framework.struct.beans.";
    private static final String IMPL_SUFFIX = "Impl";
    private static final String IMPL_IMPL_BUILDER_SUFFIX = "Impl$ImplBuilder";

    public StructAttributeBeanTypeDefImpl(StructType _saType, ClassLoader _classLoader) throws ClassNotFoundException {
        this(_saType, _classLoader, new HashMap<>());
    }

    public StructAttributeBeanTypeDefImpl(StructType _saType, ClassLoader _classLoader, Map<String, TypeDef> _parentMap) throws ClassNotFoundException {
        super(_saType, getType(_saType), _classLoader, _parentMap);
    }

    public StructAttributeBeanTypeDefImpl(StructAttributeBeanTypeDef _beanTypeDef) {
        super(_beanTypeDef);
    }

    /**
     * Initialize the bean's parents
     *
     * @param _saType
     * @param _classLoader
     * @param _parentMap
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected List<TypeDef> initBeanParents(StructType _saType, ClassLoader _classLoader, Map<String, TypeDef> _parentMap) throws ClassNotFoundException {
        BeanTypeDescriptor tempBeanTypeDescriptor = null;
        if (_saType instanceof BeanStructType) {
            tempBeanTypeDescriptor = ((BeanStructType) _saType).getTypeDescriptor();
        }
        if (tempBeanTypeDescriptor != null) {
            List<TypeDef> tempResultList = new ArrayList(tempBeanTypeDescriptor.getInterfaceDescriptors().size());
            for (BeanTypeDescriptor tempItem : tempBeanTypeDescriptor.getInterfaceDescriptors()) {
                String tempInterfaceName = tempItem.getClassName();
                TypeDef tempTypeDef = _parentMap.get(tempInterfaceName);
                if (tempTypeDef == null) {
                    tempTypeDef = new BeanRefTypeDefImpl(tempInterfaceName, getType(tempInterfaceName));
                }
                tempResultList.add(tempTypeDef);
            }

            return tempResultList;
        } else {
            return new ArrayList(1);
        }
    }

    public BeanTypeDescriptor getDescriptor() {
        return this.structType instanceof BeanStructType ? ((BeanStructType) this.structType).getTypeDescriptor() : null;
    }

    public static Type getType(StructType _saType) {
        String tempInterfaceName = getStructAttributeInterfaceName(_saType.getQualifiedName());
        return TypeDefBuilder.getObjectType(tempInterfaceName);
    }

    /**
     * Build the type with type name.
     *
     * @param _typeName
     * @return
     */
    public static Type getType(String _typeName) {
        return TypeDefBuilder.getObjectType(_typeName);
    }

    @Override
    protected String getSAInterfaceName(String _className) {
        return getStructAttributeInterfaceName(_className);
    }

    /**
     * Get the StructAttribute's interface name.
     *
     * @param _className
     * @return
     */
    protected static String getStructAttributeInterfaceName(String _className) {
        return _className.startsWith(PACKAGE_PREFIX) ? _className : PACKAGE_PREFIX + _className.replace(':', '.');
    }

    /**
     * Get the StructAttribute's class name.
     *
     * @param _className
     * @return
     */
    public static String getStructAttributeClassName(String _className) {
        return getStructAttributeInterfaceName(_className) + IMPL_SUFFIX;
    }

    /**
     * Get the StructAttribute's builder's name.
     *
     * @param _className
     * @return
     */
    public static String getStructAttributeBuilderClassName(String _className) {
        return getStructAttributeInterfaceName(_className) + IMPL_IMPL_BUILDER_SUFFIX;
    }

    /**
     * Get the StructAttribute's qualified name.
     *
     * @param _typeName
     * @return
     */
    public static String getStructAttributeQualifiedTypeName(String _typeName) {
        return _typeName.substring(PACKAGE_PREFIX.length()).replace('.', ':');
    }
}
