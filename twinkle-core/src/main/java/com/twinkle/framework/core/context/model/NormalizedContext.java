package com.twinkle.framework.core.context.model;

import com.twinkle.framework.core.context.ContextSchema;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.AttributeInfo;
import com.twinkle.framework.core.lang.LogAttribute;

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
public class NormalizedContext implements Serializable, Cloneable{
    private static final long serialVersionUID = 3534496933565226478L;
    protected Attribute[] attributes_;
    private NormalizedAttributeType typeInfo_;

    public NormalizedContext() {
        this("%DefaultNormalizedEventType");
    }

    public NormalizedContext(String var1) {
        this(ContextSchema.getInstance().getNormalizedEventType(var1));
    }

    public NormalizedContext(String var1, boolean var2) {
        this(ContextSchema.getInstance().getNormalizedEventType(var1), var2);
    }

    public NormalizedContext(NormalizedAttributeType _neType) {
        this.typeInfo_ = _neType;
        if (this.typeInfo_ == null) {
            throw new IllegalArgumentException("The NormalizedAttributeType is null");
        }
    }

    public NormalizedContext(NormalizedAttributeType _neType, boolean _createFlag) {
        this(_neType);
        if (_createFlag) {
            int[] tempIndexes = this.typeInfo_.getAttributeIndexes();
            ContextSchema tempSchema = ContextSchema.getInstance();

            for(int i = 0; i < tempIndexes.length; ++i) {
                this.setAttribute(tempSchema.newAttributeInstance(tempIndexes[i]), tempIndexes[i]);
            }
        }

    }

    public NormalizedAttributeType getType() {
        return this.typeInfo_;
    }

    /**
     * Get the Attribute with index.
     *
     * @param _index
     * @return
     */
    public final Attribute getAttribute(int _index) {
        _index = this.typeInfo_.getNormalizedEventIndex(_index);
        return this.attributes_ != null && _index < this.attributes_.length && _index >= 0 ? this.attributes_[_index] : null;
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
     * @param _attr
     * @param _index
     */
    public final void setAttribute(Attribute _attr, int _index) {
        _index = this.typeInfo_.getNormalizedEventIndex(_index);
        this.checkSize(_index);
        this.attributes_[_index] = _attr;
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
        _index = this.typeInfo_.getNormalizedEventIndex(_index);
        this.checkSize(_index);
        if (this.attributes_[_index] == null) {
            this.attributes_[_index] = (Attribute)_attr.clone();
        } else {
            this.attributes_[_index].setValue(_attr);
        }

    }

    /**
     * Whether the attributes is empty or not.
     *
     * @return
     */
    public boolean isEmpty() {
        return this.attributes_ == null;
    }

    /**
     * Get the count of attributes.
     *
     * @return
     */
    public int numAttributesSet() {
        if (this.attributes_ == null) {
            return 0;
        } else {
            int tempCount = 0;
            for(int i = 0; i < this.attributes_.length; ++i) {
                if (this.attributes_[i] != null) {
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
        this.attributes_ = null;
    }

    /**
     * Clear the attributes' values.
     */
    public void clearValues() {
        if (this.attributes_ != null) {
            for(int i = 0; i < this.attributes_.length; i++) {
                if (this.attributes_[i] != null) {
                    this.attributes_[i].setEmptyValue();
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
        if (this.attributes_ == null) {
            return "Empty NE";
        } else {
            StringBuffer tempBuffer = new StringBuffer(100);

            for(int i = 0; i < this.attributes_.length; ++i) {
                tempBuffer.append(toLogString(this.attributes_[i]));
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
        if (this.attributes_ == null) {
            return "Empty NME";
        } else if (!var2) {
            return "";
        } else {
            StringBuffer tempBuffer = new StringBuffer(300);
            String[] tempAttributeNameArray = this.typeInfo_.getAttributeNames();

            for(int i = 0; i < this.attributes_.length; ++i) {
                if (this.attributes_[i] != null || _withAttrNameFlag) {
                    tempBuffer.append(tempAttributeNameArray[i]);
                    tempBuffer.append("=");
                    tempBuffer.append(toLogString(this.attributes_[i]));
                    if (i != this.attributes_.length - 1) {
                        tempBuffer.append(", ");
                    }
                }
            }

            return tempBuffer.toString();
        }
    }

    /**
     * To Log String if the attribute is logattribute.
     * @param _attr
     * @return
     */
    private static String toLogString(Attribute _attr) {
        if (_attr == null) {
            return null;
        } else {
            return _attr instanceof LogAttribute ? ((LogAttribute)_attr).toLogString() : _attr.toString();
        }
    }

    @Override
    public Object clone() {
        NormalizedContext tempNE = null;
        try {
            tempNE = (NormalizedContext)super.clone();
        } catch (CloneNotSupportedException var3) {
            throw new Error("Assertion failure");
        }
        //Copy the attributes.
        if (this.attributes_ != null) {
            tempNE.attributes_ = new Attribute[this.attributes_.length];
            for(int i = 0; i < this.attributes_.length; ++i) {
                if (this.attributes_[i] != null) {
                    tempNE.attributes_[i] = (Attribute)this.attributes_[i].clone();
                }
            }
        }
        return tempNE;
    }

    /**
     * Update current NE with the given NE.
     * Update the current NE unless the given NE is not null.
     * @param _ne
     */
    public void update(NormalizedContext _ne) {
        if (_ne.getType().getTypeId() != this.typeInfo_.getTypeId()) {
            throw new IllegalArgumentException("Can't update an NME with type " + this.typeInfo_.getName() + " with an NME of type " + _ne.typeInfo_.getName());
        } else {
            if (_ne.attributes_ != null) {
                this.checkSize(_ne.attributes_.length - 1);

                for(int i = 0; i < _ne.attributes_.length; ++i) {
                    if (_ne.attributes_[i] != null) {
                        if (this.attributes_[i] == null) {
                            this.attributes_[i] = (Attribute)_ne.attributes_[i].clone();
                        } else {
                            this.attributes_[i].setValue(_ne.attributes_[i]);
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
        if (_ne.getType().getTypeId() != this.typeInfo_.getTypeId()) {
            this.typeInfo_ = _ne.getType();
            this.attributes_ = null;
        }

        if (_ne.attributes_ == null) {
            this.attributes_ = null;
        } else {
            if (this.attributes_ == null || this.attributes_.length != _ne.attributes_.length) {
                this.attributes_ = new Attribute[_ne.attributes_.length];
            }

            for(int i = 0; i < _ne.attributes_.length; ++i) {
                if (_ne.attributes_[i] == null) {
                    this.attributes_[i] = null;
                } else if (this.attributes_[i] == null) {
                    this.attributes_[i] = (Attribute)_ne.attributes_[i].clone();
                } else {
                    this.attributes_[i].setValue(_ne.attributes_[i]);
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
        if (this.attributes_ == null) {
            this.attributes_ = new Attribute[this.typeInfo_.getNumAttributes()];
        }

        if (_size >= this.attributes_.length) {
            int tempSize = this.typeInfo_.getNumAttributes();
            if (_size >= tempSize) {
                throw new IllegalArgumentException("Attempting to access an attribute that isn't specified in this NME of type " + this.typeInfo_.getName());
            }

            Attribute[] tempAttributeArray = new Attribute[tempSize];
            System.arraycopy(this.attributes_, 0, tempAttributeArray, 0, this.attributes_.length);
            this.attributes_ = tempAttributeArray;
        }

    }

    private void readObject(ObjectInputStream _inputStream) throws IOException, ClassNotFoundException {
        _inputStream.defaultReadObject();
        String var2 = null;
        if (this.typeInfo_ == null) {
            var2 = "%DefaultNormalizedEventType";
        } else {
            var2 = this.typeInfo_.getName();
        }

        ContextSchema var3 = ContextSchema.getInstance();
        NormalizedAttributeType var4 = this.typeInfo_;
        this.typeInfo_ = var3.getNormalizedEventType(var2);
        if (this.typeInfo_ == null) {
            throw new IOException("Cannot restore NME of type " + var2 + " since that type isn't defined in the ContextSchema");
        } else {
            Attribute[] var5 = this.attributes_;
            if (var5 != null && var5.length != 0) {
                AttributeInfo var8;
                if (var4 == null) {
                    this.attributes_ = null;

                    for(int i = 0; i < var5.length; ++i) {
                        Attribute var7 = var5[i];
                        if (var7 != null) {
                            var8 = var3.getAttribute(i);
                            this.typeInfo_.addAttribute(var8);
                            this.setAttribute(var7, i);
                        }
                    }
                } else if (this.typeInfo_.isInconsistentSerializedType(var4)) {
                    this.attributes_ = null;
                    int[] var11 = var4.getAttributeIndexes();

                    int i;
                    for(i = 0; i < var11.length; ++i) {
                        var8 = var3.getAttribute(var11[i]);
                        this.typeInfo_.addAttribute(var8);
                    }

                    for(i = 0; i < var11.length; ++i) {
                        int var13 = var11[i];
                        int var9 = var4.getNormalizedEventIndex(var13);
                        Attribute var10 = var5[var9];
                        if (var10 != null) {
                            this.setAttribute(var10, var13);
                        }
                    }
                }

            }
        }
    }
}
