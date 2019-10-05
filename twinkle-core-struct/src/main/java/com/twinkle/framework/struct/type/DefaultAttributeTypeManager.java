package com.twinkle.framework.struct.type;

import com.twinkle.framework.struct.error.TypeAlreadyExistsException;
import com.twinkle.framework.struct.error.TypeNotFoundException;
import com.twinkle.framework.core.lang.util.ImmutableIterator;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/31/19 2:15 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DefaultAttributeTypeManager implements AttributeTypeManager {
    private static final DefaultAttributeTypeManager INTERNAL_TYPE_MGR = new DefaultAttributeTypeManager();
    private final Map<String, AttributeType> primitiveTypeMap;
    private final Map<String, AttributeType> typeMap;
    protected final Lock readLock;
    protected final Lock writeLock;
    private DefaultAttributeTypeManager parentTypeManager;

    static {
        INTERNAL_TYPE_MGR.primitiveTypeMap.putAll(getDefaultPrimitives());
        INTERNAL_TYPE_MGR.typeMap.putAll(INTERNAL_TYPE_MGR.primitiveTypeMap);
        INTERNAL_TYPE_MGR.typeMap.putAll(getDefaultAliases());
    }
    public DefaultAttributeTypeManager() {
        this(INTERNAL_TYPE_MGR);
    }

    protected DefaultAttributeTypeManager(DefaultAttributeTypeManager _parentManager) {
        this.primitiveTypeMap = new HashMap(20);
        this.typeMap = new HashMap(16);
        this.parentTypeManager = null;
        this.parentTypeManager = _parentManager;
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.readLock();
    }

    protected static DefaultAttributeTypeManager getInternalTypeManager() {
        return INTERNAL_TYPE_MGR;
    }

    protected static Map<String, AttributeType> getDefaultPrimitives() {
        Map<String, AttributeType> tempPrimitiveTypeMap = new HashMap();
        tempPrimitiveTypeMap.put("byte", PrimitiveType.BYTE);
        tempPrimitiveTypeMap.put("short", PrimitiveType.SHORT);
        tempPrimitiveTypeMap.put("int", PrimitiveType.INT);
        tempPrimitiveTypeMap.put("long", PrimitiveType.LONG);
        tempPrimitiveTypeMap.put("char", PrimitiveType.CHAR);
        tempPrimitiveTypeMap.put("boolean", PrimitiveType.BOOLEAN);
        tempPrimitiveTypeMap.put("float", PrimitiveType.FLOAT);
        tempPrimitiveTypeMap.put("double", PrimitiveType.DOUBLE);
        tempPrimitiveTypeMap.put("string", StringType.STRING);
        tempPrimitiveTypeMap.put("byte[]", ArrayType.BYTE_ARRAY);
        tempPrimitiveTypeMap.put("short[]", ArrayType.SHORT_ARRAY);
        tempPrimitiveTypeMap.put("int[]", ArrayType.INT_ARRAY);
        tempPrimitiveTypeMap.put("long[]", ArrayType.LONG_ARRAY);
        tempPrimitiveTypeMap.put("char[]", ArrayType.CHAR_ARRAY);
        tempPrimitiveTypeMap.put("boolean[]", ArrayType.BOOLEAN_ARRAY);
        tempPrimitiveTypeMap.put("float[]", ArrayType.FLOAT_ARRAY);
        tempPrimitiveTypeMap.put("double[]", ArrayType.DOUBLE_ARRAY);
        tempPrimitiveTypeMap.put("string[]", ArrayType.STRING_ARRAY);
        return tempPrimitiveTypeMap;
    }

    protected static Map<String, AttributeType> getDefaultAliases() {
        Map<String, AttributeType> tempAliasMap = new HashMap();
        tempAliasMap.put("TextString", new AliasStructType("TextString", ArrayType.CHAR_ARRAY));
        tempAliasMap.put("ASCIIString", new AliasStructType("ASCIIString", ArrayType.BYTE_ARRAY));
        tempAliasMap.put("Time", new AliasStructType("Time", PrimitiveType.INT));
        tempAliasMap.put("IPAddress", new AliasStructType("IPAddress", PrimitiveType.INT));
        tempAliasMap.put("UUID", new AliasStructType("UUID", ArrayType.BYTE_ARRAY));
        tempAliasMap.put("URL", new AliasStructType("URL", ArrayType.CHAR_ARRAY));
        return tempAliasMap;
    }

    @Override
    public int size() {
        this.readLock.lock();

        int tempSize;
        try {
            int tempMapSize = this.typeMap.size();
            if (this.parentTypeManager != null) {
                tempMapSize += this.parentTypeManager.size();
            }
            tempSize = tempMapSize;
        } finally {
            this.readLock.unlock();
        }

        return tempSize;
    }
    @Override
    public Iterator<String> getTypeNames() {
        this.readLock.lock();
        ImmutableIterator<String> tempIterator;
        try {
            Set<String> tempKeySet;
            if (this.parentTypeManager == null) {
                tempKeySet = this.typeMap.keySet();
            } else {
                tempKeySet = new HashSet<>();
                tempKeySet.addAll(this.typeMap.keySet());
                Iterator<String> tempParentIterator = this.parentTypeManager.getTypeNames();

                while(tempParentIterator.hasNext()) {
                    tempKeySet.add(tempParentIterator.next());
                }
            }
            tempIterator = new ImmutableIterator(tempKeySet.iterator());
        } finally {
            this.readLock.unlock();
        }

        return tempIterator;
    }

    /**
     * Get the primitive type names.
     *
     * @return
     */
    public Iterator<String> getPrimitiveTypeNames() {
        this.readLock.lock();
        ImmutableIterator<String> tempIterator;
        try {
            Set<String> tempKeySet;
            if (this.parentTypeManager == null) {
                tempKeySet = this.primitiveTypeMap.keySet();
            } else {
                tempKeySet = new HashSet<>();
                tempKeySet.addAll(this.primitiveTypeMap.keySet());
                Iterator<String> tempParentIterator = this.parentTypeManager.getPrimitiveTypeNames();
                while(tempParentIterator.hasNext()) {
                    tempKeySet.add(tempParentIterator.next());
                }
            }
            tempIterator = new ImmutableIterator(tempKeySet.iterator());
        } finally {
            this.readLock.unlock();
        }

        return tempIterator;
    }
    @Override
    public boolean hasTypeName(String _typeName) {
        this.readLock.lock();

        try {
            if (this.typeMap.containsKey(_typeName)) {
                return true;
            }
            if (this.parentTypeManager != null) {
                boolean tempExistFlag = this.parentTypeManager.hasTypeName(_typeName);
                return tempExistFlag;
            }
        } finally {
            this.readLock.unlock();
        }

        return false;
    }
    @Override
    public Iterator<String> getUserDefinedTypeNames() {
        this.readLock.lock();

        Iterator<String> tempIterator;
        try {
            Iterator<String> tempTypeNameItr = this.getTypeNames();
            List<String> tempUserDefinedTypeNameList = new ArrayList<>();
            while(tempTypeNameItr.hasNext()) {
                String tempTypeName = tempTypeNameItr.next();

                try {
                    if (this.getType(tempTypeName, false) instanceof AliasStructType) {
                        tempUserDefinedTypeNameList.add(tempTypeName);
                    }
                } catch (TypeNotFoundException e) {
                }
            }
            tempIterator = tempUserDefinedTypeNameList.iterator();
        } finally {
            this.readLock.unlock();
        }
        return tempIterator;
    }
    @Override
    public AttributeType getType(String _typeName) throws TypeNotFoundException {
        return this.getType(_typeName, true);
    }
    @Override
    public AttributeType getType(String _typeName, boolean _checkAliasFlag) throws TypeNotFoundException {
        this.readLock.lock();

        AttributeType tempResultType;
        try {
            AttributeType tempType = this.typeMap.get(_typeName);
            if (tempType == null) {
                if (this.parentTypeManager == null) {
                    throw new TypeNotFoundException(_typeName);
                }
                tempType = this.parentTypeManager.getType(_typeName, _checkAliasFlag);
            }

            if (_checkAliasFlag) {
                while(tempType instanceof AliasStructType) {
                    tempType = ((AliasStructType)tempType).getSourceType();
                }
            }
            tempResultType = tempType;
        } finally {
            this.readLock.unlock();
        }

        return tempResultType;
    }
    @Override
    public void addType(String _typeName, AttributeType _type) throws TypeAlreadyExistsException {
        this.writeLock.lock();
        try {
            if (this.typeMap.containsKey(_typeName)) {
                throw new TypeAlreadyExistsException(_typeName);
            }

            AttributeType tempStructType = _type;
            if (!_typeName.equals(_type.getName())) {
                tempStructType = new AliasStructType(_typeName, _type);
            }

            this.typeMap.put(_typeName, tempStructType);
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Remove type by the given name.
     *
     * @param _typeName
     * @throws TypeNotFoundException
     */
    protected void removeType(String _typeName) throws TypeNotFoundException {
        this.writeLock.lock();

        try {
            if (!this.hasTypeName(_typeName)) {
                throw new TypeNotFoundException("Type [" + _typeName + "] does not exist.");
            }

            if (this.primitiveTypeMap.containsKey(_typeName)) {
                throw new TypeNotFoundException("Type [" + _typeName + "] is a Primitive type, can not be removed.");
            }

            this.typeMap.remove(_typeName);
        } finally {
            this.writeLock.unlock();
        }
    }
}
