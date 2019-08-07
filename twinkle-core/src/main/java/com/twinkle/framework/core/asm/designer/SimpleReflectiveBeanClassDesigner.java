package com.twinkle.framework.core.asm.designer;

import com.twinkle.framework.core.datastruct.Bean;
import com.twinkle.framework.core.datastruct.RecyclableBean;
import com.twinkle.framework.core.datastruct.SimpleReflectiveBean;
import com.twinkle.framework.core.datastruct.schema.AttributeDef;
import com.twinkle.framework.core.datastruct.schema.BeanTypeDef;
import com.twinkle.framework.core.utils.AttributeUtil;
import com.twinkle.framework.core.utils.TypeUtil;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-05 22:41<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class SimpleReflectiveBeanClassDesigner extends AbstractReflectiveBeanClassDesigner {
    public SimpleReflectiveBeanClassDesigner(String _className, BeanTypeDef _typeDef) {
        super(_className, _typeDef);
    }
    @Override
    protected String[] getDefaultInterfaces() {
        return new String[]{Bean.class.getName(), RecyclableBean.class.getName(), SimpleReflectiveBean.class.getName(), Cloneable.class.getName()};
    }
    @Override
    protected void addMethodsDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        super.addMethodsDefinition(_visitor, _className, _attrDefList);
        List<AttributeDef> tempSortedAttrDefList = this.sort(_attrDefList);
        this.addAttributeGettersSetters(_visitor, _className, tempSortedAttrDefList);
        this.addAttributeGetFlagDefinition(_visitor, _className, tempSortedAttrDefList);
        this.addAttributeClearFlagDefinition(_visitor, _className, tempSortedAttrDefList);
    }

    /**
     * Add universal setter and getter method for attributes,
     * The attributes who have the same Attribute type will be
     * classified into the same group, and will be added into one
     * setter or getter method with the switch case instruction.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     */
    protected void addAttributeGettersSetters(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        List<AttributeDef> tempAttrDefList = this.filterExactMatch(_attrDefList, Type.BOOLEAN_TYPE);
        this.addAttributeGetterDefinition(_visitor, Boolean.TYPE, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, Boolean.TYPE, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.CHAR_TYPE);
        this.addAttributeGetterDefinition(_visitor, Character.TYPE, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, Character.TYPE, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.BYTE_TYPE);
        this.addAttributeGetterDefinition(_visitor, Byte.TYPE, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, Byte.TYPE, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.SHORT_TYPE);
        this.addAttributeGetterDefinition(_visitor, Short.TYPE, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, Short.TYPE, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.INT_TYPE);
        this.addAttributeGetterDefinition(_visitor, Integer.TYPE, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, Integer.TYPE, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.LONG_TYPE);
        this.addAttributeGetterDefinition(_visitor, Long.TYPE, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, Long.TYPE, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.FLOAT_TYPE);
        this.addAttributeGetterDefinition(_visitor, Float.TYPE, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, Float.TYPE, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, Type.DOUBLE_TYPE);
        this.addAttributeGetterDefinition(_visitor, Double.TYPE, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, Double.TYPE, _className, tempAttrDefList);
        tempAttrDefList = this.filterExactMatch(_attrDefList, TypeUtil.STRING_TYPE);
        this.addAttributeGetterDefinition(_visitor, String.class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, String.class, _className, tempAttrDefList);
        tempAttrDefList = this.filterObjectExcludes(_attrDefList, Arrays.asList(TypeUtil.STRING_TYPE));
        this.addAttributeGetterDefinition(_visitor, Object.class, _className, tempAttrDefList);
        this.addAttributeSetterDefinition(_visitor, Object.class, _className, tempAttrDefList, true);
    }

    /**
     * Add universal getter method by batch.
     *
     * @param _visitor
     * @param _fieldClass
     * @param _className
     * @param _attrDefList
     * @return
     */
    public MethodVisitor addAttributeGetterDefinition(ClassVisitor _visitor, Class _fieldClass, String _className, List<AttributeDef> _attrDefList) {
        return this.addAttributeGetterDefinition(_visitor, _fieldClass, _className, _attrDefList, false);
    }

    /**
     * Add public XXX getXXXValue(String _attrName){
     *     int tempIndex = _attrName.hashCode();
     *     swith(tempIndex):
     *        case xx:
     *        case xx:
     *
     * }
     *
     * @param _visitor
     * @param _fieldClass
     * @param _className
     * @param _attrDefList
     * @param var5
     * @return
     */
    public MethodVisitor addAttributeGetterDefinition(ClassVisitor _visitor, Class _fieldClass, String _className, List<AttributeDef> _attrDefList, boolean var5) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, this.getAttributeGetterName(_fieldClass), TypeUtil.getMethodDescriptor(new Class[]{String.class}, _fieldClass), null, null);
        tempVisitor.visitCode();
        this.addSwitchCaseGetValueStatement(tempVisitor, _fieldClass, _className, _attrDefList, var5);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add universal setter method by batch.
     *
     * @param _visitor
     * @param _fieldClass
     * @param _className
     * @param _attrDefList
     * @return
     */
    public MethodVisitor addAttributeSetterDefinition(ClassVisitor _visitor, Class _fieldClass, String _className, List<AttributeDef> _attrDefList) {
        return this.addAttributeSetterDefinition(_visitor, _fieldClass, _className, _attrDefList, false);
    }

    /**
     * Add universal setter method by batch.
     *   public void setXXXValue(String _attrName, XXX _value){
     *      int tempIndex = _attrName.hashCode();
     *      swith(tempIndex):
     *          case xx:
     *          case xx:
     *
     *     }
     * @param _visitor
     * @param _fieldClass
     * @param _className
     * @param _attrDefList
     * @param var5
     * @return
     */
    public MethodVisitor addAttributeSetterDefinition(ClassVisitor _visitor, Class _fieldClass, String _className, List<AttributeDef> _attrDefList, boolean var5) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, this.getAttributeSetterName(_fieldClass), TypeUtil.getMethodDescriptor(new Class[]{String.class, _fieldClass}, Void.TYPE), null, null);
        tempVisitor.visitCode();
        this.addSwitchCaseSetValueStatement(tempVisitor, _fieldClass, _className, _attrDefList, var5);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add public boolean isAttributeSet(String _attrName){}
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected MethodVisitor addAttributeGetFlagDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "isAttributeSet", TypeUtil.getMethodDescriptor(new Class[]{String.class}, Boolean.TYPE), null, null);
        tempVisitor.visitCode();
        this.addSwitchCaseGetFlagStatement(tempVisitor, _className, _attrDefList);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Add public clean(String _attrName) method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected MethodVisitor addAttributeClearFlagDefinition(ClassVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        MethodVisitor tempVisitor = _visitor.visitMethod(Opcodes.ACC_PUBLIC, "clear", TypeUtil.getMethodDescriptor(new Type[]{TypeUtil.STRING_TYPE}, Type.VOID_TYPE), null, null);
        tempVisitor.visitCode();
        this.addSwitchCaseClearFlagStatement(tempVisitor, _className, _attrDefList);
        tempVisitor.visitMaxs(AUTO_STACK_SIZE, AUTO_LOCAL_VARS);
        tempVisitor.visitEnd();
        return tempVisitor;
    }

    /**
     * Get the attribute getter name.
     * such as:
     * getDoubleAttribute
     * getDoubleArrayAttribute
     *
     * @param _class
     * @return
     */
    protected String getAttributeGetterName(Class _class) {
        String tempSimpleName = _class.getSimpleName();
        return "get" + tempSimpleName.substring(0, 1).toUpperCase() + tempSimpleName.substring(1).replace("[]", "Array") + "Attribute";
    }

    /**
     * Get the attribute setter name.
     * such as:
     * setDoubleAttribute
     * setDoubleArrayAttribute
     *
     * @param _class
     * @return
     */
    protected String getAttributeSetterName(Class _class) {
        String tempSimpleName = _class.getSimpleName();
        return "set" + tempSimpleName.substring(0, 1).toUpperCase() + tempSimpleName.substring(1).replace("[]", "Array") + "Attribute";
    }

    /**
     * Add switch - case segment into the attribute universal getter method.
     *
     * @param _visitor
     * @param _fieldClass
     * @param _className
     * @param _attrDefList
     * @param _needJumpFlag
     * @return
     */
    protected int addSwitchCaseGetValueStatement(MethodVisitor _visitor, Class _fieldClass, String _className, List<AttributeDef> _attrDefList, boolean _needJumpFlag) {
        byte tempLoadRefObjIndex = 1;
        byte tempStoreIntIndex = 2;
        return this.addSwitchCaseStatement(_visitor, _className, _attrDefList, new CaseAppender() {
            @Override
            public void addCase(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
                Type tempAttrType = _attrDef.getType().getType();
                _visitor.visitVarInsn(Opcodes.ALOAD, 0);
                _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, AttributeUtil.getFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(tempAttrType));
                _visitor.visitInsn(TypeUtil.getOpcode(tempAttrType, 172));
            }
        }, this.getSwitchCaseDefaultAppender(tempLoadRefObjIndex, tempStoreIntIndex), tempLoadRefObjIndex, tempStoreIntIndex, false, SWITCH_SEGMENT_SIZE);
    }

    /**
     * Add switch - case segment into the attribute universal setter method.
     *
     * @param _visitor
     * @param _fieldClass
     * @param _className
     * @param _attrDefList
     * @param _visitTypeInstruction
     * @return
     */
    protected int addSwitchCaseSetValueStatement(MethodVisitor _visitor, Class _fieldClass, String _className, List<AttributeDef> _attrDefList, final boolean _visitTypeInstruction) {
        byte tempLoadRefObjIndex = 1;
        int tempStoreIntIndex = 2 + Type.getType(_fieldClass).getSize();
        return this.addSwitchCaseStatement(_visitor, _className, _attrDefList, new CaseAppender() {
            @Override
            public void addCase(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
                if (_attrDef.isReadOnly()) {
                    _visitor.visitTypeInsn(Opcodes.NEW, "java/lang/UnsupportedOperationException");
                    _visitor.visitInsn(Opcodes.DUP);
                    _visitor.visitLdcInsn(TypeUtil.getSetterName(_attrDef) + ": readonly attribute [" + _attrDef.getName() + "]");
                    _visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/UnsupportedOperationException", "<init>", "(Ljava/lang/String;)V");
                    _visitor.visitInsn(Opcodes.ATHROW);
                } else {
                    _visitor.visitVarInsn(Opcodes.ALOAD, 0);
                    _visitor.visitVarInsn(TypeUtil.getOpcode(_attrDef.getType().getType(), 21), 2);
                    if (_visitTypeInstruction) {
                        _visitor.visitTypeInsn(Opcodes.CHECKCAST, _attrDef.getType().getType().getInternalName());
                    }

                    _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, _className, TypeUtil.getSetterName(_attrDef), TypeUtil.getSetterSignature(_attrDef.getType().getType()));
                }

            }
        }, this.getSwitchCaseDefaultAppender(tempLoadRefObjIndex, tempStoreIntIndex), tempLoadRefObjIndex, tempStoreIntIndex, true, SWITCH_SEGMENT_SIZE);
    }

    /**
     * Add is switch - case segment into the attribute universal getter method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected int addSwitchCaseGetFlagStatement(MethodVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        byte tempLoadRefObjIndex = 1;
        byte tempStoreIntIndex = 2;
        return this.addSwitchCaseStatement(_visitor, _className, _attrDefList, new CaseAppender() {
            @Override
            public void addCase(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
                _visitor.visitVarInsn(Opcodes.ALOAD, 0);
                _visitor.visitFieldInsn(Opcodes.GETFIELD, _className, AttributeUtil.getFlagFieldName(_attrDef.getName()), TypeUtil.getFieldDescriptor(Type.BOOLEAN_TYPE));
                _visitor.visitInsn(TypeUtil.getOpcode(Type.BOOLEAN_TYPE, 172));
            }
        }, this.getSwitchCaseDefaultAppender(tempLoadRefObjIndex, tempStoreIntIndex), tempLoadRefObjIndex, tempStoreIntIndex, false, SWITCH_SEGMENT_SIZE);
    }

    /**
     * Add is switch - case segment into the attribute universal clear method.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @return
     */
    protected int addSwitchCaseClearFlagStatement(MethodVisitor _visitor, String _className, List<AttributeDef> _attrDefList) {
        byte tempLoadRefObjIndex = 1;
        byte tempStoreIntIndex = 2;
        return this.addSwitchCaseStatement(_visitor, _className, _attrDefList, new CaseAppender() {
            @Override
            public void addCase(MethodVisitor _visitor, String _className, AttributeDef _attrDef) {
                if (_attrDef.isReadOnly()) {
                    _visitor.visitTypeInsn(Opcodes.NEW, "java/lang/UnsupportedOperationException");
                    _visitor.visitInsn(Opcodes.DUP);
                    _visitor.visitLdcInsn(TypeUtil.getFlagSetterName(_attrDef) + ": readonly attribute [" + _attrDef.getName() + "]");
                    _visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/UnsupportedOperationException", "<init>", "(Ljava/lang/String;)V");
                    _visitor.visitInsn(Opcodes.ATHROW);
                } else {
                    _visitor.visitVarInsn(Opcodes.ALOAD, 0);
                    _visitor.visitInsn(Opcodes.ICONST_0);
                    _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, _className, TypeUtil.getFlagSetterName(_attrDef), TypeUtil.getFlagSetterSignature());
                }

            }
        }, this.getSwitchCaseDefaultAppender(tempLoadRefObjIndex, tempStoreIntIndex), tempLoadRefObjIndex, tempStoreIntIndex, true, SWITCH_SEGMENT_SIZE);
    }

    /**
     * Get the default segment appender for switch.
     *
     * @param _loadRefObjIndex
     * @param _storeIntIndex
     * @return
     */
    protected DefaultCaseAppender getSwitchCaseDefaultAppender(final int _loadRefObjIndex, int _storeIntIndex) {
        return new DefaultCaseAppender() {
            @Override
            public void addDefaultCase(MethodVisitor _visitor, String _className) {
                Type tempExceptionType = Type.getType(SimpleReflectiveBeanClassDesigner.this.getNoSuchElementExceptionClass());
                _visitor.visitTypeInsn(Opcodes.NEW, tempExceptionType.getInternalName());
                _visitor.visitInsn(Opcodes.DUP);
                _visitor.visitVarInsn(Opcodes.ALOAD, _loadRefObjIndex);
                _visitor.visitMethodInsn(Opcodes.INVOKESPECIAL, tempExceptionType.getInternalName(), "<init>", TypeUtil.getMethodDescriptor(new Type[]{TypeUtil.STRING_TYPE}, Type.VOID_TYPE));
                _visitor.visitInsn(Opcodes.ATHROW);
            }
        };
    }

    protected Class getNoSuchElementExceptionClass() {
        return NoSuchElementException.class;
    }
}
