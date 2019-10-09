package com.twinkle.framework.context.lang;

import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.Operation;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/9/19 5:54 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class AttributeListAttribute implements Attribute, Cloneable, Serializable {
    private final String DELIMITER = "|";
    private final int BLOCK_SIZE = 16;
    private static int type = 111;
    private int[] attrIndexArray = null;
    private int attrCount = 0;

    public AttributeListAttribute() {
    }

    public AttributeListAttribute(AttributeListAttribute _value) {
        this.setValue(_value);
    }

    public AttributeListAttribute(String _value) {
        this.setValue(_value);
    }

    @Override
    public int getPrimitiveType() {
        return Attribute.STRING_TYPE;
    }

    @Override
    public int getTypeIndex() {
        return type;
    }

    @Override
    public void setTypeIndex(int _index) {
        type = _index;
    }

    @Override
    public void setEmptyValue() {
        this.clear();
    }

    public String getValue() {
        return this.toString();
    }

    @Override
    public void setValue(String _value) {
        this.clear();
        StringTokenizer tempTokenizer = new StringTokenizer(_value, DELIMITER);
        int tempTokenCounts = tempTokenizer.countTokens();
        if (this.attrIndexArray == null || this.attrIndexArray.length < tempTokenCounts) {
            this.attrIndexArray = new int[tempTokenCounts];
        }

        for (int i = 0; i < tempTokenCounts; i++) {
            String tempAttrName = tempTokenizer.nextToken();
            AttributeInfo tempAttrInfo = PrimitiveAttributeSchema.getInstance().getAttribute(tempAttrName);
            if (tempAttrInfo == null) {
                throw new IllegalArgumentException("Attribute " + tempAttrName + " is not defined in Attribute Schema.");
            }
            this.add(tempAttrInfo.getIndex());
        }

    }

    @Override
    public void setValue(Object _value) {
        if (_value == null) {
            this.clear();
            return;
        }
        if (_value instanceof Attribute) {
            this.setValue((Attribute) _value);
        } else {
            this.setValue(_value.toString());
        }
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            AttributeListAttribute tempSrcAttr = (AttributeListAttribute) _attr;
            this.clear();
            this.attrCount = tempSrcAttr.attrCount;
            this.setCapacity(tempSrcAttr.capacity());
            if (this.attrCount != 0) {
                System.arraycopy(tempSrcAttr.attrIndexArray, 0, this.attrIndexArray, 0, tempSrcAttr.attrCount);
            }
        }
    }

    @Override
    public void aggregate(Operation _operation, Attribute _attr) {
        if (_operation == Operation.SET) {
            this.setValue(_attr);
        } else {
            throw new IllegalArgumentException("Operation " + _operation + " is not supported.");
        }
    }

    @Override
    public Object clone() {
        return new AttributeListAttribute(this);
    }

    @Override
    public int hashCode() {
        if (this.attrIndexArray == null) {
            return 0;
        } else {
            int tempCode = 0;

            for (int i = 0; i < this.attrCount; i++) {
                tempCode ^= this.attrIndexArray[i];
            }
            return tempCode;
        }
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "";
        }
        StringBuilder tempBuilder = new StringBuilder(256);
        int tempSize = this.size();
        PrimitiveAttributeSchema tempSchema = PrimitiveAttributeSchema.getInstance();
        int i;
        for (i = 0; i < tempSize - 1; i++) {
            tempBuilder.append(tempSchema.getAttribute(this.attrIndexArray[i]).getName());
            tempBuilder.append("|");
        }
        tempBuilder.append(tempSchema.getAttribute(this.attrIndexArray[i]).getName());
        return tempBuilder.toString();
    }
    @Override
    public int compareTo(Object _obj) {
        AttributeListAttribute tempObj2 = (AttributeListAttribute) _obj;
        int tempObj2Count = tempObj2.attrCount;
        int tempObj1Count = this.attrCount;
        if (tempObj1Count < tempObj2Count) {
            return -1;
        } else if (tempObj1Count > tempObj2Count) {
            return 1;
        } else {
            for (int i = 0; i < tempObj1Count; i++) {
                if (this.attrIndexArray[i] < tempObj2.attrIndexArray[i]) {
                    return -1;
                }
                if (this.attrIndexArray[i] > tempObj2.attrIndexArray[i]) {
                    return 1;
                }
            }
            return 0;
        }
    }
    @Override
    public boolean equals(Object _obj) {
        return _obj != null && this == _obj || this.compareTo(_obj) == 0;
    }

    /**
     * Build an array with given attribute's index.
     *
     * @param _attrIndex
     */
    public void add(int _attrIndex) {
        if (this.attrIndexArray == null) {
            this.attrIndexArray = new int[BLOCK_SIZE];
        } else if (this.attrIndexArray.length == this.attrCount) {
            int[] tempNewArray = new int[this.attrCount + BLOCK_SIZE];
            System.arraycopy(this.attrIndexArray, 0, tempNewArray, 0, this.attrCount);
            this.attrIndexArray = tempNewArray;
        }

        this.attrIndexArray[this.attrCount++] = _attrIndex;
    }

    /**
     * Get the attribute's index at the position _index.
     *
     * @param _index
     * @return
     */
    public int getAttrIndex(int _index) {
        return this.attrIndexArray[_index];
    }

    public void add(String var1) {
        AttributeInfo var2 = PrimitiveAttributeSchema.getInstance().getAttribute(var1);
        if (var2 == null) {
            throw new IllegalArgumentException("Attribute " + var1 + " is not defined in NMESchema.");
        } else {
            this.add(var2.getIndex());
        }
    }

    /**
     * Get the Attribute's name which at the _index position.
     *
     * @param _index
     * @return
     */
    public String getAttrName(int _index) {
        return PrimitiveAttributeSchema.getInstance().getAttribute(this.attrIndexArray[_index]).getName();
    }

    /**
     * Reset the attribute's array's size.
     *
     * @param _size
     */
    public void setCapacity(int _size) {
        if (_size == 0) {
            this.attrIndexArray = null;
        }
        if (this.attrIndexArray == null) {
            this.attrIndexArray = new int[_size];
        } else {
            int[] tempAttrIndexArray = new int[_size];
            System.arraycopy(this.attrIndexArray, 0, tempAttrIndexArray, 0, _size);
            this.attrIndexArray = tempAttrIndexArray;
        }

    }

    /**
     * Convert the Attribute List's index to the index array.
     *
     * @return
     */
    public int[] toArray() {
        if (this.attrIndexArray == null) {
            return null;
        } else {
            int[] tempArray = new int[this.attrCount];
            System.arraycopy(this.attrIndexArray, 0, tempArray, 0, this.attrCount);
            return tempArray;
        }
    }

    /**
     * Convert the Attribute List's index to the index array,
     * and set the converted array into the given array.
     *
     * @param _array
     * @return
     */
    public int[] toArray(int[] _array) {
        if (_array.length < this.attrCount) {
            return this.toArray();
        } else {
            System.arraycopy(this.attrIndexArray, 0, _array, 0, this.attrCount);
            return _array;
        }
    }

    /**
     * Judge the array is empty or not?
     *
     * @return
     */
    public boolean isEmpty() {
        return this.attrCount == 0;
    }

    /**
     * Get the array's size.
     *
     * @return
     */
    public int size() {
        return this.attrCount;
    }

    /**
     * Get the array's capacity.
     *
     * @return
     */
    public int capacity() {
        return this.attrIndexArray == null ? 0 : this.attrIndexArray.length;
    }

    /**
     * Clear this attribute.
     *
     */
    private void clear() {
        this.attrCount = 0;
    }
    @Override
    public Object getObjectValue() {
        return this.getValue();
    }
}
