package com.twinkle.framework.ruleengine.rule.support;

import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.core.lang.*;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/5/19 3:30 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class CriteriaList {
    protected Vector criteria;
    protected PrimitiveAttributeSchema primitiveAttributeSchema;

    public CriteriaList() {
        this.criteria = new Vector();
        this.primitiveAttributeSchema = PrimitiveAttributeSchema.getInstance();
    }

    public CriteriaList(Criteria[] _criterias) {
        this();
        if (_criterias != null) {
            for (int i = 0; i < _criterias.length; ++i) {
                this.criteria.add(_criterias[i]);
            }
        }
    }

    /**
     * Add criteria into the current list.
     *
     * @param _operand
     * @param _operator
     * @param _value
     */
    public void addCriteria(String _operand, String _operator, String _value){
        this.addCriteria(_operand, _operator, _value, this.criteria.size());
    }

    /**
     * Add the given criteria into the dest pos.
     *
     * @param _operand
     * @param _operator
     * @param _value
     * @param _pos
     */
    public void addCriteria(String _operand, String _operator, String _value, int _pos) {
        int tempAttrIndex;
        try {
            tempAttrIndex = this.primitiveAttributeSchema.getAttributeIndex(_operand, _operand);
        } catch (Exception e) {
            this.criteria.add(_pos, new Criteria(_operand, _operator, _value));
            return;
        }

        AttributeInfo tempAttrInfo = this.primitiveAttributeSchema.getAttribute(_value);
        if (tempAttrInfo == null && _value.charAt(0) == '%') {
            tempAttrInfo = this.primitiveAttributeSchema.getAttribute(_value.substring(1));
        }

        if (tempAttrInfo == null && !_operator.endsWith("within")) {
            Attribute tempAttr = this.primitiveAttributeSchema.newAttributeInstance(tempAttrIndex);
            tempAttr.setValue(_value);
            switch (tempAttr.getPrimitiveType()) {
                case Attribute.INTEGER_TYPE:
                    _value = (new Integer(((IIntegerAttribute) tempAttr).getInt())).toString();
                case Attribute.STRING_TYPE:
                default:
                    break;
                case Attribute.LONG_TYPE:
                    _value = (new Long(((ILongAttribute) tempAttr).getLong())).toString();
                    break;
                case Attribute.FLOAT_TYPE:
                    _value = (new Float(((IFloatAttribute) tempAttr).getFloat())).toString();
                    break;
                case Attribute.DOUBLE_TYPE:
                    _value = (new Double(((IDoubleAttribute) tempAttr).getDouble())).toString();
                    break;
                case Attribute.BYTE_ARRAY_TYPE:
                case Attribute.UNICODE_STRING_TYPE:
                    _value = tempAttr.toString();
            }
        }

        this.criteria.add(_pos, new Criteria(_operand, _operator, _value));
    }

    /**
     * Add the given criteria.
     *
     * @param _operand
     * @param _operator
     */
    public void addCriteria(String _operand, String _operator) {
        try {
            this.primitiveAttributeSchema.getAttributeIndex(_operand, _operand);
        } catch (Exception e) {
            this.criteria.add(new Criteria(_operand, _operator, null));
            return;
        }

        this.criteria.add(new Criteria(_operand, _operator, null));
    }

    public void addCriteriaFirst(String _operand, String _operator, String _value) {
        this.addCriteria(_operand, _operator, _value, 0);
    }

    /**
     * Add Criteria in the first pos.
     *
     * @param _operand
     * @param _operator
     */
    public void addCriteriaFirst(String _operand, String _operator) {
        try {
            this.primitiveAttributeSchema.getAttributeIndex(_operand, _operand);
        } catch (Exception e) {
            this.criteria.add(0, new Criteria(_operand, _operator, null));
            return;
        }

        this.criteria.add(0, new Criteria(_operand, _operator, null));
    }

    /**
     * Get the enumeration of this criteria list.
     *
     * @return
     */
    public Enumeration getCriteria() {
        return this.criteria.elements();
    }

    /**
     * Get the iterator of this criteria list.
     *
     * @return
     */
    public Iterator iterator() {
        return this.criteria.iterator();
    }

    /**
     * Get Criteria list size.
     *
     * @return
     */
    public int size() {
        return this.criteria.size();
    }

    /**
     * Get the Criteria array.
     *
     * @return
     */
    public Criteria[] getCriteriaArray() {
        Criteria[] tempArray = new Criteria[this.criteria.size()];
        return ((Criteria[]) this.criteria.toArray(tempArray));
    }
}
