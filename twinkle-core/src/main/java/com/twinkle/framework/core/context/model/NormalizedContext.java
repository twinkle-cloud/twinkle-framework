package com.twinkle.framework.core.context.model;

import com.twinkle.framework.core.context.ContextSchema;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.AttributeInfo;
import com.twinkle.framework.core.lang.ILogAttribute;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-12 17:12<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class NormalizedContext implements Serializable, Cloneable {
    private static final long serialVersionUID = 3534496933565226478L;
    protected Attribute[] attributes;
    private NormalizedAttributeType typeInfo;

    public NormalizedContext() {
        this("%DefaultNormalizedEventType");
    }

    public NormalizedContext(String _neTypeName) {
        this(ContextSchema.getInstance().getNormalizedEventType(_neTypeName));
    }

    public NormalizedContext(String _neTypeName, boolean _createFlag) {
        this(ContextSchema.getInstance().getNormalizedEventType(_neTypeName), _createFlag);
    }

    public NormalizedContext(NormalizedAttributeType _neType) {
        this.typeInfo = _neType;
        if (this.typeInfo == null) {
            throw new IllegalArgumentException("The NormalizedAttributeType is null");
        }
    }

    public NormalizedContext(NormalizedAttributeType _neType, boolean _createFlag) {
        this(_neType);
        if (_createFlag) {
            int[] tempIndexes = this.typeInfo.getAttributeIndexes();
            ContextSchema tempSchema = ContextSchema.getInstance();

            for (int i = 0; i < tempIndexes.length; ++i) {
                this.setAttribute(tempSchema.newAttributeInstance(tempIndexes[i]), tempIndexes[i]);
            }
        }

    }

    public NormalizedAttributeType getType() {
        return this.typeInfo;
    }

    /**
     * Get the Attribute with index.
     *
     * @param _index
     * @return
     */
    public final Attribute getAttribute(int _index) {
        _index = this.typeInfo.getNormalizedEventIndex(_index);
        return this.attributes != null && _index < this.attributes.length && _index >= 0 ? this.attributes[_index] : null;
    }

    /**
     * Get the Attribute with attribute name.
     *
     * @param _attrName
     * @return
     */
    public final Attribute getAttribute(String _attrName) {
        AttributeInfo tempAttributeInfo = ContextSchema.getInstance().getAttribute(_attrName);
        if (tempAttributeInfo == null) {
            throw new IllegalArgumentException(_attrName + " not in ContextSchema");
        } else {
            return this.getAttribute(tempAttributeInfo.getIndex());
        }
    }

    /**
     * Update the position[_index]'s Attribute.
     *
     * @param _attr
     * @param _index
     */
    public final void setAttribute(Attribute _attr, int _index) {
        _index = this.typeInfo.getNormalizedEventIndex(_index);
        this.checkSize(_index);
        this.attributes[_index] = _attr;
    }

    /**
     * Set the _attr to the attribute with _attrName.
     *
     * @param _attr
     * @param _attrName
     */
    public final void setAttribute(Attribute _attr, String _attrName) {
        AttributeInfo tempAttrInfo = ContextSchema.getInstance().getAttribute(_attrName);
        if (tempAttrInfo == null) {
            throw new IllegalArgumentException(_attrName + " not in ContextSchema");
        } else {
            this.setAttribute(_attr, tempAttrInfo.getIndex());
        }
    }

    /**
     * Copy the attribute to the attribute with _index.
     *
     * @param _attr
     * @param _index
     */
    public final void copyAttribute(Attribute _attr, int _index) {
        _index = this.typeInfo.getNormalizedEventIndex(_index);
        this.checkSize(_index);
        if (this.attributes[_index] == null) {
            this.attributes[_index] = (Attribute) _attr.clone();
        } else {
            this.attributes[_index].setValue(_attr);
        }

    }

    /**
     * Whether the attributes is empty or not.
     *
     * @return
     */
    public boolean isEmpty() {
        return this.attributes == null;
    }

    /**
     * Get the count of attributes.
     *
     * @return
     */
    public int numAttributesSet() {
        if (this.attributes == null) {
            return 0;
        } else {
            int tempCount = 0;
            for (int i = 0; i < this.attributes.length; ++i) {
                if (this.attributes[i] != null) {
                    ++tempCount;
                }
            }
            return tempCount;
        }
    }

    /**
     * Clear all of the attributes.
     */
    public void clear() {
        this.attributes = null;
    }

    /**
     * Clear the attributes' values.
     */
    public void clearValues() {
        if (this.attributes != null) {
            for (int i = 0; i < this.attributes.length; i++) {
                if (this.attributes[i] != null) {
                    this.attributes[i].setEmptyValue();
                }
            }
        }
    }

    @Override
    public String toString() {
        return this.toStringWithAttrNames(true);
    }

    /**
     * To string with attribute value ONLY.
     *
     * @return
     */
    public String toStringValuesOnly() {
        if (this.attributes == null) {
            return "Empty NE";
        } else {
            StringBuffer tempBuffer = new StringBuffer(100);

            for (int i = 0; i < this.attributes.length; ++i) {
                tempBuffer.append(toLogString(this.attributes[i]));
                tempBuffer.append(' ');
            }
            return tempBuffer.toString();
        }
    }

    /**
     * To string with attribute name or not.
     *
     * @param _withAttrNameFlag
     * @return
     */
    public String toStringWithAttrNames(boolean _withAttrNameFlag) {
        return this.toStringWithAttrNames(_withAttrNameFlag, true, true, false, System.getProperty("line.separator", "\n"));
    }

    public String toStringWithAttrNames(boolean _withAttrNameFlag, boolean var2, boolean var3, boolean var4, String _separator) {
        if (this.attributes == null) {
            return "Empty StructAttribute";
        } else if (!var2) {
            return "";
        }
        StringBuffer tempBuffer = new StringBuffer(300);
        String[] tempAttributeNameArray = this.typeInfo.getAttributeNames();

        for (int i = 0; i < this.attributes.length; ++i) {
            if (this.attributes[i] != null || _withAttrNameFlag) {
                tempBuffer.append(tempAttributeNameArray[i]);
                tempBuffer.append("=");
                tempBuffer.append(toLogString(this.attributes[i]));
                if (i != this.attributes.length - 1) {
                    tempBuffer.append(", ");
                }
            }
        }

        return tempBuffer.toString();
    }

    /**
     * To Log String if the attribute is logattribute.
     *
     * @param _attr
     * @return
     */
    private static String toLogString(Attribute _attr) {
        if (_attr == null) {
            return null;
        } else {
            return _attr instanceof ILogAttribute ? ((ILogAttribute) _attr).toLogString() : _attr.toString();
        }
    }

    @Override
    public Object clone() {
        NormalizedContext tempNE = null;
        try {
            tempNE = (NormalizedContext) super.clone();
        } catch (CloneNotSupportedException var3) {
            throw new Error("Assertion failure");
        }
        //Copy the attributes.
        if (this.attributes != null) {
            tempNE.attributes = new Attribute[this.attributes.length];
            for (int i = 0; i < this.attributes.length; ++i) {
                if (this.attributes[i] != null) {
                    tempNE.attributes[i] = (Attribute) this.attributes[i].clone();
                }
            }
        }
        return tempNE;
    }

    /**
     * Update current NE with the given NE.
     * Update the current NE unless the given NE is not null.
     *
     * @param _ne
     */
    public void update(NormalizedContext _ne) {
        if (_ne.getType().getTypeId() != this.typeInfo.getTypeId()) {
            throw new IllegalArgumentException("Can't update a StructAttribute with type " + this.typeInfo.getName() + " with a StructAttribute of type " + _ne.typeInfo.getName());
        } else {
            if (_ne.attributes != null) {
                this.checkSize(_ne.attributes.length - 1);

                for (int i = 0; i < _ne.attributes.length; ++i) {
                    if (_ne.attributes[i] != null) {
                        if (this.attributes[i] == null) {
                            this.attributes[i] = (Attribute) _ne.attributes[i].clone();
                        } else {
                            this.attributes[i].setValue(_ne.attributes[i]);
                        }
                    }
                }
            }
        }
    }

    /**
     * Set the NE with the given NE.
     * totally copy.
     *
     * @param _ne
     */
    public void set(NormalizedContext _ne) {
        if (_ne.getType().getTypeId() != this.typeInfo.getTypeId()) {
            this.typeInfo = _ne.getType();
            this.attributes = null;
        }

        if (_ne.attributes == null) {
            this.attributes = null;
        } else {
            if (this.attributes == null || this.attributes.length != _ne.attributes.length) {
                this.attributes = new Attribute[_ne.attributes.length];
            }

            for (int i = 0; i < _ne.attributes.length; ++i) {
                if (_ne.attributes[i] == null) {
                    this.attributes[i] = null;
                } else if (this.attributes[i] == null) {
                    this.attributes[i] = (Attribute) _ne.attributes[i].clone();
                } else {
                    this.attributes[i].setValue(_ne.attributes[i]);
                }
            }
        }
    }

    /**
     * Check the size of current NE. If the size > current size, then initialize the default indexs.
     *
     * @param _size
     */
    protected final void checkSize(int _size) {
        if (this.attributes == null) {
            this.attributes = new Attribute[this.typeInfo.getNumAttributes()];
        }

        if (_size >= this.attributes.length) {
            int tempSize = this.typeInfo.getNumAttributes();
            if (_size >= tempSize) {
                throw new IllegalArgumentException("Attempting to access an attribute that isn't specified in this StructAttribute of type " + this.typeInfo.getName());
            }

            Attribute[] tempAttributeArray = new Attribute[tempSize];
            System.arraycopy(this.attributes, 0, tempAttributeArray, 0, this.attributes.length);
            this.attributes = tempAttributeArray;
        }

    }

    private void readObject(ObjectInputStream _inputStream) throws IOException, ClassNotFoundException {
        _inputStream.defaultReadObject();
        String tempTypeName = null;
        if (this.typeInfo == null) {
            tempTypeName = "%DefaultNormalizedEventType";
        } else {
            tempTypeName = this.typeInfo.getName();
        }

        ContextSchema tempContextSchema = ContextSchema.getInstance();
        NormalizedAttributeType tempTypeInfo = this.typeInfo;
        this.typeInfo = tempContextSchema.getNormalizedEventType(tempTypeName);
        if (this.typeInfo == null) {
            throw new IOException("Cannot restore StructAttribute of type " + tempTypeName + " since that type isn't defined in the ContextSchema");
        } else {
            Attribute[] tempAttributeArray = this.attributes;
            if (tempAttributeArray != null && tempAttributeArray.length != 0) {
                AttributeInfo tempAttrItemInfo;
                if (tempTypeInfo == null) {
                    this.attributes = null;

                    for (int i = 0; i < tempAttributeArray.length; ++i) {
                        Attribute tempAttr = tempAttributeArray[i];
                        if (tempAttr != null) {
                            tempAttrItemInfo = tempContextSchema.getAttribute(i);
                            this.typeInfo.addAttribute(tempAttrItemInfo);
                            this.setAttribute(tempAttr, i);
                        }
                    }
                } else if (this.typeInfo.isInconsistentSerializedType(tempTypeInfo)) {
                    this.attributes = null;
                    int[] tempAttrIndexArray = tempTypeInfo.getAttributeIndexes();

                    int i;
                    for (i = 0; i < tempAttrIndexArray.length; ++i) {
                        tempAttrItemInfo = tempContextSchema.getAttribute(tempAttrIndexArray[i]);
                        this.typeInfo.addAttribute(tempAttrItemInfo);
                    }

                    for (i = 0; i < tempAttrIndexArray.length; ++i) {
                        int tempItemIndex = tempAttrIndexArray[i];
                        int tempItemNEIndex = tempTypeInfo.getNormalizedEventIndex(tempItemIndex);
                        Attribute tempAttr = tempAttributeArray[tempItemNEIndex];
                        if (tempAttr != null) {
                            this.setAttribute(tempAttr, tempItemIndex);
                        }
                    }
                }
            }
        }
    }
}
