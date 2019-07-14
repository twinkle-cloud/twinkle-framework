package com.twinkle.framework.core.context;

import com.twinkle.framework.core.context.model.NormalizedAttributeType;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.AttributeInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-12 15:01<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class ContextSchema {
    private int num_types_;
    private static volatile ContextSchema instance;
    protected Map attributeNameMap;
    protected List attributeList;
    protected List typeList;
    protected boolean initialized = false;
    protected Map normalizedAttributeTypeMap = new HashMap();
    protected int attributeCount = 0;
    protected NormalizedAttributeType defaultNormalizedAttributeType_;
    protected int normalizedAttrubiteTypeCount;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock;
    private Lock writeLock;
    public static final String DEFAULT_NME_TYPE = "%DefaultNormalizedEventType";

    private ContextSchema() {
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();
        this.attributeNameMap = new HashMap();
        this.attributeList = new ArrayList();
        this.typeList = new ArrayList();
        this.num_types_ = 1;
    }

    public void configure(String[][] _attrColumns) throws IllegalArgumentException {
        if (this.defaultNormalizedAttributeType_ == null) {
            this.defaultNormalizedAttributeType_ = new NormalizedAttributeType(DEFAULT_NME_TYPE, this.normalizedAttrubiteTypeCount++, _attrColumns.length);
            this.normalizedAttributeTypeMap.put(DEFAULT_NME_TYPE, this.defaultNormalizedAttributeType_);
        }

        for (int i = 0; i < _attrColumns.length; ++i) {
            if (_attrColumns[i][0] == null) {
                throw new IllegalArgumentException("Attribute name is empty/null in \"" + _attrColumns[i] + "\" value");
            }

            if (_attrColumns[i][1] == null) {
                throw new IllegalArgumentException("Attribute type is empty/null in \"" + _attrColumns[i] + "\" value");
            }

            this.addAttribute(_attrColumns[i][0], _attrColumns[i][1], _attrColumns[i][2]);
        }

    }

    public void updateContextSchema(String[][] _attrColumns) throws IllegalArgumentException {
        List<String> attrNameList = new ArrayList();
        List<String> attrTypeList = new ArrayList();
        List<String> attrValueList = new ArrayList();
        this.collectNewAttributes(_attrColumns, attrNameList, attrTypeList, attrValueList);

        for (int i = 0; i < attrNameList.size(); ++i) {
            this.addAttribute(attrNameList.get(i), attrTypeList.get(i), attrValueList.get(i));
        }

    }

    private void collectNewAttributes(String[][] _attrColumns, List<String> _attrNameList, List<String> _attrTypeList, List<String> _attrValueList) throws IllegalArgumentException {
        this.readLock.lock();
        try {
            for (int i = 0; i < _attrColumns.length; ++i) {
                if (_attrColumns[i][0] == null && _attrColumns[i][1] == null) {
                    throw new IllegalArgumentException("Null/Empty Values in ContextSchema are not allowed, ignore it.");
                }

                if (!this.attributeNameMap.containsKey(_attrColumns[i][0])) {
                    _attrNameList.add(_attrColumns[i][0]);
                    _attrTypeList.add(_attrColumns[i][1]);
                    _attrValueList.add(_attrColumns[i][2]);
                }
            }
        } finally {
            this.readLock.unlock();
        }
    }

    public boolean initialized() {
        this.readLock.lock();

        boolean tempInitFlag;
        try {
            tempInitFlag = this.initialized;
        } finally {
            this.readLock.unlock();
        }

        return tempInitFlag;
    }

    public void addAttribute(String _attrName, String _attrType) {
        this.addAttribute(_attrName, _attrType, (String) null);
    }

    public void addAttribute(String _attrName, String _attrType, String _attrValue) {
        this.writeLock.lock();
        try {
            int tempTypeId;
            int tempPrimitiveType;
            try {
                tempTypeId = this.getTypeID(_attrType);
                if (tempTypeId == -1) {
                    tempTypeId = this.addType(_attrType);
                }

                tempPrimitiveType = this.getPrimitiveType(_attrType);
                if (tempPrimitiveType == -1) {
                }
            } catch (ClassNotFoundException ex) {
                log.warn("ContextSchema-Attribute Class not found for [{}]", new Object[]{_attrName, _attrType});
                return;
            }

            AttributeInfo tempAttrInfo = this.getAttribute(_attrName, false);
            if (tempAttrInfo == null) {
                tempAttrInfo = new AttributeInfo(tempTypeId, tempPrimitiveType, _attrName, this.attributeCount++, _attrType, _attrValue);
                this.attributeNameMap.put(_attrName.toLowerCase(), tempAttrInfo);
                this.attributeList.add(tempAttrInfo);
                this.initialized = true;
            } else if (_attrType.equals(tempAttrInfo.getClassName())) {
                log.debug("Attribute[{}] already exists in the context schema.", _attrName);
                this.initialized = true;
            } else {
                log.info("ContextSchema-Going to add new attr.", new Object[]{_attrName, tempAttrInfo.getClassName(), _attrType});
                tempAttrInfo = new AttributeInfo(tempTypeId, tempPrimitiveType, _attrName, tempAttrInfo.getIndex(), _attrType, _attrValue);
                this.attributeNameMap.put(_attrName.toLowerCase(), tempAttrInfo);
                this.attributeList.set(tempAttrInfo.getIndex(), tempAttrInfo);
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Add type with the type class.
     *
     * @param _typeClass
     * @return
     */
    protected int addType(String _typeClass) {
        this.writeLock.lock();
        try {
            Attribute tempAttr;
            try {
                tempAttr = (Attribute) Class.forName(_typeClass).newInstance();
                tempAttr.setType(this.num_types_);
            } catch (Throwable ex) {
                log.warn("ContextSchemaMsg- Add Type[] failed.", _typeClass);
                return -1;
            }
            this.typeList.add(tempAttr);
            this.num_types_++;
            return this.num_types_ - 1;
        } finally {
            this.writeLock.unlock();
        }
    }

    public AttributeInfo getAttribute(String _attrName) {
        return this.getAttribute(_attrName, true);
    }

    private AttributeInfo getAttribute(String _attrName, boolean _createFlag) {
        Object tempObj = null;
        this.readLock.lock();

        try {
            tempObj = this.attributeNameMap.get(_attrName.toLowerCase());
        } finally {
            this.readLock.unlock();
        }

        if (tempObj == null) {
            return null;
        } else {
            AttributeInfo var4 = (AttributeInfo) tempObj;
            if (_createFlag && !this.defaultNormalizedAttributeType_.isMember(var4.getIndex())) {
                this.defaultNormalizedAttributeType_.addAttribute(var4);
            }

            return var4;
        }
    }

    /**
     * Get Attribute by Index.
     *
     * @param _attrIndex
     * @return
     */
    public AttributeInfo getAttribute(int _attrIndex) {
        this.readLock.lock();
        AttributeInfo tempAttrInfo;
        try {
            tempAttrInfo = (AttributeInfo) this.attributeList.get(_attrIndex);
        } finally {
            this.readLock.unlock();
        }

        return tempAttrInfo;
    }

    /**
     * Get Attribute Info array by attribute names' array.
     *
     * @param _attrNames
     * @return
     */
    public AttributeInfo[] getAttributes(String[] _attrNames) {
        this.readLock.lock();

        AttributeInfo[] tempAttrInfoArray;
        try {
            AttributeInfo[] tempInfoArray = new AttributeInfo[_attrNames.length];

            for (int i = 0; i < tempInfoArray.length; ++i) {
                AttributeInfo tempInfo = this.getAttribute(_attrNames[i]);
                if (tempInfo == null) {
                    return null;
                }
                tempInfoArray[i] = tempInfo;
            }
            tempAttrInfoArray = tempInfoArray;
        } finally {
            this.readLock.unlock();
        }

        return tempAttrInfoArray;
    }

    public Enumeration getAttributes() {
        this.readLock.lock();
        Enumeration tempAttrEnum;
        try {
            tempAttrEnum = new Enumeration() {
                private Iterator it_;
                private int numAttrs_;

                {
                    this.it_ = ContextSchema.this.attributeNameMap.values().iterator();
                    this.numAttrs_ = ContextSchema.this.attributeList.size();
                }

                @Override
                public boolean hasMoreElements() {
                    ContextSchema.this.readLock.lock();

                    boolean tempHasMoreFlag;
                    try {
                        if (this.numAttrs_ != ContextSchema.this.attributeList.size()) {
                            throw new ConcurrentModificationException();
                        }

                        tempHasMoreFlag = this.it_.hasNext();
                    } finally {
                        ContextSchema.this.readLock.unlock();
                    }

                    return tempHasMoreFlag;
                }

                @Override
                public Object nextElement() {
                    ContextSchema.this.readLock.lock();
                    Object tempNextObj;
                    try {
                        if (this.numAttrs_ != ContextSchema.this.attributeList.size()) {
                            throw new ConcurrentModificationException();
                        }
                        tempNextObj = this.it_.next();
                    } finally {
                        ContextSchema.this.readLock.unlock();
                    }
                    return tempNextObj;
                }
            };
        } finally {
            this.readLock.unlock();
        }

        return tempAttrEnum;
    }

    public int getTypeID(String _typeName) throws ClassNotFoundException {
        this.readLock.lock();
        try {
            for (int i = 0; i < this.typeList.size(); ++i) {
                if (_typeName.equals(this.typeList.get(i).getClass().getName())) {
                    return i + 1;
                }
            }
            return -1;
        } finally {
            this.readLock.unlock();
        }
    }

    public int getPrimitiveType(String _typeName) throws ClassNotFoundException {
        this.readLock.lock();
        try {
            for (int i = 0; i < this.typeList.size(); ++i) {
                if (Class.forName(_typeName).isInstance(this.typeList.get(i))) {
                    int tempPrimitiveType = ((Attribute) this.typeList.get(i)).getPrimitiveType();
                    return tempPrimitiveType;
                }
            }
        } finally {
            this.readLock.unlock();
        }
        return -1;
    }

    public Attribute newAttributeInstance(int _index) {
        Attribute tempAttr = null;

        try {
            AttributeInfo tempAttrInfo = this.getAttribute(_index);
            if (tempAttrInfo == null) {
                log.warn("ContextSchemaMsg- Did not get [{}]'s attribute info.", _index);
                return null;
            }

            tempAttr = tempAttrInfo.newAttributeInstance();
        } catch (Exception ex) {
            log.debug("Cannot get new instance for index: {}, exception: {}", _index, ex);
        }

        return tempAttr;
    }

    public Attribute newAttributeInstance(String _typeName) {
        Attribute tempAttr = null;
        try {
            AttributeInfo tempAttrInfo = this.getAttribute(_typeName);
            if (tempAttrInfo == null) {
                log.error("ContextSchemaMsg-Did not get [{}]'s attribute.", _typeName);
                return null;
            }
            tempAttr = tempAttrInfo.newAttributeInstance();
        } catch (Exception var4) {
            log.debug("Cannot get new instance for name: {}, exception: {}", _typeName, var4);
        }

        return tempAttr;
    }

    private int getAttributeIndex(String _typeName) throws IllegalArgumentException {
        AttributeInfo tempAttrInfo = this.getAttribute(_typeName);
        return tempAttrInfo == null ? -1 : tempAttrInfo.getIndex();
    }

    public int getAttributeIndex(String _attrName, String var2) throws IllegalArgumentException {
        AttributeInfo tempAttrInfo = this.getAttribute(_attrName);
        if (tempAttrInfo == null) {
            throw new IllegalArgumentException(var2 + " - Unable to find attribute '" + _attrName + "' in the NME Schema.");
        } else {
            return tempAttrInfo.getIndex();
        }
    }

    public int[] getAttributeIndexes(String[] _attrNames, String var2) throws IllegalArgumentException {
        this.readLock.lock();

        int[] tempAttrIndexes;
        try {
            int[] tempIndexes = new int[_attrNames.length];

            for (int i = 0; i < _attrNames.length; ++i) {
                tempIndexes[i] = this.getAttributeIndex(_attrNames[i]);
                if (tempIndexes[i] == -1) {
                    throw new IllegalArgumentException(var2 + "the NME attribute '" + _attrNames[i] + "' is not defined in the NME Schema.");
                }
            }

            tempAttrIndexes = tempIndexes;
        } finally {
            this.readLock.unlock();
        }

        return tempAttrIndexes;
    }

    public static ContextSchema getInstance() {
        if (instance == null) {
            instance = new ContextSchema();
        }

        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    /**
     * Get Attributes' size.
     *
     * @return
     */
    public int size() {
        this.readLock.lock();

        int tempSize;
        try {
            tempSize = this.attributeList.size();
        } finally {
            this.readLock.lock();
        }

        return tempSize;
    }

    /**
     * Create NE Type with NE Type name.
     *
     * @param _neTypeName
     * @return
     */
    public NormalizedAttributeType createNormalizedEventType(String _neTypeName) {
        this.writeLock.lock();

        NormalizedAttributeType tempNEType;
        try {
            if (_neTypeName == null) {
                throw new IllegalArgumentException("The NormalizedAttributeType name cannot be null");
            }

            NormalizedAttributeType tempType = (NormalizedAttributeType) this.normalizedAttributeTypeMap.get(_neTypeName);
            if (tempType == null) {
                tempType = new NormalizedAttributeType(_neTypeName, this.normalizedAttrubiteTypeCount++, this.attributeNameMap.size());
                this.normalizedAttributeTypeMap.put(_neTypeName, tempType);
                tempNEType = tempType;
                return tempNEType;
            }

            tempNEType = tempType;
        } finally {
            this.writeLock.unlock();
        }

        return tempNEType;
    }

    /**
     * Create NE Type with NE Type Name and add the attributes.
     *
     * @param _neTypeName
     * @param _attrNames
     * @return
     */
    public NormalizedAttributeType createNormalizedEventType(String _neTypeName, String[] _attrNames) {
        NormalizedAttributeType tempNEType = this.createNormalizedEventType(_neTypeName);
        for (int i = 0; i < _attrNames.length; ++i) {
            tempNEType.addAttribute(_attrNames[i]);
        }
        return tempNEType;
    }

    public NormalizedAttributeType getNormalizedEventType(String _neTypeName) {
        this.readLock.lock();

        NormalizedAttributeType tempNEType;
        try {
            NormalizedAttributeType tempType = (NormalizedAttributeType) this.normalizedAttributeTypeMap.get(_neTypeName);
            tempNEType = tempType;
        } finally {
            this.readLock.unlock();
        }

        return tempNEType;
    }

    public int[] getNormalizedEventTypeIndexes(String _neTypeName) {
        this.readLock.lock();
        int[] tempAttrIndexes;
        try {
            NormalizedAttributeType tempNEType = (NormalizedAttributeType) this.normalizedAttributeTypeMap.get(_neTypeName);
            tempAttrIndexes = tempNEType.getAttributeIndexes();
        } finally {
            this.readLock.unlock();
        }

        return tempAttrIndexes;
    }
}
