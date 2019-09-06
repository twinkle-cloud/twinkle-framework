package com.twinkle.framework.asm.designer;

import com.twinkle.framework.asm.define.AttributeDef;
import com.twinkle.framework.asm.define.BeanTypeDef;
import com.twinkle.framework.asm.utils.TypeUtil;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-05 22:12<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractReflectiveBeanClassDesigner extends RecyclableBeanClassDesigner {
    protected static final int SWITCH_SEGMENT_SIZE = 20;
    private static final Comparator<AttributeDef> ATTR_NAME_COMPARATOR = new AbstractReflectiveBeanClassDesigner.AttributeNameComparator();

    public AbstractReflectiveBeanClassDesigner(String _className, BeanTypeDef _beanTypeDef) {
        super(_className, _beanTypeDef);
    }

    /**
     * Add switch method body.
     *
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @param _caseAppender
     * @param _defaultCaseAppender
     * @param _loadRefObjIndex
     * @param _storeIntIndex
     * @param _needJumpFlag
     * @param _segmentSize
     * @return
     */
    protected int addSwitchCaseStatement(MethodVisitor _visitor, String _className, List<AttributeDef> _attrDefList, CaseAppender _caseAppender,
                                         DefaultCaseAppender _defaultCaseAppender, int _loadRefObjIndex, int _storeIntIndex, boolean _needJumpFlag, int _segmentSize) {
        _visitor.visitVarInsn(Opcodes.ALOAD, _loadRefObjIndex);
        _visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, TypeUtil.STRING_TYPE.getInternalName(), HASHCODE_METHOD_NAME, TypeUtil.getMethodDescriptor(new Type[0], Type.INT_TYPE));
        _visitor.visitVarInsn(Opcodes.ISTORE, _storeIntIndex);
        if (_segmentSize > 0 && _segmentSize < _attrDefList.size()) {
            int tempLastIndex = 0;

            while (tempLastIndex < _attrDefList.size()) {
                int tempIndex = Math.min(tempLastIndex + _segmentSize, _attrDefList.size());
                if (tempIndex < _attrDefList.size()) {
                    _visitor.visitVarInsn(Opcodes.ILOAD, _storeIntIndex);
                    _visitor.visitLdcInsn(getCaseKey(_attrDefList.get(tempIndex)));
                    Label tempLabelA = new Label();
                    _visitor.visitJumpInsn(Opcodes.IF_ICMPGE, tempLabelA);
                    tempLastIndex = this.addSwitchCaseStatement(_visitor, _className, _attrDefList, tempLastIndex, tempIndex - tempLastIndex, _caseAppender, _defaultCaseAppender, _loadRefObjIndex, _storeIntIndex, _needJumpFlag);
                    _visitor.visitLabel(tempLabelA);
                } else {
                    tempLastIndex = this.addSwitchCaseStatement(_visitor, _className, _attrDefList, tempLastIndex, tempIndex - tempLastIndex, _caseAppender, _defaultCaseAppender, _loadRefObjIndex, _storeIntIndex, _needJumpFlag);
                }
            }

            return tempLastIndex;
        } else {
            return this.addSwitchCaseStatement(_visitor, _className, _attrDefList, 0, _attrDefList.size(), _caseAppender, _defaultCaseAppender, _loadRefObjIndex, _storeIntIndex, _needJumpFlag);
        }
    }

    /**
     * @param _visitor
     * @param _className
     * @param _attrDefList
     * @param _beginIndex
     * @param _offSet
     * @param _caseAppender
     * @param _defaultCaseAppender
     * @param _loadRefObjIndex
     * @param _storeIntIndex
     * @param _needJumpFlag
     * @return
     */
    protected int addSwitchCaseStatement(MethodVisitor _visitor, String _className, List<AttributeDef> _attrDefList, int _beginIndex, int _offSet,
                                         CaseAppender _caseAppender, DefaultCaseAppender _defaultCaseAppender,
                                         int _loadRefObjIndex, int _storeIntIndex, boolean _needJumpFlag) {
        int tempAttrItemCount = Math.min(_beginIndex + _offSet, _attrDefList.size());
        _visitor.visitVarInsn(Opcodes.ILOAD, _storeIntIndex);
        Label[] tempLabelArray = getCaseLabels(tempAttrItemCount - _beginIndex);
        Label tempLabelA = new Label();
        Label tempLabelB = _needJumpFlag ? new Label() : null;
        _visitor.visitLookupSwitchInsn(tempLabelA, getCaseKeys(_attrDefList, _beginIndex, tempAttrItemCount - _beginIndex), tempLabelArray);
        int tempLabelIndex = 0;

        int tempIndex;
        for (tempIndex = _beginIndex; tempIndex < tempAttrItemCount; tempLabelIndex++) {
            AttributeDef tempAttrDef = _attrDefList.get(tempIndex);
            _visitor.visitLabel(tempLabelArray[tempLabelIndex]);
            _caseAppender.addCase(_visitor, _className, tempAttrDef);
            if (_needJumpFlag) {
                _visitor.visitJumpInsn(Opcodes.GOTO, tempLabelB);
            }

            tempIndex++;
        }

        _visitor.visitLabel(tempLabelA);
        _defaultCaseAppender.addDefaultCase(_visitor, _className);
        if (_needJumpFlag) {
            _visitor.visitLabel(tempLabelB);
            _visitor.visitInsn(Opcodes.RETURN);
        }

        return tempIndex;
    }

    /**
     * Get Labels List.
     *
     * @param _size
     * @return
     */
    private static Label[] getCaseLabels(int _size) {
        Label[] tempLabelArray = new Label[_size];
        for (int i = 0; i < tempLabelArray.length; i++) {
            tempLabelArray[i] = new Label();
        }
        return tempLabelArray;
    }

    /**
     * Get Case Keys List by given Attribute List.
     *
     * @param _attrDefList
     * @param _beginIndex
     * @param _keySize
     * @return
     */
    private static int[] getCaseKeys(List<AttributeDef> _attrDefList, int _beginIndex, int _keySize) {
        int[] tempCaseKeyArray = new int[_keySize];
        for (int i = 0; i < tempCaseKeyArray.length; i++) {
            tempCaseKeyArray[i] = getCaseKey(_attrDefList.get(_beginIndex + i));
        }
        return tempCaseKeyArray;
    }

    /**
     * Return the attribute's hashcode as the case key.
     *
     * @param _attrDef
     * @return
     */
    private static int getCaseKey(AttributeDef _attrDef) {
        return _attrDef.getName().hashCode();
    }

    /**
     * Sort the Attribute based on attribute name.
     *
     * @param _attrDefList
     * @return
     */
    protected List<AttributeDef> sort(List<AttributeDef> _attrDefList) {
        List<AttributeDef> temResultList = new ArrayList(_attrDefList);
        Collections.sort(temResultList, ATTR_NAME_COMPARATOR);
        return temResultList;
    }

    /**
     * Filter the attribute define list with the given type.
     *
     * @param _sourceList
     * @param _matchType
     * @return
     */
    protected List<AttributeDef> filterExactMatch(List<AttributeDef> _sourceList, Type _matchType) {
        return _sourceList.parallelStream().filter(item -> _matchType.equals(item.getType().getType())).collect(Collectors.toList());
    }

    /**
     * To get the Attribute def list who is an Object Attribute
     * and does not exist in _excludeTypes.
     *
     * @param _sourceList
     * @param _excludeTypes
     * @return
     */
    protected List<AttributeDef> filterObjectExcludes(List<AttributeDef> _sourceList, Collection<Type> _excludeTypes) {
        return _sourceList.parallelStream().filter(item -> item.getType().getType().getSort() == Type.OBJECT
                && !_excludeTypes.contains(item.getType().getType())).collect(Collectors.toList());
    }

    /**
     * To get the Attribute def list who is an Object Array Attribute
     * and does not exist in _excludeTypes.
     *
     * @param _sourceList
     * @param _excludeTypes
     * @return
     */
    protected List<AttributeDef> filterObjectArraysExcludes(List<AttributeDef> _sourceList, Collection<Type> _excludeTypes) {
        return _sourceList.parallelStream().filter(item -> item.getType().getType().getSort() == Type.ARRAY
                && item.getType().getType().getElementType().getSort() == Type.OBJECT
                && !_excludeTypes.contains(item.getType().getType())).collect(Collectors.toList());
    }

    /**
     * Attribute Name comparator using the hash code.
     */
    protected static class AttributeNameComparator implements Comparator<AttributeDef> {
        protected AttributeNameComparator() {
        }

        @Override
        public int compare(AttributeDef _attr1, AttributeDef _attr2) {
            int tempHashCode1 = _attr1.getName().hashCode();
            int tempHashCode2 = _attr2.getName().hashCode();
            if (tempHashCode1 < tempHashCode2) {
                return -1;
            } else {
                return tempHashCode1 > tempHashCode2 ? 1 : 0;
            }
        }
    }

    protected interface DefaultCaseAppender {
        /**
         * Add default segment for switch.
         *
         * @param _visitor
         * @param _className
         */
        void addDefaultCase(MethodVisitor _visitor, String _className);
    }

    protected interface CaseAppender {
        /**
         * Add case segement for switch.
         *
         * @param _visitor
         * @param _className
         * @param _attrDef
         */
        void addCase(MethodVisitor _visitor, String _className, AttributeDef _attrDef);
    }
}
