package com.twinkle.framework.struct.type;

import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.struct.asm.descriptor.DefaultSAAttributeDescriptorImpl;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.core.lang.util.ImmutableIterator;
import com.twinkle.framework.struct.error.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 9:20 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DefaultStructType implements BeanStructType, Cloneable {
    private String name;
    protected BeanTypeDescriptor typeDescriptor;
    private String namespace;
    protected StructTypeManager typeManager;
    private String qualifiedName;
    private int hashCode;
    protected Map<String, SAAttributeDescriptor> attributes;
    private boolean published;
    protected boolean mutable;
    protected final Lock readLock;
    protected final Lock writeLock;

    public DefaultStructType(String _name) {
        this(_name, null);
    }

    public DefaultStructType(String _name, BeanTypeDescriptor _typeDescriptor) {
        this.namespace = null;
        this.typeManager = null;
        this.qualifiedName = null;
        this.hashCode = 0;
        this.attributes = this.initAttributes();
        this.published = false;
        this.mutable = false;
        this.name = _name;
        this.typeDescriptor = _typeDescriptor;
        ReentrantReadWriteLock tempWriteLock = new ReentrantReadWriteLock();
        this.readLock = tempWriteLock.readLock();
        this.writeLock = tempWriteLock.readLock();
    }

    protected SAAttributeDescriptor createNewAttributeDescriptor(String _attrName, String _attrTypeName, boolean _optional) {
        return new DefaultSAAttributeDescriptorImpl(_attrName, _attrTypeName, this.typeManager, _optional);
    }

    protected Map<String, SAAttributeDescriptor> initAttributes() {
        return new LinkedHashMap();
    }

    protected Map<String, SAAttributeDescriptor> initAttributes(Map<String, SAAttributeDescriptor> _attrMap) {
        return new LinkedHashMap(_attrMap);
    }
    @Override
    public boolean isPrimitiveType() {
        return false;
    }
    @Override
    public boolean isArrayType() {
        return false;
    }
    @Override
    public boolean isStructType() {
        return true;
    }
    @Override
    public boolean isStringType() {
        return false;
    }
    @Override
    public int getID() {
        return STRUCT_ID;
    }

    /**
     * Update the namespace and name of this struct attribute.
     *
     * @param _name
     * @param _namespace
     */
    protected void setName(String _name, String _namespace) {
        this.writeLock.lock();

        try {
            this.name = _name;
            this.setNamespace(_namespace);
        } finally {
            this.writeLock.unlock();
        }

    }

    /**
     * Update the namespace.
     *
     * @param _namespace
     */
    public void setNamespace(String _namespace) {
        this.writeLock.lock();

        try {
            if (!this.mutable && this.isPublished() && this.namespace != null) {
                throw new IllegalStateException("StructAttributeType already published to the namespace " + this.namespace);
            }
            this.namespace = _namespace;
            this.qualifiedName = this.namespace + ":" + this.name;
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Update the struct type manager for this type.
     *
     * @param _typeManager
     */
    public void setTypeManager(StructTypeManager _typeManager) {
        this.writeLock.lock();
        try {
            this.typeManager = _typeManager;
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Publish this struct attribute type.
     */
    public void publish() {
        this.writeLock.lock();

        try {
            this.published = true;
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Unpublish this struct attribute type.
     */
    public void unpublish() {
        this.writeLock.lock();

        try {
            this.published = false;
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Object clone() {
        this.readLock.lock();
        DefaultStructType tempCloneType;
        try {
            DefaultStructType tempType;
            try {
                tempType = (DefaultStructType) super.clone();
                if (!this.isPublished()) {
                    tempType.attributes = this.initAttributes(this.attributes);
                }
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Clone operation failed for " + this.name, e);
            }
            tempCloneType = tempType;
        } finally {
            this.readLock.unlock();
        }
        return tempCloneType;
    }

    @Override
    public String getName() {
        this.readLock.lock();

        try {
            return this.name;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public boolean isPublished() {
        this.readLock.lock();
        try {
            if (!this.mutable) {
                return this.published;
            }
        } finally {
            this.readLock.unlock();
        }
        return false;
    }

    @Override
    public String getNamespace() {
        this.readLock.lock();
        try {
            if (this.namespace == null) {
                throw new IllegalStateException("Type not published.");
            }
            return this.namespace;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public String getQualifiedName() {
        this.readLock.lock();
        try {
            if (this.namespace == null) {
                throw new IllegalStateException("Type not published.");
            }
            return this.qualifiedName;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public StructTypeManager getTypeManager() {
        this.readLock.lock();

        try {
            StructTypeManager tempStructTypeManager = this.typeManager;
            return tempStructTypeManager;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public int size() {
        this.readLock.lock();
        try {
            return this.attributes.size();
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public Iterator<SAAttributeDescriptor> getAttributes() {
        this.readLock.lock();

        ImmutableIterator<SAAttributeDescriptor> tempDescriptorItr;
        try {
            tempDescriptorItr = new ImmutableIterator(this.attributes.values().iterator());
        } finally {
            this.readLock.unlock();
        }
        return tempDescriptorItr;
    }

    @Override
    public SAAttributeDescriptor getAttribute(String _attrName) throws AttributeNotFoundException {
        this.readLock.lock();

        SAAttributeDescriptor tempDescriptor;
        try {
            SAAttributeDescriptor tempDescriptorInMap = this.attributes.get(_attrName);
            if (tempDescriptorInMap == null) {
                throw new AttributeNotFoundException(_attrName);
            }
            tempDescriptor = tempDescriptorInMap;
        } finally {
            this.readLock.unlock();
        }
        return tempDescriptor;
    }

    @Override
    public boolean hasAttribute(String _attrName) {
        this.readLock.lock();

        boolean tempExistsFlag;
        try {
            tempExistsFlag = this.attributes.containsKey(_attrName);
        } finally {
            this.readLock.unlock();
        }

        return tempExistsFlag;
    }

    /**
     * Add an attribute with attribute name, attribute type name, and optional.
     *
     * @param _attrName
     * @param _attrTypeName
     * @param _optional
     * @throws AttributeAlreadyExistsException
     * @throws TypeNotFoundException
     * @throws BadAttributeNameException
     * @throws StructAttributeTypeNotFoundException
     * @throws NamespaceNotFoundException
     * @throws IllegalStateException
     */
    @Override
    public void addAttribute(String _attrName, String _attrTypeName, boolean _optional) throws AttributeAlreadyExistsException, TypeNotFoundException, BadAttributeNameException, StructAttributeTypeNotFoundException, NamespaceNotFoundException, IllegalStateException {
        this.writeLock.lock();

        try {
            if (this.typeManager == null) {
                throw new IllegalStateException("Cannot add attributes into this StructAttributeType, TypeManager is not set yet.");
            }
            if (this.isPublished()) {
                throw new IllegalStateException("Cannot add attributes to a published StructAttributeType.");
            }
            if (this.hasAttribute(_attrName)) {
                throw new AttributeAlreadyExistsException(this, _attrName);
            }
            SAAttributeDescriptor tempDescriptor = this.createNewAttributeDescriptor(_attrName, _attrTypeName, _optional);
            this.attributes.put(_attrName, tempDescriptor);
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Remove the attribute with the given attr name from this struct type .
     *
     * @param _attrName
     * @throws AttributeNotFoundException
     */
    protected void removeAttribute(String _attrName) throws AttributeNotFoundException {
        this.writeLock.lock();

        try {
            if (!this.attributes.containsKey(_attrName)) {
                throw new AttributeNotFoundException(_attrName);
            }

            this.attributes.remove(_attrName);
        } finally {
            this.writeLock.unlock();
        }

    }

    @Override
    public BeanTypeDescriptor getTypeDescriptor() {
        return this.typeDescriptor;
    }

    @Override
    public String toString() {
        this.readLock.lock();

        String tempResultStr;
        try {
            String tempLineSeparator = System.getProperty("line.separator");
            String temp2Spaces = "  ";
            StringBuilder tempBuilder = new StringBuilder(1024);
            if (this.isPublished()) {
                tempBuilder.append(this.namespace);
                tempBuilder.append(':');
            }

            tempBuilder.append(this.name);
            tempBuilder.append(" {");
            tempBuilder.append(tempLineSeparator);
            Iterator<SAAttributeDescriptor> tempAttrDesItr = this.getAttributes();

            while (tempAttrDesItr.hasNext()) {
                SAAttributeDescriptor tempAttrDescItem = tempAttrDesItr.next();
                tempBuilder.append(temp2Spaces);
                if (tempAttrDescItem.isOptional()) {
                    tempBuilder.append("optional ");
                }
                tempBuilder.append(tempAttrDescItem.getTypeName());
                tempBuilder.append(' ');
                tempBuilder.append(tempAttrDescItem.getName());
                tempBuilder.append(';');
                tempBuilder.append(tempLineSeparator);
            }
            tempBuilder.append('}');
            tempResultStr = tempBuilder.toString();
        } finally {
            this.readLock.unlock();
        }
        return tempResultStr;
    }

    @Override
    public boolean equals(Object _obj) {
        if (_obj != null && _obj instanceof DefaultStructType) {
            if (this == _obj) {
                return true;
            } else {
                DefaultStructType tempObj = (DefaultStructType) _obj;
                return System.identityHashCode(this) < System.identityHashCode(tempObj) ? equalsImpl(this, tempObj) : equalsImpl(tempObj, this);
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        this.readLock.lock();
        try {
            int tempCode;
            if (this.hashCode == 0) {
                if (!this.isPublished()) {
                    tempCode = this.name.hashCode();
                    return tempCode;
                }
                tempCode = this.name.length();
                int tempProcessingCode = 0;
                while (true) {
                    if (tempProcessingCode >= tempCode) {
                        tempCode = this.namespace.length();
                        tempProcessingCode = 0;

                        while (true) {
                            if (tempProcessingCode >= tempCode) {
                                break;
                            }

                            this.hashCode = 31 * this.hashCode + this.namespace.charAt(tempProcessingCode);
                            ++tempProcessingCode;
                        }
                    }

                    this.hashCode = 31 * this.hashCode + this.name.charAt(tempProcessingCode);
                    ++tempProcessingCode;
                }
            }

            tempCode = this.hashCode;
            return tempCode;
        } finally {
            this.readLock.unlock();
        }
    }

    /**
     * Judge the given two attribute type equal or not?
     *
     * @param _firstType
     * @param _secondType
     * @return
     */
    private static boolean equalsImpl(DefaultStructType _firstType, DefaultStructType _secondType) {
        _firstType.readLock.lock();

        try {
            _secondType.readLock.lock();

            try {
                if (_firstType.getName().equals(_secondType.getName())) {
                    boolean tempResult = _firstType.isPublished();
                    boolean tempSecondPublished = _secondType.isPublished();
                    if (tempResult != tempSecondPublished) {
                        return false;
                    }

                    if (!_firstType.getNamespace().equals(_secondType.getNamespace())) {
                        return false;
                    }

                    if (tempResult && _firstType.getNamespace().equals(_secondType.getNamespace())) {
                        return true;
                    }

                    if (_firstType.size() != _secondType.size()) {
                        return false;
                    }

                    Iterator<SAAttributeDescriptor> firstTypeAttrItr = _firstType.getAttributes();

                    while (firstTypeAttrItr.hasNext()) {
                        SAAttributeDescriptor tempFistAttrItem = firstTypeAttrItr.next();
                        String tempFistAttrItemName = tempFistAttrItem.getName();
                        if (!_secondType.hasAttribute(tempFistAttrItemName)) {
                            return false;
                        }
                        try {
                            if (!tempFistAttrItem.equals(_secondType.getAttribute(tempFistAttrItemName))) {
                                return false;
                            }
                        } catch (AttributeNotFoundException e) {
                            return false;
                        }
                    }
                    return true;
                }
            } finally {
                _secondType.readLock.unlock();
            }
        } finally {
            _firstType.readLock.unlock();
        }

        return false;
    }
}
