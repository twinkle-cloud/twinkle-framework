package com.twinkle.framework.context;

import com.alibaba.fastjson.JSONArray;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.context.model.DefaultNormalizedAttributeType;
import com.twinkle.framework.api.context.NormalizedAttributeType;
import com.twinkle.framework.context.model.DefaultAttributeInfo;
import com.twinkle.framework.core.lang.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
public class PrimitiveAttributeSchema {
    protected boolean initialized = false;
    private int numTypes;
    protected int attributeCount = 0;
    protected int normalizedAttributeTypeCount;
    private static volatile PrimitiveAttributeSchema instance;
    protected Map<String, AttributeInfo> attributeNameMap;
    protected List<AttributeInfo> attributeInfoList;
    protected List<Attribute> attributeList;
    protected Map<String, NormalizedAttributeType> normalizedAttributeTypeMap = new HashMap<>();
    protected NormalizedAttributeType defaultNormalizedAttributeType;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock;
    private Lock writeLock;
    public static final String DEFAULT_STRUCT_ATTRIBUTE_TYPE = "%DefaultNormalizedEventType";

    private PrimitiveAttributeSchema() {
        this.readLock = this.readWriteLock.readLock();
        this.writeLock = this.readWriteLock.writeLock();
        this.attributeNameMap = new HashMap<>();
        this.attributeInfoList = new ArrayList<>();
        this.attributeList = new ArrayList<>();
        this.numTypes = 1;
    }

    public void configure(JSONArray _attrColumns) throws IllegalArgumentException {
        if (this.defaultNormalizedAttributeType == null) {
            this.defaultNormalizedAttributeType = new DefaultNormalizedAttributeType(DEFAULT_STRUCT_ATTRIBUTE_TYPE, this.normalizedAttributeTypeCount++, _attrColumns.size());
            this.normalizedAttributeTypeMap.put(DEFAULT_STRUCT_ATTRIBUTE_TYPE, this.defaultNormalizedAttributeType);
        }

        for (int i = 0; i < _attrColumns.size(); i++) {
            JSONArray tempArray = _attrColumns.getJSONArray(i);
            String tempAttrName = tempArray.getString(0);
            String tempAttrType = tempArray.getString(1);
            if (StringUtils.isBlank(tempAttrName)) {
                throw new IllegalArgumentException("Attribute name is empty/null in \"" + tempArray + "\" value.");
            }
            if (StringUtils.isBlank(tempAttrType)) {
                throw new IllegalArgumentException("Attribute type is empty/null in \"" + tempArray + "\" value.");
            }
            this.addAttribute(tempAttrName, tempAttrType, tempArray.getString(2));
        }
    }

    public void updatePrimitiveAttributeSchema(String[][] _attrColumns) throws IllegalArgumentException {
        List<String> attrNameList = new ArrayList();
        List<String> attrTypeList = new ArrayList();
        List<String> attrExtraInfoList = new ArrayList();
        this.collectNewAttributes(_attrColumns, attrNameList, attrTypeList, attrExtraInfoList);

        for (int i = 0; i < attrNameList.size(); i++) {
            this.addAttribute(attrNameList.get(i), attrTypeList.get(i), attrExtraInfoList.get(i));
        }
    }

    private void collectNewAttributes(String[][] _attrColumns, List<String> _attrNameList, List<String> _attrTypeList, List<String> _attrExtraInfoList) throws IllegalArgumentException {
        this.readLock.lock();
        try {
            for (int i = 0; i < _attrColumns.length; i++) {
                if (_attrColumns[i][0] == null && _attrColumns[i][1] == null) {
                    throw new IllegalArgumentException("Null/Empty Values in PrimitiveAttributeSchema are not allowed, ignore it.");
                }

                if (!this.attributeNameMap.containsKey(_attrColumns[i][0])) {
                    _attrNameList.add(_attrColumns[i][0]);
                    _attrTypeList.add(_attrColumns[i][1]);
                    _attrExtraInfoList.add(_attrColumns[i][2]);
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

    /**
     * Add attribute into the schema.
     *
     * @param _attrName
     * @param _attrType
     */
    public void addAttribute(String _attrName, String _attrType) {
        this.addAttribute(_attrName, _attrType, null);
    }

    /**
     * Add attribute into the schema with attribute descriptor.
     *
     * @param _attrName
     * @param _attrType
     * @param _extraInfo
     */
    public void addAttribute(String _attrName, String _attrType, String _extraInfo) {
        this.writeLock.lock();
        try {
            int tempTypeId;
            int tempPrimitiveType;
            try {
                tempTypeId = this.getTypeIndex(_attrType);
                if (tempTypeId == -1) {
                    tempTypeId = this.addType(_attrType);
                }

                tempPrimitiveType = this.getPrimitiveType(_attrType);
                if (tempPrimitiveType == -1) {
                }
            } catch (ClassNotFoundException ex) {
                log.warn("PrimitiveAttributeSchema-Attribute Class not found for [{}]", new Object[]{_attrName, _attrType});
                return;
            }

            AttributeInfo tempAttrInfo = this.getAttribute(_attrName, false);
            if (tempAttrInfo == null) {
                tempAttrInfo = new DefaultAttributeInfo(tempTypeId, tempPrimitiveType, _attrName, this.attributeCount++, _attrType, _extraInfo);

                this.attributeNameMap.put(_attrName.toLowerCase(), tempAttrInfo);
                this.attributeInfoList.add(tempAttrInfo);
                this.initialized = true;
            } else if (_attrType.equals(tempAttrInfo.getClassName())) {
                log.debug("Attribute[{}] already exists in the context schema.", _attrName);
                this.initialized = true;
            } else {
                log.info("PrimitiveAttributeSchema-Going to add new attr.", new Object[]{_attrName, tempAttrInfo.getClassName(), _attrType});
                tempAttrInfo = new DefaultAttributeInfo(tempTypeId, tempPrimitiveType, _attrName, tempAttrInfo.getIndex(), _attrType, _extraInfo);
                this.attributeNameMap.put(_attrName.toLowerCase(), tempAttrInfo);
                this.attributeInfoList.set(tempAttrInfo.getIndex(), tempAttrInfo);
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
                tempAttr.setTypeIndex(this.numTypes);
            } catch (Throwable ex) {
                log.warn("PrimitiveAttributeSchemaMsg- Add Type[] failed.", _typeClass);
                return -1;
            }
            this.attributeList.add(tempAttr);
            this.numTypes++;
            return this.numTypes - 1;
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Get the AttributeInfo by Attribute Name.
     *
     * @param _attrName
     * @return
     */
    public AttributeInfo getAttribute(String _attrName) {
        return this.getAttribute(_attrName, true);
    }

    /**
     * Get the AttributeInfo by Attribute Name.
     * if the attribute does not exists, then create it.
     *
     * @param _attrName
     * @param _createFlag
     * @return
     */
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
        }
        AttributeInfo tempAttrInfo = (AttributeInfo) tempObj;
        if (_createFlag && !this.defaultNormalizedAttributeType.isMember(tempAttrInfo.getIndex())) {
            this.defaultNormalizedAttributeType.addAttribute(tempAttrInfo);
        }

        return tempAttrInfo;
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
            tempAttrInfo = this.attributeInfoList.get(_attrIndex);
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

            for (int i = 0; i < tempInfoArray.length; i++) {
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

    /**
     * Get attributes' enumeration.
     *
     * @return
     */
    public Enumeration getAttributes() {
        this.readLock.lock();
        Enumeration tempAttrEnum;
        try {
            tempAttrEnum = new Enumeration() {
                private Iterator it;
                private int numAttrs;

                {
                    this.it = PrimitiveAttributeSchema.this.attributeNameMap.values().iterator();
                    this.numAttrs = PrimitiveAttributeSchema.this.attributeInfoList.size();
                }

                @Override
                public boolean hasMoreElements() {
                    PrimitiveAttributeSchema.this.readLock.lock();
                    boolean tempHasMoreFlag;
                    try {
                        if (this.numAttrs != PrimitiveAttributeSchema.this.attributeInfoList.size()) {
                            throw new ConcurrentModificationException();
                        }
                        tempHasMoreFlag = this.it.hasNext();
                    } finally {
                        PrimitiveAttributeSchema.this.readLock.unlock();
                    }

                    return tempHasMoreFlag;
                }

                @Override
                public Object nextElement() {
                    PrimitiveAttributeSchema.this.readLock.lock();
                    Object tempNextObj;
                    try {
                        if (this.numAttrs != PrimitiveAttributeSchema.this.attributeInfoList.size()) {
                            throw new ConcurrentModificationException();
                        }
                        tempNextObj = this.it.next();
                    } finally {
                        PrimitiveAttributeSchema.this.readLock.unlock();
                    }
                    return tempNextObj;
                }
            };
        } finally {
            this.readLock.unlock();
        }

        return tempAttrEnum;
    }

    /**
     * Get type's Index in the schema.
     *
     * @param _typeName
     * @return
     * @throws ClassNotFoundException
     */
    public int getTypeIndex(String _typeName) throws ClassNotFoundException {
        this.readLock.lock();
        try {
            for (int i = 0; i < this.attributeList.size(); i++) {
                if (_typeName.equals(this.attributeList.get(i).getClass().getName())) {
                    return i + 1;
                }
            }
            return -1;
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * Get base type for some attribute.
     *
     * @param _typeName
     * @return
     * @throws ClassNotFoundException
     */
    public int getPrimitiveType(String _typeName) throws ClassNotFoundException {
        this.readLock.lock();
        try {
            for (int i = 0; i < this.attributeList.size(); i++) {
                if (Class.forName(_typeName).isInstance(this.attributeList.get(i))) {
                    int tempPrimitiveType = (this.attributeList.get(i)).getPrimitiveType();
                    return tempPrimitiveType;
                }
            }
        } finally {
            this.readLock.unlock();
        }
        return -1;
    }

    /**
     * Build a new instance with given type index.
     *
     * @param _index
     * @return
     */
    public Attribute newAttributeInstance(int _index) {
        Attribute tempAttr = null;

        try {
            AttributeInfo tempAttrInfo = this.getAttribute(_index);
            if (tempAttrInfo == null) {
                log.warn("PrimitiveAttributeSchemaMsg- Did not get [{}]'s attribute info.", _index);
                return null;
            }
            tempAttr = tempAttrInfo.newAttributeInstance();
        } catch (Exception ex) {
            log.debug("Cannot get new instance for index: {}, exception: {}", _index, ex);
        }

        return tempAttr;
    }

    /**
     * Build a new instance with given type name.
     *
     * @param _typeName
     * @return
     */
    public Attribute newAttributeInstance(String _typeName) {
        Attribute tempAttr = null;
        try {
            AttributeInfo tempAttrInfo = this.getAttribute(_typeName);
            if (tempAttrInfo == null) {
                log.error("PrimitiveAttributeSchemaMsg-Did not get [{}]'s attribute.", _typeName);
                return null;
            }
            tempAttr = tempAttrInfo.newAttributeInstance();
        } catch (Exception e) {
            log.debug("Cannot get new instance for name: {}, exception: {}", _typeName, e);
        }

        return tempAttr;
    }

    private int getAttributeIndex(String _attrName) throws IllegalArgumentException {
        AttributeInfo tempAttrInfo = this.getAttribute(_attrName);
        return tempAttrInfo == null ? -1 : tempAttrInfo.getIndex();
    }

    public int getAttributeIndex(String _attrName, String _tag) throws IllegalArgumentException {
        AttributeInfo tempAttrInfo = this.getAttribute(_attrName);
        if (tempAttrInfo == null) {
            throw new IllegalArgumentException(_tag + " - Unable to find attribute '" + _attrName + "' in the Context Schema.");
        } else {
            return tempAttrInfo.getIndex();
        }
    }

    public int[] getAttributeIndexes(String[] _attrNames, String _config) throws IllegalArgumentException {
        this.readLock.lock();
        int[] tempAttrIndexes;
        try {
            int[] tempIndexes = new int[_attrNames.length];

            for (int i = 0; i < _attrNames.length; i++) {
                tempIndexes[i] = this.getAttributeIndex(_attrNames[i]);
                if (tempIndexes[i] == -1) {
                    throw new IllegalArgumentException(_config + "the attribute '" + _attrNames[i] + "' is not defined in the Context Schema.");
                }
            }

            tempAttrIndexes = tempIndexes;
        } finally {
            this.readLock.unlock();
        }

        return tempAttrIndexes;
    }

    public static PrimitiveAttributeSchema getInstance() {
        if (instance == null) {
            instance = new PrimitiveAttributeSchema();
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
            tempSize = this.attributeInfoList.size();
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
    public DefaultNormalizedAttributeType createNormalizedEventType(String _neTypeName) {
        this.writeLock.lock();

        DefaultNormalizedAttributeType tempNEType;
        try {
            if (_neTypeName == null) {
                throw new IllegalArgumentException("The NormalizedAttributeType name cannot be null");
            }

            DefaultNormalizedAttributeType tempType = (DefaultNormalizedAttributeType) this.normalizedAttributeTypeMap.get(_neTypeName);
            if (tempType == null) {
                tempType = new DefaultNormalizedAttributeType(_neTypeName, this.normalizedAttributeTypeCount++, this.attributeNameMap.size());
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
    public DefaultNormalizedAttributeType createNormalizedEventType(String _neTypeName, String[] _attrNames) {
        DefaultNormalizedAttributeType tempNEType = this.createNormalizedEventType(_neTypeName);
        for (int i = 0; i < _attrNames.length; i++) {
            tempNEType.addAttribute(_attrNames[i]);
        }
        return tempNEType;
    }

    public DefaultNormalizedAttributeType getNormalizedEventType(String _neTypeName) {
        this.readLock.lock();

        DefaultNormalizedAttributeType tempNEType;
        try {
            DefaultNormalizedAttributeType tempType = (DefaultNormalizedAttributeType) this.normalizedAttributeTypeMap.get(_neTypeName);
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
            DefaultNormalizedAttributeType tempNEType = (DefaultNormalizedAttributeType) this.normalizedAttributeTypeMap.get(_neTypeName);
            tempAttrIndexes = tempNEType.getAttributeIndexes();
        } finally {
            this.readLock.unlock();
        }

        return tempAttrIndexes;
    }

    @Data
    @AllArgsConstructor
    private static class ValueClassPair {
        private Class<?> valueClass;
        private String description;
    }
}
