package com.twinkle.framework.context.model;

import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.context.NormalizedAttributeType;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import lombok.extern.slf4j.Slf4j;

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
public class DefaultNormalizedAttributeType implements NormalizedAttributeType {
    private static final long serialVersionUID = 7568004364229166443L;
    private transient boolean inconsistentSerializedType;
    private transient boolean serializedTypeChecked;
    private String name;
    private transient int typeId;
    private int[] attributeIndexes;
    private int[] indexMap;
    private transient AttributeInfo[] attributeInfos;
    private transient String[] attributeNames;
    private ReadWriteLock readWriteLock;
    private Lock readLock;
    private Lock writeLock;

    public DefaultNormalizedAttributeType(String _name, int _typeId, int _size) {
        this(_name, _typeId, _size, new AttributeInfo[0]);
    }

    public DefaultNormalizedAttributeType(String _name, int _typeId, int _size, AttributeInfo[] _attributeInfoArray) {
        this.inconsistentSerializedType = true;
        this.serializedTypeChecked = false;
        this.readWriteLock = new ReentrantReadWriteLock();
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();
        log.debug("NormalizedAttributeType.NormalizedAttributeType({}, {}, {})", _name, _typeId, _size);
        this.name = _name;
        this.typeId = _typeId;
        this.indexMap = new int[_size];

        int i;
        for (i = 0; i < _size; i++) {
            this.indexMap[i] = -1;
        }

        this.attributeInfos = _attributeInfoArray;
        this.attributeNames = new String[_attributeInfoArray.length];
        this.attributeIndexes = new int[_attributeInfoArray.length];

        for (i = 0; i < _attributeInfoArray.length; this.indexMap[_attributeInfoArray[i].getIndex()] = i++) {
            this.attributeNames[i] = _attributeInfoArray[i].getName();
            this.attributeIndexes[i] = _attributeInfoArray[i].getIndex();
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getTypeId() {
        return this.typeId;
    }

    @Override
    public String[] getAttributeNames() {
        this.readLock.lock();

        String[] tempAttributeNameArray;
        try {
            tempAttributeNameArray = this.attributeNames;
        } finally {
            this.readLock.unlock();
        }

        return tempAttributeNameArray;
    }

    @Override
    public int[] getAttributeIndexes() {
        this.readLock.lock();

        int[] tempAttributeIndexArray;
        try {
            tempAttributeIndexArray = this.attributeIndexes;
        } finally {
            this.readLock.unlock();
        }

        return tempAttributeIndexArray;
    }

    /**
     * Judge the attribute is one member of this Type.
     *
     * @param _attrName
     * @return
     */
    @Override
    public boolean isMember(String _attrName) {
        this.readLock.lock();

        boolean tempFlag;
        try {
            for (int i = 0; i < this.attributeNames.length; ++i) {
                if (this.attributeNames[i].equalsIgnoreCase(_attrName)) {
                    return true;
                }
            }
            tempFlag = false;
        } finally {
            this.readLock.unlock();
        }

        return tempFlag;
    }

    /**
     * Judge the attribute with the index exists in this type or not.
     *
     * @param _index
     * @return
     */
    @Override
    public boolean isMember(int _index) {
        this.readLock.lock();

        boolean tempFlag;
        try {
            tempFlag = _index < this.indexMap.length && this.indexMap[_index] != -1;
        } finally {
            this.readLock.unlock();
        }

        return tempFlag;
    }

    @Override
    public void addAttribute(String _attrName) {
        this.addAttribute(PrimitiveAttributeSchema.getInstance().getAttribute(_attrName));
    }

    /**
     * Add attribute into the arraylist.
     *
     * @param _attributeInfo
     */
    @Override
    public void addAttribute(AttributeInfo _attributeInfo) {
        if (!this.isMember(_attributeInfo.getIndex())) {
            this.writeLock.lock();

            try {
                log.debug("Adding attribute {}->{} to type {}", _attributeInfo.getName(), _attributeInfo.getIndex(), this.name);
                // Add the attribute into the List.
                AttributeInfo[] tempNewAttributeInfoArray = new AttributeInfo[this.attributeInfos.length + 1];
                System.arraycopy(this.attributeInfos, 0, tempNewAttributeInfoArray, 0, this.attributeInfos.length);
                this.attributeInfos = tempNewAttributeInfoArray;
                this.attributeInfos[this.attributeInfos.length - 1] = _attributeInfo;
                String[] tempAttributeNameArray = new String[this.attributeNames.length + 1];
                System.arraycopy(this.attributeNames, 0, tempAttributeNameArray, 0, this.attributeNames.length);
                this.attributeNames = tempAttributeNameArray;
                this.attributeNames[this.attributeNames.length - 1] = _attributeInfo.getName();
                int[] tempAttributeIndexArray = new int[this.attributeIndexes.length + 1];
                System.arraycopy(this.attributeIndexes, 0, tempAttributeIndexArray, 0, this.attributeIndexes.length);
                this.attributeIndexes = tempAttributeIndexArray;
                this.attributeIndexes[this.attributeIndexes.length - 1] = _attributeInfo.getIndex();
                if (_attributeInfo.getIndex() >= this.indexMap.length) {
                    tempAttributeIndexArray = new int[_attributeInfo.getIndex() + 1];
                    System.arraycopy(this.indexMap, 0, tempAttributeIndexArray, 0, this.indexMap.length);
                    // Initialize the index value for the gap items.
                    for (int i = this.indexMap.length; i < tempAttributeIndexArray.length; i++) {
                        tempAttributeIndexArray[i] = -1;
                    }
                    // Get the new Index Array.
                    this.indexMap = tempAttributeIndexArray;
                }
                //Update the new Item's index into the index array.
                this.indexMap[_attributeInfo.getIndex()] = this.attributeNames.length - 1;
            } finally {
                this.writeLock.unlock();
            }

        }
    }

    /**
     * Get the count of the attributes.
     *
     * @return
     */
    @Override
    public int getNumAttributes() {
        this.readLock.lock();

        int tempNum = 0;
        try {
            tempNum = this.attributeNames.length;
        } finally {
            this.readLock.unlock();
        }

        return tempNum;
    }

    /**
     * Judge the type is inconsistent correctly or not.
     *
     * @param _neType
     * @return
     */
    @Override
    public final boolean isInconsistentSerializedType(NormalizedAttributeType _neType) {
        this.writeLock.lock();

        boolean tempFlag;
        try {
            if (this.serializedTypeChecked) {
                return this.inconsistentSerializedType;
            }

            this.serializedTypeChecked = true;
            int[] tempAttributeIndexArray = _neType.getAttributeIndexes();
            int[] tempIndexArray = _neType.getIndexMap();
            if (tempAttributeIndexArray == null) {
                this.inconsistentSerializedType = false;
                tempFlag = false;
                return tempFlag;
            }

            if (tempAttributeIndexArray.length > this.attributeIndexes.length) {
                this.inconsistentSerializedType = true;
            } else {
                int tempIndex = 0;

                while (true) {
                    if (tempIndex >= tempAttributeIndexArray.length) {
                        this.inconsistentSerializedType = false;
                        break;
                    }

                    if (tempAttributeIndexArray[tempIndex] != this.attributeIndexes[tempIndex] && tempIndexArray[tempIndex] != this.indexMap[tempIndex]) {
                        this.inconsistentSerializedType = true;
                        return true;
                    }

                    tempIndex++;
                }
            }

            tempFlag = this.inconsistentSerializedType;
        } finally {
            this.writeLock.unlock();
        }

        return tempFlag;
    }

    /**
     * Get the mapped's index by Type Index.
     *
     * @param _index
     * @return
     */
    @Override
    public final int getNormalizedEventIndex(int _index) {
        return this.indexMap[_index];
    }

    @Override
    public int[] getIndexMap() {
        return this.indexMap;
    }
}
