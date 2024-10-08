package com.twinkle.framework.struct.asm.define;

import com.twinkle.framework.asm.builder.AnnotationDefBuilder;
import com.twinkle.framework.asm.builder.TypeDefBuilder;
import com.twinkle.framework.asm.define.*;
import com.twinkle.framework.asm.descriptor.AttributeDescriptor;
import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.asm.utils.BeanUtil;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.type.ArrayType;
import com.twinkle.framework.struct.type.BeanStructType;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.type.AttributeType;
import com.twinkle.framework.struct.util.StructAttributeArray;
import com.twinkle.framework.struct.utils.StructTypeUtil;
import lombok.Getter;
import org.objectweb.asm.Type;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/9/19 11:50 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class StructAttributeGeneralBeanTypeDefImpl extends BeanRefTypeDefImpl implements StructAttributeBeanTypeDef, Cloneable {
    protected StructType structType;
    protected List<TypeDef> interfaceTypeDefs;
    protected List<AttributeDef> attributes;
    protected List<AnnotationDef> annotations;

    public StructAttributeGeneralBeanTypeDefImpl(StructType _saType, Type _type, ClassLoader _classLoader) throws ClassNotFoundException {
        this(_saType, _type, _classLoader, new HashMap<>());
    }

    /**
     * Build the structAttribute General Bean TypeDefine.
     *
     * @param _saType
     * @param _type:       StructAttributeType's Type.
     * @param _classLoader
     * @param _parentMap
     * @throws ClassNotFoundException
     */
    public StructAttributeGeneralBeanTypeDefImpl(StructType _saType, Type _type, ClassLoader _classLoader, Map<String, TypeDef> _parentMap) throws ClassNotFoundException {
        super(_saType.getQualifiedName(), _type);
        this.structType = _saType;
        _parentMap.put(this.getType().getClassName(), this);
        this.interfaceTypeDefs = this.initBeanParents(_saType, _classLoader, _parentMap);
        this.attributes = this.initBeanAttributes(_saType, _classLoader, _parentMap);
        BeanTypeDescriptor tempBeanTypeDescriptor = null;
        if (_saType instanceof BeanStructType) {
            tempBeanTypeDescriptor = ((BeanStructType) _saType).getTypeDescriptor();
        }

        if (tempBeanTypeDescriptor != null) {
            this.annotations = this.initAnnotations(tempBeanTypeDescriptor.getAnnotations(), _classLoader);
        } else {
            this.annotations = Collections.emptyList();
        }

    }

    public StructAttributeGeneralBeanTypeDefImpl(StructAttributeBeanTypeDef _beanTypeDef) {
        super(_beanTypeDef.getName(), _beanTypeDef.getType());
        this.structType = _beanTypeDef.getStructType();
        this.interfaceTypeDefs = new ArrayList(_beanTypeDef.getInterfaces());
        this.attributes = new ArrayList(_beanTypeDef.getAttributes());
        this.annotations = new ArrayList(_beanTypeDef.getAnnotations());
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        StructAttributeGeneralBeanTypeDefImpl tempDestObj = (StructAttributeGeneralBeanTypeDefImpl) super.clone();
        tempDestObj.interfaceTypeDefs = new ArrayList(this.interfaceTypeDefs);
        tempDestObj.attributes = new ArrayList(this.attributes);
        tempDestObj.annotations = new ArrayList(this.annotations);
        return tempDestObj;
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
    protected List<TypeDef> initBeanParents(StructType _saType, ClassLoader _classLoader, Map<String, TypeDef> _parentMap) throws ClassNotFoundException {
        return new ArrayList(1);
    }

    /**
     * Initialize the bean's attributes.
     *
     * @param _saType
     * @param _classLoader
     * @param _parentMap
     * @return
     * @throws ClassNotFoundException
     */
    protected List<AttributeDef> initBeanAttributes(StructType _saType, ClassLoader _classLoader, Map<String, TypeDef> _parentMap) throws ClassNotFoundException {
        BeanTypeDescriptor tempBeanTypeDescriptor = null;
        if (_saType instanceof BeanStructType) {
            tempBeanTypeDescriptor = ((BeanStructType) _saType).getTypeDescriptor();
        }

        List<AttributeDef> tempResultList = new ArrayList(_saType.size());
        Iterator<SAAttributeDescriptor> tempItr = _saType.getAttributes();
        while (tempItr.hasNext()) {
            SAAttributeDescriptor tempSADescriptor = tempItr.next();
            AttributeDescriptor tempAttributeDescriptor;
            List<AnnotationDef> tempAnnotationList;
            if (tempBeanTypeDescriptor != null) {
                tempAttributeDescriptor = tempBeanTypeDescriptor.getAttribute(tempSADescriptor.getName());
                tempAnnotationList = this.initAnnotations(tempAttributeDescriptor.getAnnotations(), _classLoader);
            } else {
                tempAttributeDescriptor = null;
                tempAnnotationList = new ArrayList<>();
            }

            AttributeType tempStructType = tempSADescriptor.getType();
            TypeDef tempTypeDef = getAttributeTypeDef(tempStructType, _parentMap);
            if (tempStructType.isArrayType()) {
                Set<String> tempAnnotationSet = new HashSet<>(1);
                StringBuilder tempBuilder = new StringBuilder("@io.swagger.v3.oas.annotations.media.Schema(name=\"");
                tempBuilder.append(tempSADescriptor.getName());
                tempBuilder.append("\", types=\"[L");//dataType
                if(tempTypeDef.isGeneric()) {
                    Type[] tempGenericTypes = ((GenericTypeDef)tempTypeDef).getTypeParameters();
                    if(tempGenericTypes != null) {
//                        tempBuilder.append("<");
                        for(int i = 0; i< tempGenericTypes.length; i++) {
                            if(i>0) {
                                tempBuilder.append(",");
                            }
                            tempBuilder.append(tempGenericTypes[i].getClassName());
                        }
                        tempBuilder.append(";");
//                        tempBuilder.append(">");
                    }
                }
                tempBuilder.append("\")");

                tempAnnotationSet.add(tempBuilder.toString());
                tempAnnotationList.addAll(this.initAnnotations(tempAnnotationSet, this.getClass().getClassLoader()));
            }

            if (tempBeanTypeDescriptor != null) {
                tempResultList.add(new StructAttributeAttributeDefImpl(tempAttributeDescriptor, tempTypeDef, tempAnnotationList));
            } else {
                tempResultList.add(new StructAttributeAttributeDefImpl(tempSADescriptor.getName(), tempTypeDef, tempAnnotationList, false, !tempSADescriptor.isOptional(), null));
            }
        }

        return tempResultList;
    }


    /**
     * Get the annotation list.
     *
     * @param _annotationSet
     * @param _classLoader
     * @return
     * @throws ClassNotFoundException
     */
    protected List<AnnotationDef> initAnnotations(Set<String> _annotationSet, ClassLoader _classLoader) throws ClassNotFoundException {
        List<AnnotationDef> tempResultList = new ArrayList(_annotationSet.size());
        Iterator<String> tempItr = _annotationSet.iterator();
        while (tempItr.hasNext()) {
            String tempAnnotationItem = tempItr.next();
            if (tempAnnotationItem.startsWith("@")) {
                tempResultList.add(AnnotationDefBuilder.getAnnotationDef(tempAnnotationItem, _classLoader));
            }
        }

        return tempResultList;
    }

    /**
     * Build the struct type's type def.
     *
     * @param _structType
     * @param _typeMap
     * @return
     * @throws ClassNotFoundException
     */
    private TypeDef getAttributeTypeDef(AttributeType _structType, Map<String, TypeDef> _typeMap) throws ClassNotFoundException {
        if (_structType.isStructType()) {
            StructType tempSAType = (StructType) _structType;
            Type tempType = getASMType(tempSAType);
            String tempClassName = tempType.getClassName();
            TypeDef tempTypeDef = _typeMap.get(tempClassName);
            return (tempTypeDef != null ? tempTypeDef : new BeanRefTypeDefImpl(tempClassName, getASMType(tempSAType)));
        } else if (_structType.isArrayType()) {
            ArrayType tempArrayType = (ArrayType) _structType;
            AttributeType tempElementType = tempArrayType.getElementType();
            if (tempElementType.isStructType()) {
                StructType tempElementSAType = (StructType) tempElementType;
                Type tempType = getASMType(tempElementSAType);
                String tempClassName = tempType.getClassName();
                Object tempTypeDefObj = _typeMap.get(tempClassName);
                if (tempTypeDefObj == null) {
                    tempTypeDefObj = new BeanRefTypeDefImpl(tempClassName, getASMType(tempElementSAType));
                }

                return new GenericTypeDefImpl(tempArrayType.getName(), StructAttributeArray.class, new Type[]{((TypeDef) tempTypeDefObj).getType()});
            } else {
                Class<?> tempTypeClass = StructTypeUtil.getTypeClass(_structType);
                return new ClassTypeDefImpl(tempArrayType.getName(), tempTypeClass);
            }
        } else {
            Class<?> tempTypeClass = StructTypeUtil.getTypeClass(_structType);
            return new ClassTypeDefImpl(_structType.getName(), tempTypeClass);
        }
    }

    /**
     * Get StructAttributeType's type.
     * ONLY for General Bean.
     *
     * @param _saType
     * @return
     */
    private Type getASMType(StructType _saType) {
        String tempInterfaceName = getSAInterfaceName(_saType.getQualifiedName());
        return TypeDefBuilder.getObjectType(tempInterfaceName);
    }

    protected String getSAInterfaceName(String _className) {
        return BeanUtil.getStructAttributeInterfaceName(_className);
    }

    @Override
    public TypeDef addInterfaceTypeDef(Type _interfaceType) {
        for (TypeDef tempItem : this.interfaceTypeDefs) {
            if (tempItem.getType().equals(_interfaceType)) {
                return tempItem;
            }
        }
        BeanRefTypeDefImpl tempRefDef = new BeanRefTypeDefImpl(_interfaceType.getClassName(), _interfaceType);
        this.interfaceTypeDefs.add(tempRefDef);
        return tempRefDef;
    }

    @Override
    public List<String> getInterfaces() {
        return this.interfaceTypeDefs.stream().map(item -> item.getType().getClassName()).collect(Collectors.toList());
    }

    @Override
    public TypeDef getSuperTypeDef() {
        return null;
    }

    @Override
    public StructType getStructType() {
        return this.structType;
    }
}
