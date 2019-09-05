package com.twinkle.framework.core.asm.designer;

import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.Blob;
import com.twinkle.framework.core.datastruct.converter.AttributeConverter;
import com.twinkle.framework.core.datastruct.converter.LooseAttributeConverter;
import com.twinkle.framework.core.datastruct.define.*;
import com.twinkle.framework.core.utils.TypeUtil;
import lombok.Getter;
import org.objectweb.asm.*;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-03 18:55<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public abstract class AbstractBeanClassDesigner extends AbstractClassDesigner {
    protected static final String FLAG_SUFFIX = "Flag";
    protected static final String DEFAULT_SUFFIX = "Default";
    protected static final Type BLOB_TYPE = Type.getType(Blob.class);
    private static final Set<String> RESERVED_NAMES = new HashSet(Arrays.asList("getClass", "getType"));
    private final String className;
    private final List<String> beanInterfaces;
    private final List<AttributeDef> beanAttributes;
    private final BeanTypeDef beanTypeDef;

    List<Type> _notToClInitDefaults = Arrays.asList(TypeUtil.STRING_TYPE);
    List<Type> _notToCloneDefaults = Arrays.asList(TypeUtil.STRING_TYPE, Type.getType(BigDecimal.class), Type.getType(BigInteger.class));

    public AbstractBeanClassDesigner(String _className, BeanTypeDef _beanTypeDef) {
        this.beanTypeDef = _beanTypeDef;
        this.className = this.toInternalName(_className);
        this.beanInterfaces = this.toInternalNames(_beanTypeDef.getInterfaces());
        List<String> tempDefaultInterfacesList = this.toInternalNames(this.getDefaultInterfaces());
        tempDefaultInterfacesList.stream().filter(item -> !this.beanInterfaces.contains(item)).forEach(item ->
                this.beanInterfaces.add(item));

        AttributeConverter tempAttrConverter = this.initAttributeConverter(_className);
        this.beanAttributes = tempAttrConverter.normalize(_beanTypeDef.getAttributes());
    }

    /**
     * Initialize the attribute converter.
     * Use the @LooseAttributeConverter by default.
     *
     * @param _className
     * @return
     */
    protected AttributeConverter initAttributeConverter(String _className) {
        return new LooseAttributeConverter(_className, RESERVED_NAMES);
    }

    @Override
    public String getCanonicalClassName() {
        return this.toCanonicalName(this.className);
    }

    /**
     * The bean class always implement the root Bean interface.
     *
     * @return
     */
    protected String[] getDefaultInterfaces() {
        return new String[]{Bean.class.getName()};
    }

    protected ClassVisitor addClassDeclaration(ClassVisitor _visitor, String _className, List<String> _interfaceList, BeanTypeDef _beanTypeDef) {
        return this.addClassDeclaration(_visitor, _className, TypeUtil.OBJECT_TYPE.getInternalName(), _interfaceList, _beanTypeDef);
    }

    /**
     * Visit the header of the class.
     *
     * @param _visitor
     * @param _className
     * @param _superName
     * @param _interfaceList
     * @param _beanTypeDef
     * @return
     */
    protected ClassVisitor addClassDeclaration(ClassVisitor _visitor, String _className, String _superName, List<String> _interfaceList, BeanTypeDef _beanTypeDef) {
        _visitor.visit(TARGET_JVM, this.initAccessFlags(), _className, null, _superName, _interfaceList.toArray(new String[_interfaceList.size()]));
        return _visitor;
    }

    @Override
    protected ClassVisitor addClassDeclaration(ClassVisitor _visitor) {
        String tempSuperName = this.beanTypeDef.getSuperTypeDef().getType().getInternalName();//TypeUtil.OBJECT_TYPE.getInternalName()
        return this.addClassDeclaration(_visitor, this.className, tempSuperName, this.beanInterfaces, this.beanTypeDef);
    }

    protected void addClassDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _interfaceAttrDefList, BeanTypeDef _beanTypeDef) {
        //TypeUtil.OBJECT_TYPE.getInternalName()
        String tempSuperName = this.beanTypeDef.getSuperTypeDef().getType().getInternalName();//TypeUtil.OBJECT_TYPE.getInternalName()
        this.addClassDefinition(_visitor, _className, tempSuperName, _interfaceAttrDefList, _beanTypeDef);
    }

    @Override
    protected void addClassDefinition(ClassVisitor _visitor) {
        //TypeUtil.OBJECT_TYPE.getInternalName()
        String tempSuperName = this.beanTypeDef.getSuperTypeDef().getType().getInternalName();//TypeUtil.OBJECT_TYPE.getInternalName()
        this.addClassDefinition(_visitor, this.className, tempSuperName, this.beanAttributes, this.beanTypeDef);
    }

    protected abstract void addClassDefinition(ClassVisitor _visitor, String _className, String _superName, List<AttributeDef> _attrDefList, BeanTypeDef _beanTypeDef);

    /**
     * Add Annotation for the class.
     *
     * @param _visitor
     * @param _annotationDef
     * @return
     */
    protected AnnotationVisitor addAnnotationElements(AnnotationVisitor _visitor, AnnotationDef _annotationDef) {
        _annotationDef.getElements().stream().forEach(item -> {
            Type tempType = item.getType();
            Object tempValue = item.getValue();
            if (tempType.getSort() == Type.ARRAY) {
                Type tempElementType = tempType.getElementType();
                AnnotationVisitor tempAnnotationVisitor = _visitor.visitArray(item.getName());

                for(int i = 0; i < Array.getLength(tempValue); i++) {
                    Object tempItemValueObj = Array.get(tempValue, i);
                    if (tempItemValueObj instanceof AnnotationDef) {
                        AnnotationVisitor tempAnnotationVisitor2 = tempAnnotationVisitor.visitAnnotation(null, tempElementType.getDescriptor());
                        this.addAnnotationElements(tempAnnotationVisitor2, (AnnotationDef)tempItemValueObj);
                        tempAnnotationVisitor2.visitEnd();
                    } else if (tempItemValueObj instanceof String[]) {
                        String[] typeValue = (String[]) tempItemValueObj;
                        tempAnnotationVisitor.visitEnum(null, typeValue[0], typeValue[1]);
                    } else {
                        tempAnnotationVisitor.visit(null, tempItemValueObj);
                    }
                }
                tempAnnotationVisitor.visitEnd();
            } else if (tempValue instanceof AnnotationDef) {
                AnnotationVisitor tempAnnotationVisitor = _visitor.visitAnnotation(null, tempType.getDescriptor());
                this.addAnnotationElements(tempAnnotationVisitor, (AnnotationDef)tempValue);
                tempAnnotationVisitor.visitEnd();
            } else if (tempValue instanceof String[]) {
                String[] typeValue = (String[]) tempValue;
                _visitor.visitEnum(item.getName(), typeValue[0], typeValue[1]);
            } else {
                _visitor.visit(item.getName(), tempValue);
            }
        });
        return _visitor;
    }

    /**
     * Add the field's annotation for the given field.
     *
     * @param _visitor
     * @param _annotationDef
     * @return
     */
    protected AnnotationVisitor addFieldAnnotation(FieldVisitor _visitor, AnnotationDef _annotationDef) {
        if (_annotationDef.getKind() != AnnotationDef.Kind.GETTER && _annotationDef.getKind() != AnnotationDef.Kind.SETTER) {
            AnnotationVisitor tempVisitor = _visitor.visitAnnotation(_annotationDef.getType().getDescriptor(), true);
            this.addAnnotationElements(tempVisitor, _annotationDef);
            tempVisitor.visitEnd();
            return tempVisitor;
        } else {
            return null;
        }
    }

    /**
     * Add the class's annotation for the given class.
     *
     * @param _visitor
     * @param _annotationDef
     * @return
     */
    protected AnnotationVisitor addClassAnnotation(ClassVisitor _visitor, AnnotationDef _annotationDef) {
        AnnotationVisitor tempVisitor = _visitor.visitAnnotation(_annotationDef.getType().getDescriptor(), true);
        this.addAnnotationElements(tempVisitor, _annotationDef);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add the method's annotation for the given method.
     *
     * @param _visitor
     * @param _annotationDef
     * @param _kind
     * @return
     */
    protected AnnotationVisitor addMethodAnnotation(MethodVisitor _visitor, AnnotationDef _annotationDef, AnnotationDef.Kind _kind) {
//        if (_annotationDef.getKind() == _kind) {
            AnnotationVisitor tempVisitor = _visitor.visitAnnotation(_annotationDef.getType().getDescriptor(), true);
            this.addAnnotationElements(tempVisitor, _annotationDef);
            tempVisitor.visitEnd();
            return tempVisitor;
//        } else {
//            return null;
//        }
    }

    /**
     * Add the default constants for the attributes in the class.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     */
    protected void addDefaultConstants(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        List<AttributeDef> tempAttrDefList = new ArrayList(_attrDefList.size());
        _attrDefList.stream().filter(item -> item.getDefaultValue() != null).forEach(item -> {
            boolean tempFlag = this.clinit(item);
            this.addDefaultConstant(_visitor, item, tempFlag ? null : item.getDefaultValue());
            if (tempFlag) {
                tempAttrDefList.add(item);
            }
        });

        if (!tempAttrDefList.isEmpty()) {
            this.addClassConstructorDefinition(_visitor, _className, tempAttrDefList);
        }
    }

    /**
     * Judge the attribute is valid for clinit() method or not?
     *
     * @param _attrDef
     * @return
     */
    protected boolean clinit(AttributeDef _attrDef) {
        return !_attrDef.getType().isPrimitive() && !this._notToClInitDefaults.contains(_attrDef.getType().getType());
    }

    protected boolean isMutableDefault(AttributeDef _attrDef) {
        return !_attrDef.getType().isPrimitive() && !_attrDef.getType().isEnum() && !this._notToCloneDefaults.contains(_attrDef.getType().getType());
    }

    protected boolean isMutableDefaultInterface(AttributeDef _attrDef) {
        return false;
    }

    /**
     * Add default constant for some attribute(field).
     *
     * @param _visitor
     * @param _attrDef
     * @param _value
     * @return
     */
    protected FieldVisitor addDefaultConstant(ClassVisitor _visitor, AttributeDef _attrDef, Object _value) {
        String tempSignature;
        if (_attrDef.getType().isGeneric()) {
            tempSignature = TypeUtil.getGenericFieldSignature(_attrDef.getType());
        } else {
            tempSignature = null;
        }
        Type tepmFieldType = _attrDef.getType().getType();
        FieldVisitor tempFieldVistor = _visitor.visitField(_attrDef.getAccess(), _attrDef.getFieldName(), TypeUtil.getFieldDescriptor(tepmFieldType), tempSignature, _value);
        tempFieldVistor.visitEnd();
        return tempFieldVistor;
    }

    /**
     * Add Class cinit() Constructor Definition.
     * static segment for the bean.
     * If the attribute is static and has default value, then will be used to create the contructor.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected MethodVisitor addClassConstructorDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        String tempMethodSignature = TypeUtil.getMethodDescriptor(new Class[0], Void.TYPE);
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_STATIC, "<clinit>", tempMethodSignature, null, null);
        tempVisitor.visitCode();

        AtomicInteger tempInitCount = new AtomicInteger(0);
        _attrDefList.stream().filter(item -> item.getDefaultValue() != null).forEach(item -> {
            Object tempDefaultValue = item.getDefaultValue();
            boolean tempFlag = item.getType().getType().getSort() == Type.ARRAY;
            if (tempFlag) {
                Type tempElementType = item.getType().getType().getElementType();
                int tempValueListSize = Array.getLength(tempDefaultValue);
                tempVisitor.visitLdcInsn(tempValueListSize);
                if (TypeUtil.isPrimitive(tempElementType)) {
                    tempVisitor.visitIntInsn(Opcodes.NEWARRAY, TypeUtil.getNewArrayOpcode(tempElementType));
                } else {
                    tempVisitor.visitTypeInsn(Opcodes.ANEWARRAY, tempElementType.getInternalName());
                }

                for(int i = 0; i < tempValueListSize; ++i) {
                    tempVisitor.visitInsn(Opcodes.DUP);
                    tempVisitor.visitLdcInsn(i);
                    Object tempItemValue = Array.get(tempDefaultValue, i);
                    if (tempItemValue != null) {
                        if (!TypeUtil.isPrimitive(tempElementType) && !tempElementType.equals(TypeUtil.STRING_TYPE)) {
                            tempVisitor.visitTypeInsn(Opcodes.NEW, tempElementType.getInternalName());
                            tempVisitor.visitInsn(Opcodes.DUP);
                            tempVisitor.visitLdcInsn(tempItemValue);
                            tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, tempElementType.getInternalName(), "<init>", TypeUtil.getMethodDescriptor(new Class[]{tempItemValue.getClass()}, Void.TYPE));
                        } else {
                            tempVisitor.visitLdcInsn(tempItemValue);
                        }
                    } else {
                        tempVisitor.visitInsn(TypeUtil.getNullOpcode(tempElementType));
                    }

                    tempVisitor.visitInsn(TypeUtil.getOpcode(tempElementType, Opcodes.IASTORE));
                }

                tempVisitor.visitFieldInsn(Opcodes.PUTSTATIC, _className, this.getDefaultConstantName(item), TypeUtil.getFieldDescriptor(item.getType().getType()));
            } else if (tempDefaultValue instanceof StaticAttributeValueDef) {
                tempVisitor.visitLdcInsn(Type.getObjectType(_className));
                StaticAttributeValueDef tempValue = (StaticAttributeValueDef) tempDefaultValue;
                tempVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, tempValue.getClassInternalName(), tempValue.getMethodName(), tempValue.getMethodDescriptor());
                tempVisitor.visitFieldInsn(Opcodes.PUTSTATIC, _className, item.getFieldName(), TypeUtil.getFieldDescriptor(item.getType().getType()));
            } else {
                this.addObjectClassConstructorDefinition(tempVisitor, _className, item, tempInitCount.getAndIncrement());
            }
        });

        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add Object class constructor.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     * @param _parameterSize
     * @return
     */
    protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize) {
        Object tempValueObj = _attrDef.getDefaultValue();
        if (tempValueObj != null) {
            Type tempAttrType = _attrDef.getType().getType();
            Class tempValueClass = tempValueObj.getClass();
            if (_attrDef.getType().isEnum()) {
                EnumTypeDef tempEnumType = (EnumTypeDef)_attrDef.getType();
                List<String> tempEnumNameList = tempEnumType.getEnumNames();
                if (!tempEnumNameList.contains(tempValueObj)) {
                    throw new IllegalArgumentException("Name specified by default [" + tempValueObj + "] does not belong to the enumeration " + tempEnumType.getName() + tempEnumNameList.toString());
                }

                _visitor.visitLdcInsn(tempValueObj);
                _visitor.visitMethodInsn(Opcodes.INVOKESTATIC, tempAttrType.getInternalName(), "valueOf", TypeUtil.getMethodDescriptor(new Type[]{TypeUtil.STRING_TYPE}, tempAttrType));
                _visitor.visitFieldInsn(Opcodes.PUTSTATIC, _className, this.getDefaultConstantName(_attrDef), TypeUtil.getFieldDescriptor(tempAttrType));
            } else if (tempValueObj instanceof StaticAttributeValueDef) {
                _visitor.visitLdcInsn(Type.getObjectType(_className));
                StaticAttributeValueDef tempValueDef = (StaticAttributeValueDef) tempValueObj;
                _visitor.visitMethodInsn(Opcodes.INVOKESTATIC, tempValueDef.getClassInternalName(), tempValueDef.getMethodName(), tempValueDef.getMethodDescriptor());
                _visitor.visitFieldInsn(Opcodes.PUTSTATIC, _className, this.getDefaultConstantName(_attrDef), TypeUtil.getFieldDescriptor(tempAttrType));
            } else {
                _visitor.visitTypeInsn(Opcodes.NEW, tempAttrType.getInternalName());
                _visitor.visitInsn(Opcodes.DUP);
                _visitor.visitLdcInsn(tempValueObj);
                _visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, tempAttrType.getInternalName(), "<init>", TypeUtil.getMethodDescriptor(new Class[]{tempValueClass}, Void.TYPE));
                _visitor.visitFieldInsn(Opcodes.PUTSTATIC, _className, this.getDefaultConstantName(_attrDef), TypeUtil.getFieldDescriptor(tempAttrType));
            }
        }

        return _visitor;
    }

    /**
     * Convert to Canonical Name with the given class name.
     *
     * @param _className
     * @return
     */
    protected String toCanonicalName(String _className) {
        return _className.replace('/', '.');
    }

    /**
     * Convert to internal Name with the given class name.
     *
     * @param _className
     * @return
     */
    protected String toInternalName(String _className) {
        return _className.replace('.', '/');
    }

    /**
     * Convert to internal names
     *
     * @param _nameArray
     * @return
     */
    protected List<String> toInternalNames(String[] _nameArray) {
        return Arrays.stream(_nameArray).map(item -> this.toInternalName(item)).collect(Collectors.toList());
    }

    /**
     * Convert to internal names.
     *
     * @param _nameList
     * @return
     */
    protected List<String> toInternalNames(List<String> _nameList) {
        return _nameList.stream().map(item -> this.toInternalName(item)).collect(Collectors.toList());
    }

    /**
     * Get access of the bean.
     *
     * @return
     */
    protected abstract int initAccessFlags();

    /**
     * get default constant name.
     *
     * @param _attrDef
     * @return
     */
    protected String getDefaultConstantName(AttributeDef _attrDef) {
        return _attrDef.getConstantName();
    }
}
