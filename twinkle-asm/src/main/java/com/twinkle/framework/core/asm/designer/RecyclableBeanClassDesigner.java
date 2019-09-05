package com.twinkle.framework.core.asm.designer;

import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.RecyclableBean;
import com.twinkle.framework.core.datastruct.define.*;
import com.twinkle.framework.core.utils.TypeDefUtil;
import com.twinkle.framework.core.utils.TypeUtil;
import org.objectweb.asm.*;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-05 15:18<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class RecyclableBeanClassDesigner extends GeneralBeanClassDesigner {
    protected static final String HASHCODE_METHOD_NAME = "hashCode";
    private static final String HASHCODE_METHOD_SIGNATURE;
    private static final String KEY_VALUE_ELEMENTNAME = "value";

    static {
        HASHCODE_METHOD_SIGNATURE = TypeUtil.getMethodDescriptor(new Type[0], Type.INT_TYPE);
    }

    public RecyclableBeanClassDesigner(String _className, BeanTypeDef _beanTypeDef) {
        super(_className, _beanTypeDef);
    }

    @Override
    protected String[] getDefaultInterfaces() {
        return new String[]{Bean.class.getName(), RecyclableBean.class.getName(), Cloneable.class.getName()};
    }

    @Override
    protected void addFields(ClassVisitor _visitor, List<AttributeDef> _attrDefList) {
        super.addFields(_visitor, _attrDefList);
        _attrDefList.parallelStream().forEach(item -> this.addFlagField(_visitor, item));
    }

    protected FieldVisitor addFlagField(ClassVisitor _visitor, AttributeDef _attrDef) {
        FieldVisitor tempVisitor = _visitor.visitField(Opcodes.ACC_PRIVATE, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE), null, null);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    @Override
    protected void addGetterSetterMethodsDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        super.addGetterSetterMethodsDefinition(_visitor, _className, _attrDefList);

        _attrDefList.parallelStream().forEach(item -> {
            this.addFlagGetterDefinition(_visitor, _className, item);
            this.addFlagSetterDefinition(_visitor, _className, item);
        });

        this.addClearAllDefinition(_visitor, _className, _attrDefList);
        this.addHashCodeDefinition(_visitor, _className);
    }

    @Override
    protected void handleAttributeInit(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        super.handleAttributeInit(_visitor, _className, _attrDef);
        Object tempDefaultValue = _attrDef.getDefaultValue();
        if (tempDefaultValue != null) {
            _visitor.visitVarInsn(Opcodes.ALOAD, 0);
            _visitor.visitInsn(Opcodes.ICONST_1);
            _visitor.visitFieldInsn(Opcodes.PUTFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
        }
    }

    @Override
    protected MethodVisitor addSetterDefinition(ClassVisitor _visitor, String _className, AttributeDef _attrDef) {
        String tempSignature;
        if (_attrDef.getType().isGeneric()) {
            tempSignature = TypeUtil.getGenericSetterSignature(_attrDef.getType());
        } else {
            tempSignature = null;
        }

        MethodVisitor tempVisitor = _visitor.visitMethod(1, TypeUtil.getSetterName(_attrDef), TypeUtil.getSetterSignature(_attrDef.getType().getType()), tempSignature, null);
        _attrDef.getAnnotations().parallelStream().forEach(item -> this.addMethodAnnotation(tempVisitor, item, AnnotationDef.Kind.SETTER));

        tempVisitor.visitCode();
        if (_attrDef.isReadOnly()) {
            tempVisitor.visitTypeInsn(Opcodes.NEW, "java/lang/UnsupportedOperationException");
            tempVisitor.visitInsn(Opcodes.DUP);
            tempVisitor.visitLdcInsn(TypeUtil.getSetterName(_attrDef) + ": readonly attribute [" + _attrDef.getName() + "]");
            tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/UnsupportedOperationException", "<init>", "(Ljava/lang/String;)V");
            tempVisitor.visitInsn(Opcodes.ATHROW);
        } else {
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            tempVisitor.visitVarInsn(TypeUtil.getOpcode(_attrDef.getType().getType(), 21), 1);
            tempVisitor.visitFieldInsn(Opcodes.PUTFIELD, _className, TypeDefUtil.getFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(_attrDef.getType().getType()));
            if (TypeUtil.isPrimitive(_attrDef.getType().getType())) {
                tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
                tempVisitor.visitInsn(Opcodes.ICONST_1);
                tempVisitor.visitFieldInsn(Opcodes.PUTFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
            } else {
                Label tempLabelA = new Label();
                Label tempLabelB = new Label();
                tempVisitor.visitVarInsn(Opcodes.ALOAD, 1);
                tempVisitor.visitJumpInsn(Opcodes.IFNULL, tempLabelA);
                tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
                tempVisitor.visitInsn(Opcodes.ICONST_1);
                tempVisitor.visitFieldInsn(Opcodes.PUTFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
                tempVisitor.visitJumpInsn(Opcodes.GOTO, tempLabelB);
                tempVisitor.visitLabel(tempLabelA);
                tempVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
                tempVisitor.visitInsn(Opcodes.ICONST_0);
                tempVisitor.visitFieldInsn(Opcodes.PUTFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
                tempVisitor.visitLabel(tempLabelB);
                tempVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            }

            tempVisitor.visitInsn(Opcodes.RETURN);
        }

        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add Flag's Getter to this class.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     * @return
     */
    protected MethodVisitor addFlagGetterDefinition(ClassVisitor _visitor, String _className, AttributeDef _attrDef) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, TypeUtil.getFlagGetterName(_attrDef), TypeUtil.getFlagGetterDescriptor(), null, null);
        tempVisitor.visitCode();
        tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        tempVisitor.visitFieldInsn(Opcodes.GETFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
        tempVisitor.visitInsn(TypeUtil.getOpcode(Type.BOOLEAN_TYPE, Opcodes.IRETURN));
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add setter method to this class.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     * @return
     */
    protected MethodVisitor addFlagSetterDefinition(ClassVisitor _visitor, String _className, AttributeDef _attrDef) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, TypeUtil.getFlagSetterName(_attrDef), TypeUtil.getFlagSetterDescriptor(), null, null);
        tempVisitor.visitCode();
        if (_attrDef.isReadOnly()) {
            tempVisitor.visitTypeInsn(Opcodes.NEW, "java/lang/UnsupportedOperationException");
            tempVisitor.visitInsn(Opcodes.DUP);
            tempVisitor.visitLdcInsn(TypeUtil.getFlagSetterName(_attrDef) + ": readonly attribute [" + _attrDef.getName() + "]");
            tempVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/UnsupportedOperationException", "<init>", "(Ljava/lang/String;)V");
            tempVisitor.visitInsn(Opcodes.ATHROW);
        } else {
            Label tempLabelA = new Label();
            Label tempLabelB = new Label();
            tempVisitor.visitVarInsn(Opcodes.ILOAD, 1);
            tempVisitor.visitJumpInsn(Opcodes.IFNE, tempLabelA);
            //Add the attribute define for setter method.
            tempVisitor = this.addMethodAttributeDef(tempVisitor, _className, _attrDef);

            tempVisitor.visitJumpInsn(Opcodes.GOTO, tempLabelB);
            tempVisitor.visitLabel(tempLabelA);
            tempVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            tempVisitor.visitVarInsn(Opcodes.ALOAD, 0);
            tempVisitor.visitInsn(Opcodes.ICONST_1);
            tempVisitor.visitFieldInsn(Opcodes.PUTFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
            tempVisitor.visitLabel(tempLabelB);
            tempVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            tempVisitor.visitInsn(Opcodes.RETURN);
        }

        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add clear() method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected MethodVisitor addClearAllDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "clear", TypeUtil.getMethodDescriptor(new Type[0], Type.VOID_TYPE), null, null);
        tempVisitor.visitCode();

        _attrDefList.stream().filter(item -> !item.isReadOnly()).forEach(item -> {
            this.addMethodAttributeDef(tempVisitor, _className, item);
        });

        tempVisitor.visitInsn(Opcodes.RETURN);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add Method's attribute.
     *
     * @param _visitor
     * @param _className
     * @param _attrDef
     * @return
     */
    private MethodVisitor addMethodAttributeDef(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        Object tempDefaultValueObj = _attrDef.getDefaultValue();
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        if (tempDefaultValueObj != null) {
            _visitor.visitFieldInsn(Opcodes.GETSTATIC, _className, this.getDefaultConstantName(_attrDef), TypeUtil.getFieldDescriptor(_attrDef.getType().getType()));
            if (this.isMutableDefaultInterface(_attrDef)) {
                _visitor.visitMethodInsn(Opcodes.INVOKEINTERFACE, _attrDef.getType().getType().getInternalName(), "clone", TypeUtil.getMethodDescriptor(new Class[0], Object.class));
                _visitor.visitTypeInsn(Opcodes.CHECKCAST, _attrDef.getType().getType().getInternalName());
            } else if (this.isMutableDefault(_attrDef)) {
                _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, _attrDef.getType().getType().getInternalName(), "clone", TypeUtil.getMethodDescriptor(new Class[0], Object.class));
                _visitor.visitTypeInsn(Opcodes.CHECKCAST, _attrDef.getType().getType().getInternalName());
            }
        } else {
            _visitor.visitInsn(TypeUtil.getNullOpcode(_attrDef.getType().getType()));
        }

        _visitor.visitFieldInsn(Opcodes.PUTFIELD, _className, TypeDefUtil.getFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(_attrDef.getType().getType()));
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        if (tempDefaultValueObj != null) {
            _visitor.visitInsn(Opcodes.ICONST_1);
        } else {
            _visitor.visitInsn(Opcodes.ICONST_0);
        }

        _visitor.visitFieldInsn(Opcodes.PUTFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
        return _visitor;
    }

    @Override
    protected void handleAttributeClone(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        if (!_attrDef.isReadOnly()) {
            _visitor.visitVarInsn(Opcodes.ALOAD, 0);
            _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
            Label var4 = new Label();
            _visitor.visitJumpInsn(Opcodes.IFEQ, var4);
            super.handleAttributeClone(_visitor, _className, _attrDef);
            _visitor.visitLabel(var4);
        }

    }

    @Override
    protected void handleAttributeEquals(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
        _visitor.visitVarInsn(Opcodes.ALOAD, 2);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
        Label tempLabelA = new Label();
        _visitor.visitJumpInsn(Opcodes.IF_ICMPEQ, tempLabelA);
        _visitor.visitInsn(Opcodes.ICONST_0);
        _visitor.visitInsn(Opcodes.IRETURN);
        _visitor.visitLabel(tempLabelA);
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
        Label tempLabelB = new Label();
        _visitor.visitJumpInsn(Opcodes.IFEQ, tempLabelB);
        super.handleAttributeEquals(_visitor, _className, _attrDef);
        _visitor.visitLabel(tempLabelB);
    }

    @Override
    protected void addAppendStatement(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
        _visitor.visitVarInsn(Opcodes.ALOAD, 1);
        _visitor.visitLdcInsn(_attrDef.getName());
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER_CLASS_NAME, STRING_BUILDER_APPEND_METHOD, STRING_BUILDER_APPEND_STRING);
        _visitor.visitInsn(Opcodes.POP);
        _visitor.visitVarInsn(Opcodes.ALOAD, 0);
        _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, TypeDefUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
        Label tempLabelA = new Label();
        _visitor.visitJumpInsn(Opcodes.IFNE, tempLabelA);
        _visitor.visitVarInsn(Opcodes.ALOAD, 1);
        _visitor.visitLdcInsn("*");
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, STRING_BUILDER_CLASS_NAME, STRING_BUILDER_APPEND_METHOD, STRING_BUILDER_APPEND_STRING);
        _visitor.visitInsn(Opcodes.POP);
        _visitor.visitLabel(tempLabelA);
        _visitor.visitVarInsn(Opcodes.ALOAD, 1);
        _visitor.visitLdcInsn("=");
        this.addRHSAppendStatement(_visitor, _className, _attrDef);
    }

    /**
     * Add hashCode() method to this class.
     *
     * @param _visitor
     * @param _className
     */
    private void addHashCodeDefinition(ClassVisitor _visitor, String _className) {
        List<AttributeDef> tempAttrDefList = this.getAttributesForHashCode();
        if (!tempAttrDefList.isEmpty()) {
            MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, HASHCODE_METHOD_NAME, HASHCODE_METHOD_SIGNATURE, null, null);
            tempVisitor.visitCode();
            byte tempFIndex = 0;
            byte tempSVarIndex = 1;
            int tempTVarIndex = tempSVarIndex + 1;
            byte tempLocalVarIndex = tempSVarIndex;
            int tempFourthVarIndex = tempTVarIndex++;
            int tempFifthVarIndex = tempTVarIndex++;
            tempVisitor.visitInsn(Opcodes.ICONST_1);
            tempVisitor.visitVarInsn(Opcodes.ISTORE, tempSVarIndex);
            tempVisitor.visitIntInsn(Opcodes.BIPUSH, 31);
            tempVisitor.visitVarInsn(Opcodes.ISTORE, tempFourthVarIndex);
            tempVisitor.visitIntInsn(Opcodes.BIPUSH, 32);
            tempVisitor.visitVarInsn(Opcodes.ISTORE, tempFifthVarIndex);
            Iterator tempItr = tempAttrDefList.iterator();

            while (tempItr.hasNext()) {
                AttributeDef tempAttrDef = (AttributeDef) tempItr.next();
                Label tempLabelA = new Label();
                tempVisitor.visitVarInsn(Opcodes.ALOAD, tempFIndex);
                String tempAttrName = tempAttrDef.getName();
                tempVisitor.visitFieldInsn(Opcodes.GETFIELD, _className, TypeDefUtil.getFlagFieldName(tempAttrName), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
                tempVisitor.visitJumpInsn(Opcodes.IFEQ, tempLabelA);
                tempVisitor.visitVarInsn(Opcodes.ILOAD, tempLocalVarIndex);
                tempVisitor.visitVarInsn(Opcodes.ILOAD, tempFourthVarIndex);
                tempVisitor.visitInsn(Opcodes.IMUL);
                TypeDef tempAttrTypeDef = tempAttrDef.getType();
                Type tempAttrType = tempAttrTypeDef.getType();
                int tempAttrTypeSort = tempAttrType.getSort();
                tempVisitor.visitVarInsn(Opcodes.ALOAD, tempFIndex);
                tempVisitor.visitFieldInsn(Opcodes.GETFIELD, _className, TypeDefUtil.getFieldName(tempAttrName), TypeUtil.getFieldDescriptor(tempAttrType));
                if (!tempAttrTypeDef.isArray() && tempAttrTypeSort != Type.ARRAY) {
                    if (!tempAttrTypeDef.isBean() && !tempAttrTypeDef.isGeneric() && tempAttrTypeSort != 10) {
                        switch (tempAttrTypeSort) {
                            case Type.BOOLEAN:
                                Label tempLabelB = new Label();
                                Label tempLabelC = new Label();
                                tempVisitor.visitJumpInsn(Opcodes.IFEQ, tempLabelB);
                                tempVisitor.visitIntInsn(Opcodes.BIPUSH, 1231);
                                tempVisitor.visitJumpInsn(Opcodes.GOTO, tempLabelC);
                                tempVisitor.visitLabel(tempLabelB);
                                tempVisitor.visitIntInsn(Opcodes.BIPUSH, 1237);
                                tempVisitor.visitLabel(tempLabelC);
                            case Type.CHAR:
                            case Type.BYTE:
                            case Type.SHORT:
                            case Type.INT:
                            default:
                                break;
                            case Type.FLOAT:
                                tempVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, TypeUtil.FLOAT_OBJECT_TYPE.getInternalName(), "floatToIntBits", TypeUtil.getMethodDescriptor(new Type[]{Type.FLOAT_TYPE}, Type.INT_TYPE));
                                break;
                            case Type.DOUBLE:
                                tempVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, TypeUtil.DOUBLE_OBJECT_TYPE.getInternalName(), "doubleToLongBits", TypeUtil.getMethodDescriptor(new Type[]{Type.DOUBLE_TYPE}, Type.LONG_TYPE));
                            case Type.LONG:
                                tempVisitor.visitInsn(Opcodes.DUP2);
                                tempVisitor.visitVarInsn(Opcodes.ILOAD, tempFifthVarIndex);
                                tempVisitor.visitInsn(Opcodes.LUSHR);
                                tempVisitor.visitInsn(Opcodes.LXOR);
                                tempVisitor.visitInsn(Opcodes.L2I);
                        }
                    } else {
                        Label tempLabelB = new Label();
                        Label tempLabelC = new Label();
                        tempVisitor.visitInsn(Opcodes.DUP);
                        tempVisitor.visitJumpInsn(Opcodes.IFNONNULL, tempLabelB);
                        tempVisitor.visitInsn(Opcodes.POP);
                        tempVisitor.visitInsn(Opcodes.ICONST_0);
                        tempVisitor.visitJumpInsn(Opcodes.GOTO, tempLabelC);
                        tempVisitor.visitLabel(tempLabelB);
                        tempVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, tempAttrType.getInternalName(), HASHCODE_METHOD_NAME, HASHCODE_METHOD_SIGNATURE);
                        tempVisitor.visitLabel(tempLabelC);
                    }
                } else {
                    TypeDef tempElementType = ((ArrayTypeDef) tempAttrTypeDef).getElementType();
                    if (tempElementType.isPrimitive()) {
                        tempVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, ARRAYS_CLASS_NAME, HASHCODE_METHOD_NAME, TypeUtil.getMethodDescriptor(new Type[]{tempAttrType}, Type.INT_TYPE));
                    } else {
                        tempVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, ARRAYS_CLASS_NAME, HASHCODE_METHOD_NAME, TypeUtil.getMethodDescriptor(new Type[]{Type.getType(Object[].class)}, Type.INT_TYPE));
                    }
                }

                tempVisitor.visitInsn(Opcodes.IADD);
                tempVisitor.visitVarInsn(Opcodes.ISTORE, tempLocalVarIndex);
                tempVisitor.visitLabel(tempLabelA);
            }

            tempVisitor.visitVarInsn(Opcodes.ILOAD, tempLocalVarIndex);
            tempVisitor.visitInsn(Opcodes.IRETURN);
            tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
            tempVisitor.visitEnd();
        }
    }

    /**
     * Get the attribute list will be used to generate hashCode in hashCode() method.
     *
     * If the Bean has the @Key annotation, then all of the attributes will be
     * considered as hashcode attribute, except some attribute annotated with Key and value = false.
     *
     * If the Bean does not have @key annotation, will scan the Attributes who have the @Key annotation and value = true.
     * @return
     */
    private List<AttributeDef> getAttributesForHashCode() {
        AnnotationDef tempAnnotationDef = this.getKeyAnnotation(this.getBeanTypeDef().getAnnotations());
        if (tempAnnotationDef != null && this.isKeyAnnotEnabled(tempAnnotationDef)) {
            return this.getBeanTypeDef().getAttributes().parallelStream().filter(item -> {
                AnnotationDef tempItemDef = this.getKeyAnnotation(item.getAnnotations());
                if (tempItemDef != null && !this.isKeyAnnotEnabled(tempItemDef)) {
                    return false;
                }
                return true;
            }).collect(Collectors.toList());
        } else {
            return this.getBeanTypeDef().getAttributes().parallelStream().filter(item -> {
                AnnotationDef tempItemDef = this.getKeyAnnotation(item.getAnnotations());
                if (tempItemDef != null && this.isKeyAnnotEnabled(tempItemDef)) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
        }
    }

    /**
     * To get the @Key annotation from the given annotationList.
     *
     * @param _annotationDefList
     * @return
     */
    private AnnotationDef getKeyAnnotation(List<AnnotationDef> _annotationDefList) {
        for (AnnotationDef tempItem : _annotationDefList) {
            if (!tempItem.getType().equals(TypeUtil.KEY_ANNOTATION_TYPE)) {
                continue;
            }
            return tempItem;
        }
        return null;
    }

    /**
     * Judge the annotation is @Key and value=enabled.
     *
     * @param _annotationDef
     * @return
     */
    private boolean isKeyAnnotEnabled(AnnotationDef _annotationDef) {
        for (AnnotationElementDef tempAnnotationElement : _annotationDef.getElements()) {
            if (tempAnnotationElement.getName().equals(KEY_VALUE_ELEMENTNAME) &&
                    tempAnnotationElement.getValue().equals(Boolean.FALSE)) {
                return true;
            }
        }
        return false;
    }
}
