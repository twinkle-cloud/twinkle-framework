package com.twinkle.framework.struct.asm.designer;

import com.twinkle.framework.asm.Bean;
import com.twinkle.framework.asm.RecyclableBean;
import com.twinkle.framework.asm.SimpleReflectiveBean;
import com.twinkle.framework.asm.converter.AttributeConverter;
import com.twinkle.framework.asm.converter.LooseAttributeConverter;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.asm.define.AttributeDef;
import com.twinkle.framework.asm.define.BeanTypeDef;
import com.twinkle.framework.asm.designer.ClassInitializer;
import com.twinkle.framework.asm.designer.SimpleReflectiveBeanClassDesigner;
import com.twinkle.framework.asm.utils.ClassDesignerUtil;
import com.twinkle.framework.struct.asm.define.StructAttributeBeanTypeDef;
import com.twinkle.framework.struct.error.AttributeNotFoundException;
import com.twinkle.framework.struct.error.StructAttributeCopyException;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.ref.SAAttributeRefHandleImpl;
import com.twinkle.framework.struct.ref.StructAttributeRef;
import com.twinkle.framework.struct.type.ArrayType;
import com.twinkle.framework.struct.type.StructAttribute;
import com.twinkle.framework.struct.type.StructAttributeType;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.asm.utils.TypeUtil;
import com.twinkle.framework.struct.utils.StructTypeUtil;
import org.objectweb.asm.*;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Function: This class designer is used to build the StructAttribute.
 *  the built struct attribute always extends @AbstractStructAttribute.<br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/1/19 7:35 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public final class StructAttributeClassDesigner extends SimpleReflectiveBeanClassDesigner implements ClassInitializer {
    private static final MethodHandles.Lookup _methodLookup = MethodHandles.lookup();
    public static final String GET_ATTR_REF_METHOD_NAME = "getAttributeReference";
    public static final String GET_ATTR_REF_METHOD_SIGNATURE = TypeUtil.getMethodDescriptor(new Class[]{String.class}, AttributeRef.class);
    protected static final String FIELD_REF_SUFFIX = "Ref";
    public static final String INIT_METHOD_NAME = "_init";
    public static final String INIT_METHOD_SIGNATURE;
    protected static final Type STRUCTATTRIBUTE_TYPE;
    protected static final Type STRUCTATTRIBUTETYPE_TYPE;
    protected static final Type ATTR_REF_TYPE;
    protected static final Type ATTR_REF_IMPL_TYPE;
    protected static final Type ATTR_REF_IMPL;
    protected static final Set<String> RESERVED_NAMES;
    private final String superClassName;
    private final StructAttributeType structAttributeType;

    static {
        INIT_METHOD_SIGNATURE = TypeUtil.getMethodDescriptor(new Class[]{StructAttributeType.class}, Void.TYPE);
        STRUCTATTRIBUTE_TYPE = Type.getType(StructAttribute.class);
        STRUCTATTRIBUTETYPE_TYPE = Type.getType(StructAttributeType.class);
        ATTR_REF_TYPE = Type.getType(AttributeRef.class);
        ATTR_REF_IMPL_TYPE = Type.getType(StructAttributeRef.class);
        ATTR_REF_IMPL = Type.getType(SAAttributeRefHandleImpl.class);
        RESERVED_NAMES = new HashSet(Arrays.asList("getClass", "getType"));
    }

    public StructAttributeClassDesigner(String _className, String _superClassName, StructAttributeBeanTypeDef _typeDef) {
        super(_className, _typeDef);
        this.superClassName = this.toInternalName(_superClassName);
        this.structAttributeType = _typeDef.getStructAttributeType();
    }

    protected StructAttributeType getStructAttributeType() {
        return this.structAttributeType;
    }

    /**
     * Get the struct attribute descriptor with given attribute name.
     *
     * @param _attrName
     * @return
     */
    protected SAAttributeDescriptor getSAAttribute(String _attrName) {
        return this.getStructAttributeType().getAttribute(_attrName);
    }

    @Override
    protected AttributeConverter initAttributeConverter(String _typeName) {
        return new LooseAttributeConverter(_typeName, RESERVED_NAMES);
    }

    @Override
    public void initClass(Class _class) {
        Method tempMethod = null;
        try {
            tempMethod = _class.getDeclaredMethod(INIT_METHOD_NAME, StructAttributeType.class);
            tempMethod.setAccessible(true);
            MethodHandle tempMethodHandle = _methodLookup.unreflect(tempMethod);
            tempMethodHandle.invokeExact(this.structAttributeType);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new RuntimeException(throwable);
            }
        } finally {
            tempMethod.setAccessible(false);
        }
    }

    @Override
    protected String[] getDefaultInterfaces() {
        return new String[]{Bean.class.getName(), RecyclableBean.class.getName(), SimpleReflectiveBean.class.getName(), StructAttribute.class.getName(), Cloneable.class.getName()};
    }

    @Override
    protected ClassVisitor addClassDeclaration(ClassVisitor _visitor, String _className, List<String> _interfaceList, BeanTypeDef _beanTypeDef) {
        return this.addClassDeclaration(_visitor, _className, this.superClassName, _interfaceList, _beanTypeDef);
    }

    @Override
    protected void addClassDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _interfaceAttrDefList, BeanTypeDef _beanTypeDef) {
        this.addClassDefinition(_visitor, _className, this.superClassName, _interfaceAttrDefList, _beanTypeDef);
    }

    @Override
    protected void addFields(ClassVisitor _visitor, List<AttributeDef> _attrDefList) {
        super.addFields(_visitor, _attrDefList);
        this.addStructAttributeTypeField(_visitor);
        this.addFieldRefs(_visitor, _attrDefList);
    }

    protected FieldVisitor addStructAttributeTypeField(ClassVisitor _visitor) {
        FieldVisitor tempVisitor = _visitor.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC, getStructAttributeTypeFieldName(), STRUCTATTRIBUTETYPE_TYPE.getDescriptor(), null, null);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add the attribute's refs.
     *
     * @param _visitor
     * @param _attrDefList
     */
    protected void addFieldRefs(ClassVisitor _visitor, List<AttributeDef> _attrDefList) {
        _attrDefList.stream().forEach(item -> this.addFieldRef(_visitor, item));
    }

    /**
     * Add some ref filed.
     *
     * @param _visitor
     * @param _attrDef
     * @return
     */
    protected FieldVisitor addFieldRef(ClassVisitor _visitor, AttributeDef _attrDef) {
        FieldVisitor tempVisitor = _visitor.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC, getFieldRefName(_attrDef.getName()), getFieldRefType().getDescriptor(), null, null);
        tempVisitor.visitEnd();
        return tempVisitor;
    }
    @Override
    protected void addGetterSetterMethodsDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        super.addGetterSetterMethodsDefinition(_visitor, _className, _attrDefList);
        List<AttributeDef> tempSortedAttrDefList = this.sort(_attrDefList);
        this.addClassInit(_visitor, _className, _attrDefList);
        this.addAttributeReferenceGetterDefinition(_visitor, _className, tempSortedAttrDefList);
        this.addContainsAttributeDefinition(_visitor, _className, tempSortedAttrDefList);
        this.addStructAttributeTypeGetterDefinition(_visitor, _className);
        this.addInternalAttributeRefGetterDefinition(_visitor, _className);
        this.addHasAttributeDefinition(_visitor, _className);
        this.addCopyDefinition(_visitor, _className, _attrDefList);
    }

    /**
     * Add class's init method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected MethodVisitor addClassInit(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC, INIT_METHOD_NAME, INIT_METHOD_SIGNATURE, null, null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitFieldInsn(Opcodes.PUTSTATIC, _className, getStructAttributeTypeFieldName(), STRUCTATTRIBUTETYPE_TYPE.getDescriptor());
        for(AttributeDef tempDef : _attrDefList) {
            String tempAttrName = tempDef.getName();
            Type tempObjType = Type.getObjectType(_className);
            tempVisitor.visitTypeInsn(Opcodes.NEW, getFieldRefImpl().getInternalName());
            tempVisitor.visitInsn(Opcodes.DUP);
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            tempVisitor.visitLdcInsn(tempAttrName);
            tempVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, STRUCTATTRIBUTETYPE_TYPE.getInternalName(), "getAttribute", TypeUtil.getMethodDescriptor(new Class[]{String.class}, SAAttributeDescriptor.class));
            tempVisitor.visitLdcInsn(tempObjType);
            tempVisitor.visitLdcInsn(TypeUtil.getGetterName(tempDef));
            tempVisitor.visitLdcInsn(TypeUtil.getFlagGetterName(tempDef));
            tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, getFieldRefImpl().getInternalName(), "<init>", TypeUtil.getMethodDescriptor(new Class[]{SAAttributeDescriptor.class, Class.class, String.class, String.class}, Void.TYPE));
            tempVisitor.visitFieldInsn(Opcodes.PUTSTATIC, _className, getFieldRefName(tempAttrName), getFieldRefType().getDescriptor());
        }

        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    @Override
    protected boolean isMutableDefaultInterface(AttributeDef _attrDef) {
        SAAttributeDescriptor tempDescriptor = this.getSAAttribute(_attrDef.getName());
        StructType tempStructType = tempDescriptor.getType();
        return tempStructType.isArrayType();
    }

    @Override
    protected MethodVisitor addObjectClassConstructorDefinition(MethodVisitor _visitor, String _className, AttributeDef _attrDef, int _parameterSize) {
        Object tempDefaultValue = _attrDef.getDefaultValue();
        if (tempDefaultValue != null) {
            SAAttributeDescriptor tempAttrDescriptor = this.getSAAttribute(_attrDef.getName());
            StructType tempAttrStructType = tempAttrDescriptor.getType();
            Type tempAttrType = _attrDef.getType().getType();
            Type tempMappedJavaType = StructTypeUtil.mapStructAttributeType(_attrDef.getType().getType());
            boolean isArrayFlag = tempMappedJavaType.getSort() == Type.ARRAY;
            if (tempAttrStructType.isArrayType() && isArrayFlag) {
                StructType tempElementType = ((ArrayType) tempAttrStructType).getElementType();
                Type tempMappedElementJavaType = tempMappedJavaType.getElementType();
                int tempDefaultValueLength = Array.getLength(tempDefaultValue);
                _visitor.visitLdcInsn(tempDefaultValueLength);
                if (TypeUtil.isPrimitive(tempMappedElementJavaType)) {
                    _visitor.visitIntInsn(Opcodes.NEWARRAY, TypeUtil.getNewArrayOpcode(tempMappedElementJavaType));
                } else {
                    _visitor.visitTypeInsn(Opcodes.ANEWARRAY, tempMappedElementJavaType.getInternalName());
                }
                // Set the default value for each element.
                for (int i = 0; i < tempDefaultValueLength; ++i) {
                    _visitor.visitInsn(Opcodes.DUP);
                    _visitor.visitLdcInsn(i);
                    Object tempItemValueObj = Array.get(tempDefaultValue, i);
                    if (tempItemValueObj != null) {
                        if (!TypeUtil.isPrimitive(tempMappedElementJavaType) && !tempMappedElementJavaType.equals(Type.getType(String.class))) {
                            _visitor.visitTypeInsn(Opcodes.NEW, tempMappedElementJavaType.getInternalName());
                            _visitor.visitInsn(Opcodes.DUP);
                            _visitor.visitLdcInsn(tempItemValueObj);
                            _visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, tempMappedElementJavaType.getInternalName(), "<init>", TypeUtil.getMethodDescriptor(new Class[]{tempItemValueObj.getClass()}, Void.TYPE));
                        } else {
                            _visitor.visitLdcInsn(tempItemValueObj);
                        }
                    } else {
                        _visitor.visitInsn(TypeUtil.getNullOpcode(tempMappedElementJavaType));
                    }

                    _visitor.visitInsn(TypeUtil.getOpcode(tempMappedElementJavaType, Opcodes.IASTORE));
                }

                _visitor.visitVarInsn(Opcodes.ASTORE, _parameterSize);
                _visitor.visitVarInsn(Opcodes.ALOAD, _parameterSize);
                //Refer to AbstractStructAttribute._byteArray(), _shortArray
                _visitor.visitMethodInsn(Opcodes.INVOKESTATIC, this.superClassName, "_" + tempElementType.getName() + "Array", TypeUtil.getMethodDescriptor(new Type[]{tempMappedJavaType}, tempAttrType));
                _visitor.visitFieldInsn(Opcodes.PUTSTATIC, _className, this.getDefaultConstantName(_attrDef), TypeUtil.getFieldDescriptor(_attrDef.getType().getType()));
            } else {
                super.addObjectClassConstructorDefinition(_visitor, _className, _attrDef, _parameterSize);
            }
        }

        return _visitor;
    }

    protected MethodVisitor addAttributeReferenceGetterDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, GET_ATTR_REF_METHOD_NAME, GET_ATTR_REF_METHOD_SIGNATURE, null, new String[]{Type.getType(AttributeNotFoundException.class).getInternalName()});
        tempVisitor.visitCode();
        this.addSwitchCaseStatement(tempVisitor, _className, _attrDefList, new CaseAppender() {
            @Override
            public void addCase(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
                String tempAttrName = _attrDef.getName();
                _visitor.visitFieldInsn(Opcodes.GETSTATIC, _className, StructAttributeClassDesigner.getFieldRefName(tempAttrName), getFieldRefType().getDescriptor());
                _visitor.visitInsn(Opcodes.ARETURN);
            }
        }, new DefaultCaseAppender() {
            @Override
            public void addDefaultCase(MethodVisitor _visitor, String _className) {
                Type tempExceptionType = Type.getType(AttributeNotFoundException.class);
                _visitor.visitTypeInsn(Opcodes.NEW, tempExceptionType.getInternalName());
                _visitor.visitInsn(Opcodes.DUP);
                _visitor.visitVarInsn(Opcodes.ALOAD, 0);
                _visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, tempExceptionType.getInternalName(), "<init>", TypeUtil.getMethodDescriptor(new Class[]{String.class}, Void.TYPE));
                _visitor.visitInsn(Opcodes.ATHROW);
            }
        }, 0, 1, false, SWITCH_SEGMENT_SIZE);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add public boolean containsAttribute(String _attrName) method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected MethodVisitor addContainsAttributeDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "containsAttribute", TypeUtil.getMethodDescriptor(new Class[]{String.class}, Boolean.TYPE), null, null);
        tempVisitor.visitCode();
        this.addSwitchCaseStatement(tempVisitor, _className, _attrDefList, new CaseAppender() {
            @Override
            public void addCase(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
                _visitor.visitInsn(4);
                _visitor.visitInsn(Opcodes.IRETURN);
            }
        }, new DefaultCaseAppender() {
            @Override
            public void addDefaultCase(MethodVisitor _visitor, String _className) {
                _visitor.visitInsn(3);
                _visitor.visitInsn(Opcodes.IRETURN);
            }
        }, 0, 1, false, 20);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add struct attribute getter method.
     *
     * @param _visitor
     * @param _className
     * @return
     */
    protected MethodVisitor addStructAttributeTypeGetterDefinition(ClassVisitor _visitor, String _className) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "getType", TypeUtil.getMethodDescriptor(new Type[0], STRUCTATTRIBUTETYPE_TYPE), null, null);
        tempVisitor.visitCode();
        tempVisitor.visitFieldInsn(Opcodes.GETSTATIC, _className, getStructAttributeTypeFieldName(), TypeUtil.getFieldDescriptor(STRUCTATTRIBUTETYPE_TYPE));
        tempVisitor.visitInsn(Opcodes.ARETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Build the protected StructAttributeRef _getAttributeRef() method.
     *
     * @param _visitor
     * @param _className
     * @return
     */
    protected MethodVisitor addInternalAttributeRefGetterDefinition(ClassVisitor _visitor, String _className) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PROTECTED, "_getAttributeRef", TypeUtil.getMethodDescriptor(new Class[]{String.class}, StructAttributeRef.class), null, new String[]{Type.getType(AttributeNotFoundException.class).getInternalName()});
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        tempVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, _className, GET_ATTR_REF_METHOD_NAME, GET_ATTR_REF_METHOD_SIGNATURE);
        tempVisitor.visitTypeInsn(Opcodes.CHECKCAST, getFieldRefType().getInternalName());
        tempVisitor.visitInsn(Opcodes.ARETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Build public boolean hasAttribute(String _attrName) method.
     *
     * @param _visitor
     * @param _className
     * @return
     */
    protected MethodVisitor addHasAttributeDefinition(ClassVisitor _visitor, String _className) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "hasAttribute", TypeUtil.getMethodDescriptor(new Type[]{TypeUtil.STRING_TYPE}, Type.BOOLEAN_TYPE), null, null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        tempVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, _className, "containsAttribute", TypeUtil.getMethodDescriptor(new Class[]{String.class}, Boolean.TYPE));
        tempVisitor.visitInsn(Opcodes.IRETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    @Override
    protected void handleCloneableClone(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        Label tempLabelA = new Label();
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, ClassDesignerUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
        _visitor.visitJumpInsn(Opcodes.IFEQ, tempLabelA);
        _visitor.visitFieldInsn(Opcodes.GETSTATIC, _className, getFieldRefName(_attrDef.getName()), getFieldRefType().getDescriptor());
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitVarInsn(Opcodes.ALOAD, 1);
        _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, getFieldRefType().getInternalName(), "copy", TypeUtil.getMethodDescriptor(new Class[]{StructAttribute.class, StructAttribute.class}, Void.TYPE));
        _visitor.visitLabel(tempLabelA);
    }

    @Override
    protected void handleNonCloneableClone(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        Label tempLabelA = new Label();
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, ClassDesignerUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
        _visitor.visitJumpInsn(Opcodes.IFEQ, tempLabelA);
        _visitor.visitFieldInsn(Opcodes.GETSTATIC, _className, getFieldRefName(_attrDef.getName()), getFieldRefType().getDescriptor());
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitVarInsn(Opcodes.ALOAD, 1);
        _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, getFieldRefType().getInternalName(), "copy", TypeUtil.getMethodDescriptor(new Class[]{StructAttribute.class, StructAttribute.class}, Void.TYPE));
        _visitor.visitLabel(tempLabelA);
    }

    protected MethodVisitor addCopyDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrList) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "copy", TypeUtil.getMethodDescriptor(new Class[]{StructAttribute.class}, Void.TYPE), null, new String[]{Type.getType(StructAttributeCopyException.class).getInternalName()});
        tempVisitor.visitCode();
        Label tempLabelA = new Label();
        Label tempLabelB = new Label();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
        tempVisitor.visitTypeInsn(Opcodes.INSTANCEOF, _className);
        tempVisitor.visitJumpInsn(Opcodes.IFEQ, tempLabelA);

        for(AttributeDef tempItem : _attrList) {
            Label tempLabelC = new Label();
            Label tempLabelD = new Label();
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            tempVisitor.visitFieldInsn(Opcodes.GETFIELD, _className, ClassDesignerUtil.getFlagFieldName(tempItem.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
            tempVisitor.visitJumpInsn(Opcodes.IFEQ, tempLabelC);
            tempVisitor.visitFieldInsn(Opcodes.GETSTATIC, _className, getFieldRefName(tempItem.getName()), getFieldRefType().getDescriptor());
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
            tempVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, getFieldRefType().getInternalName(), "copy", TypeUtil.getMethodDescriptor(new Class[]{StructAttribute.class, StructAttribute.class}, Void.TYPE));
            tempVisitor.visitJumpInsn(Opcodes.GOTO, tempLabelD);
            tempVisitor.visitLabel(tempLabelC);
            tempVisitor.visitFieldInsn(Opcodes.GETSTATIC, _className, getFieldRefName(tempItem.getName()), getFieldRefType().getDescriptor());
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
            tempVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, getFieldRefType().getInternalName(), "clear", TypeUtil.getMethodDescriptor(new Class[]{StructAttribute.class}, Void.TYPE));
            tempVisitor.visitLabel(tempLabelD);
        }
        tempVisitor.visitJumpInsn(Opcodes.GOTO, tempLabelB);
        tempVisitor.visitLabel(tempLabelA);

        for(AttributeDef tempItem : _attrList) {
            Type tempItemType = tempItem.getType().getType();
            SAAttributeDescriptor tempAttrDescriptor = this.getSAAttribute(tempItem.getName());
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
            tempVisitor.visitLdcInsn(tempItem.getName());
            tempVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, STRUCTATTRIBUTE_TYPE.getInternalName(), "getAttributeRef", GET_ATTR_REF_METHOD_SIGNATURE);
            tempVisitor.visitVarInsn(Opcodes.ASTORE, 2);
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            tempVisitor.visitFieldInsn(Opcodes.GETFIELD, _className, ClassDesignerUtil.getFlagFieldName(tempItem.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
            Label tempLabelE = new Label();
            Label tempLabelF = new Label();
            tempVisitor.visitJumpInsn(Opcodes.IFEQ, tempLabelE);
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 2);
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            tempVisitor.visitFieldInsn(Opcodes.GETFIELD, _className, ClassDesignerUtil.getFieldName(tempItem.getName()), TypeUtil.getFieldDescriptor(tempItemType));
            tempVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, STRUCTATTRIBUTE_TYPE.getInternalName(), this.getSAAttributeSetterName(tempAttrDescriptor.getType()), TypeUtil.getMethodDescriptor(new Type[]{ATTR_REF_TYPE, tempItemType}, Type.VOID_TYPE));
            tempVisitor.visitJumpInsn(Opcodes.GOTO, tempLabelF);
            tempVisitor.visitLabel(tempLabelE);
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 2);
            tempVisitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, STRUCTATTRIBUTE_TYPE.getInternalName(), "clear", TypeUtil.getMethodDescriptor(new Type[]{ATTR_REF_TYPE}, Type.VOID_TYPE));
            tempVisitor.visitLabel(tempLabelF);
        }

        tempVisitor.visitLabel(tempLabelB);
        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    protected static String getFieldRefName(String _fieldName) {
        return _fieldName + FIELD_REF_SUFFIX;
    }

    protected static Type getFieldRefType() {
        return ATTR_REF_IMPL_TYPE;
    }

    protected static Type getFieldRefImpl() {
        return ATTR_REF_IMPL;
    }

    protected static String getStructAttributeTypeFieldName() {
        return "structAttributeType";
    }

    protected String getSAAttributeGetterName(StructType _type) {
        return "get" + getStructAttributeTypeName(_type);
    }

    protected String getSAAttributeSetterName(StructType _type) {
        return "set" + getStructAttributeTypeName(_type);
    }

    /**
     * Get Struct attribute type name.
     *
     * @param _type
     * @return
     */
    protected static String getStructAttributeTypeName(StructType _type) {
        if (_type.isStructType()) {
            return "Struct";
        } else if (_type.isArrayType()) {
            return "Array";
        } else if (_type.isStringType()) {
            return "String";
        } else if (_type.isPrimitiveType()) {
            String tempTypeName = _type.getName();
            return tempTypeName.substring(0, 1).toUpperCase() + tempTypeName.substring(1);
        }
        throw new IllegalArgumentException(_type.getName() + "not supported");
    }
}
