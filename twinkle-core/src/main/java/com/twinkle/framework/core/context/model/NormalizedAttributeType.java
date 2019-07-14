package com.twinkle.framework.core.context.model;

import com.twinkle.framework.core.context.ContextSchema;
import com.twinkle.framework.core.lang.AttributeInfo;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-12 15:07<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class NormalizedAttributeType implements Serializable {
    private static final long serialVersionUID = -3533688971480842183L;
    private String name_;
    private int[] attributeIndexes_;
    private int[] indexMap_;
    private transient AttributeInfo[] attributeInfos_;
    private transient String[] attributeNames_;
    private transient int typeId_;
    private transient boolean inconsistentSerializedType_;
    private transient boolean serializedTypeChecked_;
    private ReadWriteLock rwLock_;
    private Lock readLock_;
    private Lock writeLock_;

    public NormalizedAttributeType(String _name, int _typeId, int _size) {
        this(_name, _typeId, _size, new AttributeInfo[0]);
    }

    public NormalizedAttributeType(String _name, int _typeId, int _size, AttributeInfo[] _attributeInfoArray) {
        this.inconsistentSerializedType_ = true;
        this.serializedTypeChecked_ = false;
        this.rwLock_ = new ReentrantReadWriteLock();
        this.readLock_ = this.rwLock_.readLock();
        this.writeLock_ = this.rwLock_.writeLock();
        log.debug("NormalizedAttributeType.NormalizedAttributeType({}, {}, {})", _name, _typeId, _size);
        this.name_ = _name;
        this.typeId_ = _typeId;
        this.indexMap_ = new int[_size];

        int i;
        for(i = 0; i < _size; ++i) {
            this.indexMap_[i] = -1;
        }

        this.attributeInfos_ = _attributeInfoArray;
        this.attributeNames_ = new String[_attributeInfoArray.length];
        this.attributeIndexes_ = new int[_attributeInfoArray.length];

        for(i = 0; i < _attributeInfoArray.length; this.indexMap_[_attributeInfoArray[i].getIndex()] = i++) {
            this.attributeNames_[i] = _attributeInfoArray[i].getName();
            this.attributeIndexes_[i] = _attributeInfoArray[i].getIndex();
        }

    }

    public String getName() {
        return this.name_;
    }

    public int getTypeId() {
        return this.typeId_;
    }

    public String[] getAttributeNames() {
        this.readLock_.lock();

        String[] tempAttributeNameArray;
        try {
            tempAttributeNameArray = this.attributeNames_;
        } finally {
            this.readLock_.unlock();
        }

        return tempAttributeNameArray;
    }

    public int[] getAttributeIndexes() {
        this.readLock_.lock();

        int[] tempAttributeIndexArray;
        try {
            tempAttributeIndexArray = this.attributeIndexes_;
        } finally {
            this.readLock_.unlock();
        }

        return tempAttributeIndexArray;
    }

    /**
     * Judge the attribute is one member of this Type.
     *
     * @param _attrName
     * @return
     */
    public boolean isMember(String _attrName) {
        this.readLock_.lock();

        boolean tempFlag;
        try {
            for(int i = 0; i < this.attributeNames_.length; ++i) {
                if (this.attributeNames_[i].equalsIgnoreCase(_attrName)) {
                    return true;
                }
            }
            tempFlag = false;
        } finally {
            this.readLock_.unlock();
        }

        return tempFlag;
    }

    /**
     * Judge the attribute with the index exists in this type or not.
     *
     * @param _index
     * @return
     */
    public boolean isMember(int _index) {
        this.readLock_.lock();

        boolean tempFlag;
        try {
            tempFlag = _index < this.indexMap_.length && this.indexMap_[_index] != -1;
        } finally {
            this.readLock_.unlock();
        }

        return tempFlag;
    }

    public void addAttribute(String _attrName) {
        this.addAttribute(ContextSchema.getInstance().getAttribute(_attrName));
    }

    /**
     * Add attribute into the arraylist.
     *
     * @param _attributeInfo
     */
    public void addAttribute(AttributeInfo _attributeInfo) {
        if (!this.isMember(_attributeInfo.getIndex())) {
            this.writeLock_.lock();

            try {
                log.debug("Adding attribute {}->{} to type ", _attributeInfo.getName(), _attributeInfo.getIndex(), this.name_);
                // Add the attribute into the List.
                AttributeInfo[] tempNewAttributeInfoArray = new AttributeInfo[this.attributeInfos_.length + 1];
                System.arraycopy(this.attributeInfos_, 0, tempNewAttributeInfoArray, 0, this.attributeInfos_.length);
                this.attributeInfos_ = tempNewAttributeInfoArray;
                this.attributeInfos_[this.attributeInfos_.length - 1] = _attributeInfo;
                String[] tempAttributeNameArray = new String[this.attributeNames_.length + 1];
                System.arraycopy(this.attributeNames_, 0, tempAttributeNameArray, 0, this.attributeNames_.length);
                this.attributeNames_ = tempAttributeNameArray;
                this.attributeNames_[this.attributeNames_.length - 1] = _attributeInfo.getName();
                int[] tempAttributeIndexArray = new int[this.attributeIndexes_.length + 1];
                System.arraycopy(this.attributeIndexes_, 0, tempAttributeIndexArray, 0, this.attributeIndexes_.length);
                this.attributeIndexes_ = tempAttributeIndexArray;
                this.attributeIndexes_[this.attributeIndexes_.length - 1] = _attributeInfo.getIndex();
                if (_attributeInfo.getIndex() >= this.indexMap_.length) {
                    tempAttributeIndexArray = new int[_attributeInfo.getIndex() + 1];
                    System.arraycopy(this.indexMap_, 0, tempAttributeIndexArray, 0, this.indexMap_.length);
                    // Initialize the index value for the gap items.
                    for(int i = this.indexMap_.length; i < tempAttributeIndexArray.length; i++) {
                        tempAttributeIndexArray[i] = -1;
                    }
                    // Get the new Index Array.
                    this.indexMap_ = tempAttributeIndexArray;
                }
                //Update the new Item's index into the index array.
                this.indexMap_[_attributeInfo.getIndex()] = this.attributeNames_.length - 1;
            } finally {
                this.writeLock_.unlock();
            }

        }
    }

    /**
     * Get the count of the attributes.
     *
     * @return
     */
    public int getNumAttributes() {
        this.readLock_.lock();

        int tempNum = 0;
        try {
            tempNum = this.attributeNames_.length;
        } finally {
            this.readLock_.unlock();
        }

        return tempNum;
    }

    /**
     * Judge the type is inconsistent correctly or not.
     *
     * @param _neType
     * @return
     */
    boolean isInconsistentSerializedType(NormalizedAttributeType _neType) {
        this.writeLock_.lock();

        boolean tempFlag;
        try {
            if (this.serializedTypeChecked_) {
                return this.inconsistentSerializedType_;
            }

            this.serializedTypeChecked_ = true;
            int[] tempAttributeIndexArray = _neType.attributeIndexes_;
            int[] tempIndexArray = _neType.indexMap_;
            if (tempAttributeIndexArray == null) {
                this.inconsistentSerializedType_ = false;
                tempFlag = false;
                return tempFlag;
            }

            if (tempAttributeIndexArray.length > this.attributeIndexes_.length) {
                this.inconsistentSerializedType_ = true;
            } else {
                int tempIndex = 0;

                while(true) {
                    if (tempIndex >= tempAttributeIndexArray.length) {
                        this.inconsistentSerializedType_ = false;
                        break;
                    }

                    if (tempAttributeIndexArray[tempIndex] != this.attributeIndexes_[tempIndex] && tempIndexArray[tempIndex] != this.indexMap_[tempIndex]) {
                        this.inconsistentSerializedType_ = true;
                        return true;
                    }

                    tempIndex++;
                }
            }

            tempFlag = this.inconsistentSerializedType_;
        } finally {
            this.writeLock_.unlock();
        }

        return tempFlag;
    }

    /**
     * Get the mapped's index by Type Index.
     *
     * @param _index
     * @return
     */
    final int getNormalizedEventIndex(int _index) {
        return this.indexMap_[_index];
    }

    int[] getIndexMap() {
        return this.indexMap_;
    }
}
