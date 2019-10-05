package com.twinkle.framework.struct.context;

import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.asm.descriptor.TypeDescriptors;
import com.twinkle.framework.core.lang.util.ImmutableIterator;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.error.*;
import com.twinkle.framework.struct.type.DefaultStructType;
import com.twinkle.framework.struct.type.DefaultAttributeTypeManager;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.type.AttributeTypeManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/31/19 2:14 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DefaultStructAttributeSchema implements BeanStructAttributeSchema {
    private final Map<String, Namespace> namespaceMap = new HashMap<>(8);
    protected int structAttributeTypes = 0;
    private TypeDescriptors typeDescriptors = null;
    protected final Lock readLock;
    protected final Lock writeLock;

    public DefaultStructAttributeSchema() {
        ReentrantReadWriteLock tempReadWriteLock = new ReentrantReadWriteLock();
        this.readLock = tempReadWriteLock.readLock();
        this.writeLock = tempReadWriteLock.readLock();
    }

    @Override
    public TypeDescriptors getTypeDescriptors() {
        return this.typeDescriptors;
    }

    public void setTypeDescriptors(TypeDescriptors _descriptor) {
        this.typeDescriptors = _descriptor;
    }

    protected AttributeTypeManager createNewTypeManager() {
        return new DefaultAttributeTypeManager();
    }

    protected Namespace createNewNamespace() {
        return new Namespace();
    }

    protected DefaultStructType createNewStructAttributeType(String _attrName, BeanTypeDescriptor _descriptor) {
        return new DefaultStructType(_attrName, _descriptor);
    }

    @Override
    public int size() {
        this.readLock.lock();

        int tempSize;
        try {
            tempSize = this.structAttributeTypes;
        } finally {
            this.readLock.unlock();
        }

        return tempSize;
    }

    @Override
    public int getNumTypes(String _namespace) throws NamespaceNotFoundException {
        this.readLock.lock();

        int tempNum;
        try {
            if (!this.namespaceMap.containsKey(_namespace)) {
                throw new NamespaceNotFoundException(_namespace);
            }
            tempNum = (this.namespaceMap.get(_namespace)).getStructAttributeTypes().size();
        } finally {
            this.readLock.unlock();
        }

        return tempNum;
    }

    @Override
    public AttributeTypeManager getTypeManager(String _namespace) throws NamespaceNotFoundException {
        this.readLock.lock();

        AttributeTypeManager tempTypeManager;
        try {
            if (_namespace != null && _namespace.length() != 0) {
                if (!this.namespaceMap.containsKey(_namespace)) {
                    throw new NamespaceNotFoundException(_namespace);
                }
                tempTypeManager = (this.namespaceMap.get(_namespace)).getTypeManager();
                return tempTypeManager;
            }
            tempTypeManager = this.createNewTypeManager();
        } finally {
            this.readLock.unlock();
        }

        return tempTypeManager;
    }

    @Override
    public int getNumNamespaces() {
        this.readLock.lock();

        int tempNum;
        try {
            tempNum = this.namespaceMap.size();
        } finally {
            this.readLock.unlock();
        }

        return tempNum;
    }

    @Override
    public Iterator<String> getNamespaces() {
        this.readLock.lock();

        ImmutableIterator<String> tempItr;
        try {
            tempItr = new ImmutableIterator(this.namespaceMap.keySet().iterator());
        } finally {
            this.readLock.unlock();
        }

        return tempItr;
    }

    @Override
    public boolean hasNamespace(String _namespace) {
        this.readLock.lock();
        boolean existFlag = false;
        try {
            existFlag = this.namespaceMap.containsKey(_namespace);
        } finally {
            this.readLock.unlock();
        }
        return existFlag;
    }

    @Override
    public void addNamespace(String _namespace) throws NamespaceAlreadyExistsException {
        this.writeLock.lock();

        try {
            if (this.namespaceMap.containsKey(_namespace)) {
                throw new NamespaceAlreadyExistsException(_namespace);
            }

            Namespace tempNamespace = this.createNewNamespace();
            AttributeTypeManager tempTypeManager = tempNamespace.getTypeManager();
            if (tempTypeManager.hasTypeName(_namespace)) {
                throw new IllegalArgumentException("Namespace name " + _namespace + " is already defined in TypeManager.");
            }

            this.namespaceMap.put(_namespace, tempNamespace);
        } finally {
            this.writeLock.unlock();
        }

    }

    /**
     * Remove the namespace from the schema.
     *
     * @param _namespace
     * @throws NamespaceNotFoundException
     */
    protected void removeNamespace(String _namespace) throws NamespaceNotFoundException {
        this.writeLock.lock();

        try {
            if (!this.namespaceMap.containsKey(_namespace)) {
                throw new NamespaceNotFoundException("Namespace is not in the schema: " + _namespace);
            }

            this.namespaceMap.remove(_namespace);
        } finally {
            this.writeLock.unlock();
        }

    }

    @Override
    public Iterator<StructType> getStructAttributeTypes(String _namespace) throws NamespaceNotFoundException {
        this.readLock.lock();

        ImmutableIterator<StructType> tempItr;
        try {
            if (!this.namespaceMap.containsKey(_namespace)) {
                throw new NamespaceNotFoundException(_namespace);
            }
            Namespace tempNamespace = this.namespaceMap.get(_namespace);
            tempItr = new ImmutableIterator(tempNamespace.getStructAttributeTypes().values().iterator());
        } finally {
            this.readLock.unlock();
        }

        return tempItr;
    }

    @Override
    public boolean hasStructAttributeType(String _namespace, String _structTypeName) {
        this.readLock.lock();

        boolean tempResult;
        try {
            tempResult = this.namespaceMap.containsKey(_namespace) && (this.namespaceMap.get(_namespace)).getStructAttributeTypes().containsKey(_structTypeName);
        } finally {
            this.readLock.unlock();
        }

        return tempResult;
    }

    @Override
    public boolean hasStructAttributeType(String _structTypeName) {
        StringTokenizer tempTokenizer = new StringTokenizer(_structTypeName, ":");
        return tempTokenizer.countTokens() != 2 ? false : this.hasStructAttributeType(tempTokenizer.nextToken(), tempTokenizer.nextToken());
    }

    @Override
    public StructType getStructAttributeType(String _namespace, String _structTypeName) throws NamespaceNotFoundException, StructAttributeTypeNotFoundException {
        this.readLock.lock();

        StructType tempResultType;
        try {
            if (!this.namespaceMap.containsKey(_namespace)) {
                throw new NamespaceNotFoundException(_namespace);
            }
            StructType tempSAType2 = (this.namespaceMap.get(_namespace)).getStructAttributeTypes().get(_structTypeName);
            if (tempSAType2 == null) {
                throw new StructAttributeTypeNotFoundException(_namespace, _structTypeName);
            }
            tempResultType = tempSAType2;
        } finally {
            this.readLock.unlock();
        }

        return tempResultType;
    }

    @Override
    public StructType getStructAttributeType(String _structTypeName) throws NamespaceNotFoundException, StructAttributeTypeNotFoundException {
        StringTokenizer tempTokenizer = new StringTokenizer(_structTypeName, ":");
        if (tempTokenizer.countTokens() != 2) {
            throw new StructAttributeTypeNotFoundException(_structTypeName, "");
        } else {
            return this.getStructAttributeType(tempTokenizer.nextToken(), tempTokenizer.nextToken());
        }
    }

    /**
     * Build
     *
     * @param _namespace
     * @param _attrName
     * @param _descriptor
     * @return
     * @throws NamespaceNotFoundException
     */
    public StructType newStructAttributeType(String _namespace, String _attrName, BeanTypeDescriptor _descriptor) throws NamespaceNotFoundException {
        this.readLock.lock();

        DefaultStructType tempResultAttrType;
        try {
            if (!this.namespaceMap.containsKey(_namespace)) {
                throw new NamespaceNotFoundException(_namespace);
            }

            DefaultStructType tempSAType = this.createNewStructAttributeType(_attrName, _descriptor);
            tempSAType.setNamespace(_namespace);
            Namespace tempNamespace = this.namespaceMap.get(_namespace);
            AttributeTypeManager tempTypeManager = tempNamespace.getTypeManager();
            tempSAType.setTypeManager(tempTypeManager);
            tempResultAttrType = tempSAType;
        } finally {
            this.readLock.unlock();
        }
        return tempResultAttrType;
    }

    @Override
    public StructType newStructAttributeType(String _namespace, String _structTypeName) throws NamespaceNotFoundException {
        return this.newStructAttributeType(_namespace, _structTypeName, null);
    }

    @Override
    public void addStructAttributeType(StructType _structType) throws NamespaceNotFoundException, StructAttributeTypeAlreadyExistsException {
        DefaultStructType tempSAType = (DefaultStructType) _structType;
        AttributeTypeManager tempTypeManager = _structType.getTypeManager();
        String tempNamespace = _structType.getNamespace();
        this.writeLock.lock();

        try {
            if (this.namespaceMap.containsKey(tempNamespace) && (this.namespaceMap.get(tempNamespace)).getStructAttributeTypes().containsKey(_structType.getName())) {
                throw new StructAttributeTypeAlreadyExistsException(tempNamespace, _structType);
            }

            try {
                if (tempTypeManager.hasTypeName(_structType.getName())) {
                    throw new IllegalArgumentException("StructAttributeType name " + _structType.getName() + " is already defined in TypeManager.");
                }

                Namespace tempNamespace2 = this.namespaceMap.get(tempNamespace);
                if (tempNamespace2 == null) {
                    throw new NamespaceNotFoundException(tempNamespace);
                }

                tempNamespace2.getStructAttributeTypes().put(tempSAType.getName(), tempSAType);
                tempTypeManager.addType(tempSAType.getName(), tempSAType);
                tempSAType.publish();
                ++this.structAttributeTypes;
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("StructAttributeType implementation " + _structType.getClass() + " is not provided by the StructAttribute Framework.");
            } catch (TypeAlreadyExistsException e) {
                e.printStackTrace();
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Remove the struct type from this schema with given struct type's name.
     *
     * @param _structTypeName
     * @throws NamespaceNotFoundException
     * @throws StructAttributeTypeNotFoundException
     */
    protected void removeStructAttributeType(String _structTypeName) throws NamespaceNotFoundException, StructAttributeTypeNotFoundException {
        StringTokenizer tempTokenizer = new StringTokenizer(_structTypeName, ":");
        if (tempTokenizer.countTokens() != 2) {
            throw new StructAttributeTypeNotFoundException(_structTypeName, "");
        } else {
            String tempNamespaceName = tempTokenizer.nextToken();
            String tempStructTypeName = tempTokenizer.nextToken();
            this.writeLock.lock();

            try {
                Namespace tempNamespace = this.namespaceMap.get(tempNamespaceName);
                if (tempNamespace == null) {
                    throw new NamespaceNotFoundException(tempNamespaceName);
                }

                StructType tempSAType = tempNamespace.getStructAttributeTypes().get(tempStructTypeName);
                if (tempSAType == null) {
                    throw new StructAttributeTypeNotFoundException(tempNamespaceName, tempStructTypeName);
                }

                tempNamespace.getStructAttributeTypes().remove(tempStructTypeName);
                --this.structAttributeTypes;
            } finally {
                this.writeLock.unlock();
            }
        }
    }

    @Override
    public String toString() {
        this.readLock.lock();

        String tempResultStr;
        try {
            String tempLineSeparator = System.getProperty("line.separator");
            String temp2Spaces = "  ";
            String temp4Spaces = "    ";
            String temp8Spaces = "      ";
            StringBuilder tempBuilder = new StringBuilder(4096);
            tempBuilder.append("StructAttributeSchema {");
            tempBuilder.append(tempLineSeparator);
            Iterator<String> tempNamespaceItr = this.getNamespaces();

            while (tempNamespaceItr.hasNext()) {
                tempResultStr = tempNamespaceItr.next();
                tempBuilder.append(temp2Spaces);
                tempBuilder.append(tempResultStr);
                tempBuilder.append(" {");
                tempBuilder.append(tempLineSeparator);

                Iterator<StructType> tempSATypeItr;
                try {
                    tempSATypeItr = this.getStructAttributeTypes(tempResultStr);
                } catch (NamespaceNotFoundException e) {
                    throw new RuntimeException("Namespace [" + tempResultStr + "] could not be found", e);
                }

                while (tempSATypeItr.hasNext()) {
                    StructType tempSAType = tempSATypeItr.next();
                    tempBuilder.append(temp4Spaces);
                    tempBuilder.append(tempSAType.getName());
                    tempBuilder.append(" {");
                    tempBuilder.append(tempLineSeparator);
                    Iterator<SAAttributeDescriptor> tempSADescriptorItr = tempSAType.getAttributes();
                    while (tempSADescriptorItr.hasNext()) {
                        SAAttributeDescriptor tempDescriptor = tempSADescriptorItr.next();
                        tempBuilder.append(temp8Spaces);
                        if (tempDescriptor.isOptional()) {
                            tempBuilder.append("optional ");
                        }
                        tempBuilder.append(tempDescriptor.getTypeName());
                        tempBuilder.append(' ');
                        tempBuilder.append(tempDescriptor.getName());
                        tempBuilder.append(';');
                        tempBuilder.append(tempLineSeparator);
                    }
                    tempBuilder.append(temp4Spaces);
                    tempBuilder.append('}');
                    tempBuilder.append(tempLineSeparator);
                }
                tempBuilder.append(temp2Spaces);
                tempBuilder.append('}');
                tempBuilder.append(tempLineSeparator);
            }
            tempBuilder.append('}');
            tempResultStr = tempBuilder.toString();
        } finally {
            this.readLock.unlock();
        }
        return tempResultStr;
    }

    @Getter
    static class Namespace {
        private AttributeTypeManager typeManager = new DefaultAttributeTypeManager();
        private Map<String, StructType> structAttributeTypes = new HashMap<>();
    }
}
