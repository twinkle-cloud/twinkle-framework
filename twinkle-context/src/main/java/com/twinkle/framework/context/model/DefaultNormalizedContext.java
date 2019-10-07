package com.twinkle.framework.context.model;

import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.api.context.NormalizedAttributeType;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.ILogAttribute;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-12 17:12<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DefaultNormalizedContext implements NormalizedContext {
    private static final long serialVersionUID = 3534496933565226478L;
    private static final String DEFAULT_NORMALIZED_TYPE_NAME = "%DefaultNormalizedEventType";
    private Attribute[] attributes;
    private NormalizedAttributeType typeInfo;

    public DefaultNormalizedContext() {
        this(DEFAULT_NORMALIZED_TYPE_NAME);
    }

    public DefaultNormalizedContext(String _neTypeName) {
        this(PrimitiveAttributeSchema.getInstance().getNormalizedEventType(_neTypeName));
    }

    public DefaultNormalizedContext(String _neTypeName, boolean _createFlag) {
        this(PrimitiveAttributeSchema.getInstance().getNormalizedEventType(_neTypeName), _createFlag);
    }

    public DefaultNormalizedContext(NormalizedAttributeType _neType) {
        this.typeInfo = _neType;
        if (this.typeInfo == null) {
            throw new IllegalArgumentException("The NormalizedAttributeType is null");
        }
    }

    public DefaultNormalizedContext(NormalizedAttributeType _neType, boolean _createFlag) {
        this(_neType);
        if (_createFlag) {
            int[] tempIndexes = this.typeInfo.getAttributeIndexes();
            PrimitiveAttributeSchema tempSchema = PrimitiveAttributeSchema.getInstance();

            for (int i = 0; i < tempIndexes.length; ++i) {
                this.setAttribute(tempSchema.newAttributeInstance(tempIndexes[i]), tempIndexes[i]);
            }
        }
    }

    @Override
    public NormalizedAttributeType getType() {
        return this.typeInfo;
    }

    @Override
    public Attribute[] getAttributes() {
        return this.attributes;
    }

    /**
     * Get the Attribute with index.
     *
     * @param _index
     * @return
     */
    @Override
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
    @Override
    public final Attribute getAttribute(String _attrName) {
        AttributeInfo tempAttributeInfo = PrimitiveAttributeSchema.getInstance().getAttribute(_attrName);
        if (tempAttributeInfo == null) {
            throw new IllegalArgumentException(_attrName + " not in PrimitiveAttributeSchema");
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
    @Override
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
    @Override
    public final void setAttribute(Attribute _attr, String _attrName) {
        AttributeInfo tempAttrInfo = PrimitiveAttributeSchema.getInstance().getAttribute(_attrName);
        if (tempAttrInfo == null) {
            throw new IllegalArgumentException(_attrName + " not in PrimitiveAttributeSchema");
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
    @Override
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
    @Override
    public boolean isEmpty() {
        return this.attributes == null;
    }

    /**
     * Get the count of attributes.
     *
     * @return
     */
    @Override
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
    @Override
    public void clear() {
        this.attributes = null;
    }

    /**
     * Clear the attributes' values.
     */
    @Override
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
    @Override
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
     * @param _printNullFlag
     * @return
     */
    @Override
    public String toStringWithAttrNames(boolean _printNullFlag) {
        return this.toStringWithAttrNames(_printNullFlag, true, true, false, System.getProperty("line.separator", "\n"));
    }

    @Override
    public String toStringWithAttrNames(boolean _printNullFlag, boolean _logAttrFlag, boolean _logStructAttributeFlag, boolean _inSingleLineFlag, String _separator) {
        if (this.attributes == null) {
            return "Empty StructAttribute";
        } else if (!_logAttrFlag) {
            return "";
        }
        StringBuffer tempBuffer = new StringBuffer(300);
        String[] tempAttributeNameArray = this.typeInfo.getAttributeNames();

        for (int i = 0; i < this.attributes.length; ++i) {
            if (this.attributes[i] != null || _printNullFlag) {
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
        DefaultNormalizedContext tempNE = null;
        try {
            tempNE = (DefaultNormalizedContext) super.clone();
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
    @Override
    public void update(NormalizedContext _ne) {
        if (_ne.getType().getTypeId() != this.typeInfo.getTypeId()) {
            throw new IllegalArgumentException("Can't update a StructAttribute with type " + this.typeInfo.getName() + " with a StructAttribute of type " + _ne.getType().getName());
        } else {
            if (_ne.getAttributes() != null) {
                this.checkSize(_ne.getAttributes().length - 1);

                for (int i = 0; i < _ne.getAttributes().length; ++i) {
                    if (_ne.getAttributes()[i] != null) {
                        if (this.attributes[i] == null) {
                            this.attributes[i] = (Attribute) _ne.getAttributes()[i].clone();
                        } else {
                            this.attributes[i].setValue(_ne.getAttributes()[i]);
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
    @Override
    public void set(NormalizedContext _ne) {
        if (_ne.getType().getTypeId() != this.typeInfo.getTypeId()) {
            this.typeInfo = _ne.getType();
            this.attributes = null;
        }

        if (_ne.getAttributes() == null) {
            this.attributes = null;
        } else {
            if (this.attributes == null || this.attributes.length != _ne.getAttributes().length) {
                this.attributes = new Attribute[_ne.getAttributes().length];
            }

            for (int i = 0; i < _ne.getAttributes().length; ++i) {
                if (_ne.getAttributes()[i] == null) {
                    this.attributes[i] = null;
                } else if (this.attributes[i] == null) {
                    this.attributes[i] = (Attribute) _ne.getAttributes()[i].clone();
                } else {
                    this.attributes[i].setValue(_ne.getAttributes()[i]);
                }
            }
        }
    }

    /**
     * Check the size of current NE. If the size > current size, then initialize the default indexs.
     *
     * @param _size
     */
    final void checkSize(int _size) {
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
            tempTypeName = DEFAULT_NORMALIZED_TYPE_NAME;
        } else {
            tempTypeName = this.typeInfo.getName();
        }

        PrimitiveAttributeSchema tempPASchema = PrimitiveAttributeSchema.getInstance();
        NormalizedAttributeType tempTypeInfo = this.typeInfo;
        this.typeInfo = tempPASchema.getNormalizedEventType(tempTypeName);
        if (this.typeInfo == null) {
            throw new IOException("Cannot restore StructAttribute of type " + tempTypeName + " since that type isn't defined in the PrimitiveAttributeSchema");
        }
        Attribute[] tempAttributeArray = this.attributes;
        if (tempAttributeArray != null && tempAttributeArray.length != 0) {
            AttributeInfo tempAttrItemInfo;
            if (tempTypeInfo == null) {
                this.attributes = null;

                for (int i = 0; i < tempAttributeArray.length; ++i) {
                    Attribute tempAttr = tempAttributeArray[i];
                    if (tempAttr != null) {
                        tempAttrItemInfo = tempPASchema.getAttribute(i);
                        this.typeInfo.addAttribute(tempAttrItemInfo.getName());
                        this.setAttribute(tempAttr, i);
                    }
                }
            } else if (this.typeInfo.isInconsistentSerializedType(tempTypeInfo)) {
                this.attributes = null;
                int[] tempAttrIndexArray = tempTypeInfo.getAttributeIndexes();

                int i;
                for (i = 0; i < tempAttrIndexArray.length; ++i) {
                    tempAttrItemInfo = tempPASchema.getAttribute(tempAttrIndexArray[i]);
                    this.typeInfo.addAttribute(tempAttrItemInfo.getName());
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
