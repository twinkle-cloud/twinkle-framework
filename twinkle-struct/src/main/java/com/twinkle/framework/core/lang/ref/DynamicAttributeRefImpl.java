package com.twinkle.framework.core.lang.ref;

import com.twinkle.framework.core.context.StructAttributeManager;
import com.twinkle.framework.core.datastruct.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.core.datastruct.descriptor.SAAttributeDescriptorImpl;
import com.twinkle.framework.core.error.*;
import com.twinkle.framework.core.lang.struct.*;
import com.twinkle.framework.core.lang.util.Array;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/3/19 10:25 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class DynamicAttributeRefImpl implements DynamicAttributeRef {
    public static final char WILDCARD = '*';
    private List<DynamicComponent> dynamicComponentList;
    private String compositeName;
    private SAAttributeDescriptor attributeDescriptor;
    protected DynamicComponent tailDynamicComponent;

    public DynamicAttributeRefImpl(StructAttributeType _saType, String _compositeName) throws BadAttributeNameException, AttributeNotFoundException, ParseException {
        this(_saType, _compositeName, StructAttributeManager.getStructAttributeFactory());
    }

    protected DynamicAttributeRefImpl(StructAttributeType _saType, String _compositeName, StructAttributeFactory _factory) throws BadAttributeNameException, AttributeNotFoundException, ParseException {
        this.compositeName = _compositeName;
        CompositeName tempCN = new CompositeName(_compositeName);
        List<CompositeName> tempSubCNList = tempCN.splitByNonNumeric();
        this.dynamicComponentList = new ArrayList(tempSubCNList.size());
        DynamicComponent tempDynamicComponent = null;
        StructAttributeRef tempAttrRef = null;

        for (Iterator tempItr = tempSubCNList.iterator(); tempItr.hasNext(); tempAttrRef = tempDynamicComponent.attrRef) {
            CompositeName tempItemCN = (CompositeName) tempItr.next();
            StructAttributeType tempSAType;
            if (tempAttrRef != null) {
                tempSAType = (StructAttributeType) tempAttrRef.getType();
            } else {
                tempSAType = _saType;
            }

            tempDynamicComponent = new DynamicComponent();
            tempDynamicComponent.attrRef = (StructAttributeRef) CompositeAttributeRefFactory.getCompositeAttributeRef(_factory, tempSAType, tempItemCN.head());
            if (tempItemCN.hasIndex() && !tempItemCN.isNumericIndex()) {
                String tempIndexStr = tempItemCN.indexString();
                if (isWildcard(tempIndexStr)) {
                    tempDynamicComponent.isWildcard = true;
                    tempDynamicComponent.isDynamic = false;
                } else {
                    tempDynamicComponent.indexRef = (StructAttributeRef) CompositeAttributeRefFactory.getCompositeAttributeRef(_factory, _saType, tempIndexStr);
                    tempDynamicComponent.isWildcard = false;
                    tempDynamicComponent.isDynamic = true;
                }
            } else {
                tempDynamicComponent.isWildcard = false;
                tempDynamicComponent.isDynamic = false;
            }

            this.dynamicComponentList.add(tempDynamicComponent);
        }

        Objects.requireNonNull(tempDynamicComponent);
        this.tailDynamicComponent = tempDynamicComponent;
        this.attributeDescriptor = new SAAttributeDescriptorImpl(_compositeName, tempDynamicComponent.attrRef.getType(), tempDynamicComponent.attrRef.getDescriptor().isOptional());
    }

    @Override
    public String getName() {
        return this.compositeName;
    }

    @Override
    public StructType getType() {
        return this.attributeDescriptor.getType();
    }

    @Override
    public SAAttributeDescriptor getDescriptor() {
        return this.attributeDescriptor;
    }

    @Override
    public AttributeRef getConcreteRef(int... _index) {
        return new ConcreteDynamicArrayRef(this, _index);
    }

    @Override
    public AttributeRef getConcreteRef(StructAttribute _attr, int... _indexes) {
        return new ConcreteDynamicArrayRef(this, _attr, _indexes);
    }

    /**
     * Check the str is * or not?
     *
     * @param _str
     * @return
     */
    private static boolean isWildcard(String _str) {
        return _str != null && (_str.length() == 0 || _str.length() == 1 && _str.charAt(0) == WILDCARD);
    }

    private static class ConcreteDynamicArrayRef implements StructAttributeRef {
        private final DynamicAttributeRefImpl dynamicAttributeRef;
        private final List<DynamicComponent> dynamicComponents;

        private ConcreteDynamicArrayRef(DynamicAttributeRefImpl _dynamicAttrRef, int... _replicableIndexes) {
            this.dynamicAttributeRef = _dynamicAttrRef;
            this.dynamicComponents = new ArrayList(this.dynamicAttributeRef.dynamicComponentList);
            int tempReplicatedIndex = 0;

            for (int i = 0; i < this.dynamicComponents.size(); ++i) {
                DynamicComponent tempComponent = this.dynamicComponents.get(i);
                if (tempComponent.isWildcard) {
                    DynamicComponent tempNewComponent = (DynamicComponent) tempComponent.clone();
                    tempNewComponent.attrRef = (StructAttributeRef) ((ArrayAttributeRef) tempNewComponent.attrRef).replicate(_replicableIndexes[tempReplicatedIndex++]);
                    tempNewComponent.isWildcard = false;
                    this.dynamicComponents.set(i, tempNewComponent);
                }
            }

            if (tempReplicatedIndex != _replicableIndexes.length) {
                throw new IllegalArgumentException("Expected number of indexes is " + tempReplicatedIndex + ", found " + _replicableIndexes.length);
            }
        }

        private ConcreteDynamicArrayRef(DynamicAttributeRefImpl _dynamicAttrRef, StructAttribute _attr, int... _replicableIndexes) {
            this.dynamicAttributeRef = _dynamicAttrRef;
            this.dynamicComponents = new ArrayList(this.dynamicAttributeRef.dynamicComponentList);
            int tempReplicatedIndex = 0;

            for (int i = 0; i < this.dynamicComponents.size(); ++i) {
                DynamicComponent tempComponent = this.dynamicComponents.get(i);
                DynamicComponent tempCloneComponent = (DynamicComponent) tempComponent.clone();
                if (tempComponent.isWildcard) {
                    tempCloneComponent.attrRef = (StructAttributeRef) ((ArrayAttributeRef) tempCloneComponent.attrRef).replicate(_replicableIndexes[tempReplicatedIndex++]);
                    tempCloneComponent.isWildcard = false;
                } else if (tempComponent.isDynamic) {
                    tempCloneComponent.attrRef = tempCloneComponent.getRef(_attr);
                    tempCloneComponent.isDynamic = false;
                }
                this.dynamicComponents.set(i, tempCloneComponent);
            }

            if (tempReplicatedIndex != _replicableIndexes.length) {
                throw new IllegalArgumentException("Expected number of indexes is " + tempReplicatedIndex + ", found " + _replicableIndexes.length);
            }
        }

        /**
         * Do check the tail struct attribute.
         *
         * @param _attr
         * @return
         * @throws AttributeNotFoundException
         * @throws AttributeNotSetException
         * @throws AttributeTypeMismatchException
         */
        private StructAttribute checkTailStructAttribute(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = _attr;
            int tempLastIndex = this.dynamicComponents.size() - 1;
            for (int i = 0; i < tempLastIndex; ++i) {
                DynamicComponent tempComponent = this.dynamicComponents.get(i);
                StructAttributeRef tempAttrRef = tempComponent.getRef(tempAttr);
                if (!tempAttrRef.isAttributeSet(tempAttr)) {
                    return null;
                }
                tempAttr = tempAttrRef.getStruct(tempAttr);
            }

            return tempAttr;
        }

        /**
         * Get the tail struct attribute.
         *
         * @param _attr
         * @return
         * @throws AttributeNotFoundException
         * @throws AttributeNotSetException
         * @throws AttributeTypeMismatchException
         */
        private StructAttribute getTailStructAttribute(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = _attr;

            int tempLastIndex = this.dynamicComponents.size() - 1;
            for (int i = 0; i < tempLastIndex; ++i) {
                DynamicComponent tempComponent = this.dynamicComponents.get(i);
                StructAttributeRef tempAttrRef = tempComponent.getRef(tempAttr);
                if (tempAttrRef.isAttributeSet(tempAttr)) {
                    tempAttr = tempAttrRef.getStruct(tempAttr);
                } else {
                    StructAttributeFactory tempFactory = StructAttributeManager.getStructAttributeFactory();
                    StructAttribute tempNewAttr = tempFactory.newStructAttribute((StructAttributeType) tempAttrRef.getType());
                    tempAttr.setStruct(tempAttrRef, tempNewAttr);
                    tempAttr = tempNewAttr;
                }
            }

            return tempAttr;
        }

        @Override
        public String getName() {
            return this.dynamicAttributeRef.getName();
        }

        @Override
        public StructType getType() {
            return this.dynamicAttributeRef.getType();
        }

        @Override
        public SAAttributeDescriptor getDescriptor() {
            return this.dynamicAttributeRef.getDescriptor();
        }

        @Override
        public boolean isComposite() {
            return false;
        }

        @Override
        public boolean isArray() {
            return false;
        }

        @Override
        public void clear(StructAttribute _attr) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.checkTailStructAttribute(_attr);
            if (tempAttr != null) {
                int tempIndex = this.dynamicComponents.size() - 1;
                DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
                StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
                if (tempAttrRef.isAttributeSet(_attr)) {
                    tempAttr.clear(tempAttrRef);
                }
            }
        }

        @Override
        public boolean isAttributeSet(StructAttribute _attr) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.checkTailStructAttribute(_attr);
            if (tempAttr != null) {
                int tempIndex = this.dynamicComponents.size() - 1;
                DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
                StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
                return tempAttrRef.isAttributeSet(tempAttr);
            }
            return false;
        }

        @Override
        public byte getByte(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getByte(tempAttrRef);
        }

        @Override
        public void setByte(StructAttribute _attr, byte _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttrRef.setByte(tempAttr, _value);
        }

        @Override
        public short getShort(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getShort(tempAttrRef);
        }

        @Override
        public void setShort(StructAttribute _attr, short _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttr.setShort(tempAttrRef, _value);
        }

        @Override
        public int getInt(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getInt(tempAttrRef);
        }

        @Override
        public void setInt(StructAttribute _attr, int _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttr.setInt(tempAttrRef, _value);
        }

        @Override
        public long getLong(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getLong(tempAttrRef);
        }

        @Override
        public void setLong(StructAttribute _attr, long _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttr.setLong(tempAttrRef, _value);
        }

        @Override
        public char getChar(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getChar(tempAttrRef);
        }

        @Override
        public void setChar(StructAttribute _attr, char _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttr.setChar(tempAttrRef, _value);
        }

        @Override
        public boolean getBoolean(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getBoolean(tempAttrRef);
        }

        @Override
        public void setBoolean(StructAttribute _attr, boolean _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttr.setBoolean(tempAttrRef, _value);
        }

        @Override
        public float getFloat(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getFloat(tempAttrRef);
        }

        @Override
        public void setFloat(StructAttribute _attr, float _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttr.setFloat(tempAttrRef, _value);
        }

        @Override
        public double getDouble(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getDouble(tempAttrRef);
        }

        @Override
        public void setDouble(StructAttribute _attr, double _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttr.setDouble(tempAttrRef, _value);
        }

        @Override
        public String getString(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getString(tempAttrRef);
        }

        @Override
        public void setString(StructAttribute _attr, String _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttr.setString(tempAttrRef, _value);
        }

        @Override
        public StructAttribute getStruct(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getStruct(tempAttrRef);
        }

        @Override
        public void setStruct(StructAttribute _attr, StructAttribute _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttr.setStruct(tempAttrRef, _value);
        }

        @Override
        public Array getArray(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getArray(tempAttrRef);
        }

        @Override
        public void setArray(StructAttribute _attr, Array _value) throws AttributeNotFoundException, AttributeTypeMismatchException, AttributeNotSetException, ClassCastException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            tempAttr.setArray(tempAttrRef, _value);
        }

        @Override
        public int getArraySize(StructAttribute _attr) throws AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            StructAttribute tempAttr = this.getTailStructAttribute(_attr);
            int tempIndex = this.dynamicComponents.size() - 1;
            DynamicComponent tempComponent = this.dynamicComponents.get(tempIndex);
            StructAttributeRef tempAttrRef = tempComponent.getRef(_attr);
            return tempAttr.getArraySize(tempAttrRef);
        }

        @Override
        public void copy(StructAttribute _srcAttr, StructAttribute _destAttr) throws StructAttributeCopyException, AttributeNotFoundException, AttributeNotSetException, AttributeTypeMismatchException {
            throw new UnsupportedOperationException("copy");
        }
    }

    private static final class DynamicComponent implements Cloneable {
        private StructAttributeRef attrRef;
        private StructAttributeRef indexRef;
        private boolean isWildcard;
        private boolean isDynamic;

        private DynamicComponent() {
        }

        /**
         * Get the given attribute's ref.
         *
         * @param _attr
         * @return
         */
        public StructAttributeRef getRef(StructAttribute _attr) {
            if (this.isDynamic) {
                int tempIndex;
                switch (this.indexRef.getType().getID()) {
                    case PrimitiveType.BYTE_ID:
                        tempIndex = _attr.getByte(this.indexRef);
                        break;
                    case PrimitiveType.SHORT_ID:
                        tempIndex = _attr.getShort(this.indexRef);
                        break;
                    case PrimitiveType.INT_ID:
                        tempIndex = _attr.getInt(this.indexRef);
                        break;
                    case PrimitiveType.LONG_ID:
                    default:
                        throw new AttributeTypeMismatchException(this.indexRef, this.indexRef.getType(), PrimitiveType.INT);
                    case PrimitiveType.CHAR_ID:
                        tempIndex = _attr.getChar(this.indexRef);
                }
                return (StructAttributeRef) ((ArrayAttributeRef) this.attrRef).replicate(tempIndex);
            } else {
                return this.attrRef;
            }
        }

        @Override
        public Object clone() {
            try {
                return super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
