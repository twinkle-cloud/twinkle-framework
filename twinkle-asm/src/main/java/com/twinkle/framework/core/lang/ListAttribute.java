package com.twinkle.framework.core.lang;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.core.utils.CharsetUtil;

import java.io.Serializable;
import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-19 17:21<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ListAttribute implements IListAttribute, Cloneable, Serializable {
    private final String DELIMITER = "|";
    private final String NULL_VALUE = "null";
    protected List<Attribute> elements = new ArrayList(8);
    private int type = 110;
    protected Class<?> attrClass_ = null;

    public ListAttribute() {
    }

    public ListAttribute(ListAttribute _attr) {
        this.setValue((Attribute) _attr);
    }

    /**
     * Build an list instance with "xxxAttribute|aa,bb,cc
     *
     * @param _value
     */
    public ListAttribute(String _value) {
        this.setValue(_value);
    }

    @Override
    public int getPrimitiveType() {
        return LIST_ATTRIBUTE_TYPE;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setType(int _type) {
        this.type = _type;
    }

    @Override
    public void setEmptyValue() {
        this.clear();
    }

    public String getValue() {
        return this.toString();
    }

    /**
     * The _valueStr's example:
     * Attribute Class | 10 | Attr1 | Attr2
     *
     * @param _valueStr
     */
    @Override
    public void setValue(String _valueStr) {
        try {
            this.clear();
            StringTokenizer tempST = new StringTokenizer(_valueStr, DELIMITER, true);
            String tempItemClassToken = tempST.nextToken();
            if (!tempItemClassToken.equals("null")) {
                //abandon the delimiter between classname and size.
                tempST.nextToken();
                //Get attribute class.
                this.attrClass_ = Class.forName(tempItemClassToken);
                //Get list size.
                int tempTotalItemCount = Integer.parseInt(tempST.nextToken());
                //Abandon the delimiter followed size.
                tempST.nextToken();
                if (tempTotalItemCount == 0) {
                    this.attrClass_ = null;
                } else {
                    int tempCount = 0;
                    boolean isDelimiterFlag = true;
                    Attribute tempAttr = null;

                    String tempToken = null;
                    try {
                        while (true) {
                            while (tempCount < tempTotalItemCount) {
                                tempToken = tempST.nextToken();
                                if (tempToken.equals(DELIMITER)) {
                                    if (isDelimiterFlag) {
                                        tempToken = "";
                                    } else {
                                        if (tempST.hasMoreTokens()) {
                                            isDelimiterFlag = true;
                                            continue;
                                        }
                                        tempToken = "";
                                    }
                                } else {
                                    isDelimiterFlag = false;
                                }

                                tempToken = CharsetUtil.hexToAscii(tempToken);
                                tempCount++;
                                //Get the attribute
                                tempAttr = (Attribute) this.attrClass_.newInstance();

                                if (tempAttr instanceof TimeAttribute) {
                                    ((TimeAttribute) tempAttr).setTimeFormat(TimeAttribute.DEFAULT_TIME_FORMAT);
                                }
                                tempAttr.setValue(tempToken);
                                this.add(tempAttr);
                            }
                            return;
                        }
                    } catch (NoSuchElementException e) {
                        while (tempCount < tempTotalItemCount) {
                            tempToken = CharsetUtil.hexToAscii("");
                            tempAttr.setEmptyValue();
                            this.add(tempAttr);
                            tempCount++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            IllegalArgumentException tempException = new IllegalArgumentException("The value " + _valueStr + " is not encoded correctly.");
            tempException.initCause(e);
            throw tempException;
        }
    }

    @Override
    public void setValue(Object _value) {
        if (_value == null) {
            this.setEmptyValue();
            return;
        }
        if (_value instanceof Attribute) {
            this.setValue((Attribute) _value);
            return;
        }
        if (_value instanceof List) {
            this.elements = (List) _value;
            return;
        }
        this.setValue(_value.toString());
    }

    @Override
    public void setValue(Attribute _attr) {
        if (this != _attr) {
            if (_attr instanceof ListAttribute) {
                ListAttribute tempAttr = (ListAttribute) _attr;
                this.clear();
                int tempSize = tempAttr.size();
                this.type = tempAttr.type;
                this.attrClass_ = tempAttr.attrClass_;

                for (int i = 0; i < tempSize; i++) {
                    this.elements.add((Attribute)tempAttr.get(i).clone());
                }
            } else {
                this.setValue(_attr.toString());
            }
        }
    }

    @Override
    public void aggregate(int _operation, Attribute _attr) {
        if (_operation == OPERATION_SET) {
            this.setValue(_attr);
        } else {
            throw new IllegalArgumentException("Operation " + _operation + " is not supported.");
        }
    }

    @Override
    public int getOperationID(String _operationName) {
        return _operationName.equals("set") ? OPERATION_SET : -1;
    }

    @Override
    public Object clone() {
        return new ListAttribute(this);
    }

    @Override
    public int hashCode() {
        int tempCode = 0;
        Object[] tempObjArray = this.elements.toArray();

        for (int i = 0; i < tempObjArray.length; ++i) {
            tempCode ^= tempObjArray[i].hashCode();
        }

        return tempCode;
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "null";
        } else {
            StringBuffer tempBuffer = new StringBuffer(256);
            tempBuffer.append(this.attrClass_.getName());
            tempBuffer.append("|");
            int tempSize = this.elements.size();
            tempBuffer.append(tempSize);
            tempBuffer.append("|");
            String tempItem = null;

            for (int i = 0; i < tempSize - 1; ++i) {
                tempItem = CharsetUtil.asciiToHex(this.get(i).toString());
                tempBuffer.append(tempItem);
                tempBuffer.append("|");
            }

            tempItem = CharsetUtil.asciiToHex(this.get(tempSize - 1).toString());
            tempBuffer.append(tempItem);
            return tempBuffer.toString();
        }
    }

    @Override
    public int compareTo(Object _obj) {
        ListAttribute tempDestAttr = (ListAttribute) _obj;
        List tempDestList = tempDestAttr.elements;
        int tempDestSize = tempDestList.size();
        int tempThisSize = this.elements.size();
        if (tempThisSize < tempDestSize) {
            return -1;
        } else if (tempThisSize > tempDestSize) {
            return 1;
        } else {
            for (int i = 0; i < tempThisSize; ++i) {
                Attribute tempThisItemAttr = (Attribute) this.elements.get(i);
                Attribute tempDestItemAttr = (Attribute) tempDestList.get(i);
                int tempResult = tempThisItemAttr.compareTo(tempDestItemAttr);
                if (tempResult != 0) {
                    return tempResult;
                }
            }
            return 0;
        }
    }

    @Override
    public boolean equals(Object _obj) {
        try {
            return _obj != null && this == _obj || this.compareTo(_obj) == 0;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * Add the attr into the dest index.
     *
     * @param _index
     * @param _attr
     */
    @Override
    public void add(int _index, Attribute _attr) {
        if (_attr != null) {
            if (this.isEmpty()) {
                this.attrClass_ = _attr.getClass();
                this.elements.add(_index, _attr);
            } else {
                if (!this.attrClass_.equals(_attr.getClass())) {
                    throw new IllegalArgumentException("Attribute of type " + _attr.getClass() + " cannot be added to list of " + this.attrClass_ + " attributes.");
                }
                this.elements.add(_index, _attr);
            }
        } else {
            throw new IllegalArgumentException("Attribute cannot be null");
        }
    }

    /**
     * Add attr into the list.
     *
     * @param _attr
     */
    public void add(Attribute _attr) {
        this.add(this.size(), _attr);
    }

    /**
     * Get the attr at the index position.
     *
     * @param _index
     * @return
     */
    @Override
    public Attribute get(int _index) {
        return (Attribute) this.elements.get(_index);
    }

    /**
     * Replace the attr at the Index position.
     *
     * @param _index
     * @param _attr
     * @return
     */
    @Override
    public Attribute set(int _index, Attribute _attr) {
        if (_attr != null && this.attrClass_ != null && this.attrClass_.equals(_attr.getClass())) {
            return (Attribute) this.elements.set(_index, _attr);
        } else {
            throw new IllegalArgumentException("Attribute of type " + (_attr != null ? _attr.getClass().getName() : "null") + " cannot be set in a list of " + this.attrClass_ + " attributes.");
        }
    }

    @Override
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    @Override
    public Attribute[] toArray() {
        Attribute[] tempAttrArray = new Attribute[this.elements.size()];
        return (Attribute[]) ((Attribute[]) this.elements.toArray(tempAttrArray));
    }

    @Override
    public Attribute[] toArray(Attribute[] _attrArray) {
        return (Attribute[]) ((Attribute[]) this.elements.toArray(_attrArray));
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public boolean contains(Attribute _attr) {
        return this.elements.contains(_attr);
    }

    @Override
    public boolean containsAll(Collection<? extends IListAttribute> _collection) {
        for (IListAttribute e : _collection)
            if (!contains(e))
                return false;
        return true;
    }

    @Override
    public void addAll(Collection<? extends IListAttribute> _collection) {
        for (IListAttribute e : _collection)
            add(e);
    }

    private void clear() {
        this.elements.clear();
        this.attrClass_ = null;
    }

    public static void main(String[] _args) {
        boolean var1 = false;
        Random tempRandom = new Random();
        int tempValue;
        if (_args.length != 0) {
            tempValue = Integer.parseInt(_args[0]);
        } else {
            tempValue = tempRandom.nextInt();
            tempValue = tempValue < 0 ? -tempValue : tempValue;
            tempValue %= 20;
        }

        ListAttribute listAttribute = new ListAttribute();

        for (int i = 0; i < tempValue; ++i) {
            IntegerAttribute var5 = new IntegerAttribute();
            var5.setValue(tempRandom.nextInt());
            String var6 = CharsetUtil.asciiToHex(var5.toString());
            System.out.println("AttrValue[" + i + "] = " + var5.toString());
            listAttribute.add(var5);
        }

        String var8 = listAttribute.toString();
        System.out.println("List attribute = " + var8);
        listAttribute.setValue(var8);

        for (int i = 0; i < listAttribute.size(); ++i) {
            System.out.println("Attribute[" + i + "] = " + listAttribute.get(i));
        }

    }

    @Override
    public Object getObjectValue() {
        return this.elements;
    }

    @Override
    public JSONObject getJsonObjectValue() {
        return JSONObject.parseObject(this.elements.toString());
    }
}
