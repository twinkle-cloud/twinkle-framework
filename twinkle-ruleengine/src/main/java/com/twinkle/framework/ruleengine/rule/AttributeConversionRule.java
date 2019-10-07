package com.twinkle.framework.ruleengine.rule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.ruleengine.rule.support.AttributeConversionMapping;
import com.twinkle.framework.struct.asm.descriptor.SAAttributeDescriptor;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.error.AttributeNotFoundException;
import com.twinkle.framework.struct.error.AttributeNotSetException;
import com.twinkle.framework.struct.error.AttributeTypeMismatchException;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.type.AttributeType;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.type.StructType;
import com.twinkle.framework.struct.utils.StructAttributeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/6/19 10:30 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class AttributeConversionRule extends AbstractRule {
    private boolean autoMapSA2Attribute;
    private boolean autoMapAttribute2SA;
    private boolean createStructAttributeFlag;
    private boolean createMissingSAAttr;
    private boolean createMissingAttr;

    private int structAttributeIndex = -1;
    private StructType structType;
    private AttributeInfo structAttributeInfo;
    private AttributeConversionMapping[] structAttribute2Attribute;
    private AttributeConversionMapping[] attribute2StructAttribute;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.autoMapSA2Attribute = _conf.getBooleanValue("StructAttributeToAttributeAutoMap");
        this.autoMapAttribute2SA = _conf.getBooleanValue("AttributeToStructAttributeAutoMap");
        String tempSAAttrName = _conf.getString("StructAttribute");

        if (tempSAAttrName != null) {
            this.structAttributeIndex = this.primitiveAttributeSchema.getAttributeIndex(tempSAAttrName, this.getFullPathName());
            if (this.structAttributeIndex < 0) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_ATTR_INVALID, "Struct Attribute's Attribute is not configured.");
            }
            this.structAttributeInfo = this.primitiveAttributeSchema.getAttribute(this.structAttributeIndex);
            this.structType = StructAttributeSchemaManager.getStructAttributeSchema().getStructAttributeType(this.structAttributeInfo.getValueType());
        } else {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_ATTR_MISSED, "Struct Attribute's Attribute is not configured.");
        }
        this.createStructAttributeFlag = _conf.getBooleanValue("CreateStructAttributeIfNeeded");
        this.createMissingSAAttr = _conf.getBoolean("CreateMissingAttributeInStructAttribute") == null ? true : _conf.getBoolean("CreateMissingAttributeInStructAttribute");
        this.createMissingAttr = _conf.getBoolean("CreateMissingAttributeInAttribute") == null ? true : _conf.getBoolean("CreateMissingAttributeInAttribute");

        this.structAttribute2Attribute = this.packConversionMapArray(_conf, "StructAttributeToAttributeMap", 0);
        this.attribute2StructAttribute = this.packConversionMapArray(_conf, "AttributeToStructAttributeMap", 1);
        //Merge auto mapping with given mapping.
        AttributeConversionMapping[] tempSA2AAutoMapping = null;
        if (this.autoMapSA2Attribute) {
            tempSA2AAutoMapping = this.getAutoMappingMatrix(this.structType);
        }
        if (this.structAttribute2Attribute != null) {
            tempSA2AAutoMapping = this.mergeMappingMatrix(tempSA2AAutoMapping, this.structAttribute2Attribute, true);
        }
        if (tempSA2AAutoMapping != null) {
            this.structAttribute2Attribute = tempSA2AAutoMapping;
        }
        //Merge auto mapping with given mapping.
        AttributeConversionMapping[] tempA2SAMapping = null;
        if (this.autoMapAttribute2SA) {
            tempA2SAMapping = this.getAutoMappingMatrix(this.structType);
        }
        if (this.attribute2StructAttribute != null) {
            tempA2SAMapping = this.mergeMappingMatrix(tempA2SAMapping, this.attribute2StructAttribute, false);
        }
        if (tempA2SAMapping != null) {
            this.attribute2StructAttribute = tempA2SAMapping;
        }
    }

    /**
     * Pack the conversion map.
     *
     * @param _conf
     * @param _key
     * @param _type
     * @return
     */
    private AttributeConversionMapping[] packConversionMapArray(JSONObject _conf, String _key, int _type) {
        JSONArray tempArray = _conf.getJSONArray(_key);
        if (!tempArray.isEmpty()) {
            AttributeConversionMapping[] tempMappingArray = new AttributeConversionMapping[tempArray.size()];
            for (int i = 0; i < tempArray.size(); i++) {
                JSONArray tempItemArray = tempArray.getJSONArray(i);
                if (tempItemArray.isEmpty()) {
                    continue;
                }
                String tempSAName = _type == 0 ? tempItemArray.getString(0) : tempItemArray.getString(1);
                String tempAttrName = _type == 0 ? tempItemArray.getString(1) : tempItemArray.getString(0);
                String tempDefaultValue = null;
                if (tempItemArray.size() > 2) {
                    tempDefaultValue = tempItemArray.getString(2);
                }
                tempMappingArray[i] = new AttributeConversionMapping(tempSAName, tempAttrName, tempDefaultValue, this.structType, this.getFullPathName());
            }
            return tempMappingArray;
        }
        return null;
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        Attribute tempAttribute = _context.getAttribute(this.structAttributeIndex);
        if (tempAttribute == null) {
            tempAttribute = this.primitiveAttributeSchema.newAttributeInstance(this.structAttributeIndex);
        }
        StructAttribute tempStructAttribute = (StructAttribute) tempAttribute.getObjectValue();

        if (this.structAttribute2Attribute != null) {
            try {
                this.convertStructAttributeToAttribute(tempStructAttribute, _context, this.structAttribute2Attribute);
            } catch (RuleException e) {
                log.debug("Last incoming attributes is " + _context.toStringWithAttrNames(true));
                throw e;
            }
        }

        if (this.attribute2StructAttribute != null) {
            try {
                this.convertAttributeToStructAttribute(_context, tempStructAttribute, this.attribute2StructAttribute);
            } catch (RuleException e) {
                log.debug("Last incoming nme is " + _context.toStringWithAttrNames(true));
                throw e;
            }
        }

        try {
            if (this.nextRule != null) {
                this.nextRule.applyRule(_context);
            }
        } finally {
        }
    }

    /**
     * Get Auto Mapping matrix.
     *
     * @param _type
     * @return
     * @throws RuleException
     */
    private AttributeConversionMapping[] getAutoMappingMatrix(StructType _type) throws RuleException {
        if (_type == null) {
            return null;
        }
        Iterator<SAAttributeDescriptor> tempItr = _type.getAttributes();
        List<AttributeConversionMapping> tempResultList = new ArrayList<>();

        SAAttributeDescriptor tempDescriptorItem;
        AttributeType tempItemType;
        while (tempItr.hasNext()) {
            tempDescriptorItem = tempItr.next();
            tempItemType = tempDescriptorItem.getType();
            if (!tempItemType.isPrimitiveType() && !tempItemType.isStringType()) {
                continue;
            }
            try {
                AttributeConversionMapping tempMappingItem = new AttributeConversionMapping(tempDescriptorItem.getName(), tempDescriptorItem.getName(), null, _type, this.getFullPathName());
                tempResultList.add(tempMappingItem);
            } catch (ConfigurationException e) {
                throw new RuleException(ExceptionCode.RULE_MANDATORY_ATTR_MISSED, this.getFullPathName() + " add auto mapping matrix failed.", e);
            }
        }
        return tempResultList.toArray(new AttributeConversionMapping[tempResultList.size()]);
    }

    /**
     * Merge the conversion mapping.
     *
     * @param _mappings1
     * @param _mappings2
     * @param _mergeAttrFlag :
     *                       true: Use Struct Attribute Name as key.
     *                       false: Use Primitive Attribute name as key.
     * @return
     */
    private AttributeConversionMapping[] mergeMappingMatrix(AttributeConversionMapping[] _mappings1, AttributeConversionMapping[] _mappings2, boolean _mergeAttrFlag) {
        if (_mappings1 != null && _mappings1.length != 0) {
            if (_mappings2 != null && _mappings2.length != 0) {
                List<AttributeConversionMapping> tempResultList = new ArrayList<>();
                int i;
                for (i = 0; i < _mappings2.length; ++i) {
                    tempResultList.add(_mappings2[i]);
                }

                for (i = 0; i < _mappings1.length; ++i) {
                    boolean duplicateFlag = false;
                    for (int j = 0; j < _mappings2.length; ++j) {
                        String tempAttrName1 = _mergeAttrFlag ? _mappings1[i].getStructAttributeName() : _mappings1[i].getAttributeName();
                        String tempAttrName2 = _mergeAttrFlag ? _mappings2[j].getStructAttributeName() : _mappings2[j].getAttributeName();
                        if (tempAttrName2.equalsIgnoreCase(tempAttrName1)) {
                            duplicateFlag = true;
                            break;
                        }
                    }
                    if (!duplicateFlag) {
                        tempResultList.add(_mappings1[i]);
                    }
                }
                return tempResultList.toArray(new AttributeConversionMapping[tempResultList.size()]);
            } else {
                return _mappings1;
            }
        } else {
            return _mappings2;
        }
    }

    /**
     * Convert Struct Attribute to Attribute.
     *
     * @param _structAttribute
     * @param _context
     * @param _mapping
     * @throws RuleException
     */
    private void convertStructAttributeToAttribute(StructAttribute _structAttribute, NormalizedContext _context, AttributeConversionMapping[] _mapping) throws RuleException {
        if (_mapping == null || _mapping.length == 0) {
            log.info("The StructAttribute to Attribute mapping is empty, so do nothing.");
            return;
        }
        for (int i = 0; i < _mapping.length; ++i) {
            if (_structAttribute == null) {
                throw new RuleException(ExceptionCode.RULE_APPLY_SA_IS_NULL, "Struct Attribute is null. Can not copy attribute from " + _mapping[i].getStructAttributeName() + " to " + _mapping[i].getAttributeName());
            }
            AttributeRef tempAttrRef = _mapping[i].resolve(_context, _structAttribute);
            String tempAttributeName = _mapping[i].getAttributeName();
            Attribute tempAttribute = _context.getAttribute(_mapping[i].getAttributeIndex());

            try {
                if (this.isAttributeSet(_structAttribute, tempAttrRef)) {
                    if (tempAttribute == null) {
                        tempAttribute = this.primitiveAttributeSchema.newAttributeInstance(_mapping[i].getAttributeIndex());
                        _context.setAttribute(tempAttribute, _mapping[i].getAttributeIndex());
                    }

                    StructAttributeUtil.setStructAttributeToAttribute(_structAttribute, tempAttrRef, tempAttribute);
                } else if (this.createMissingAttr) {
                    if (tempAttribute == null) {
                        tempAttribute = this.primitiveAttributeSchema.newAttributeInstance(_mapping[i].getAttributeIndex());
                        _context.setAttribute(tempAttribute, _mapping[i].getAttributeIndex());
                    }

                    if (_mapping[i].getDefaultValue() != null) {
                        log.debug("Attribute " + tempAttrRef + " is not set in the structured nme. Set " + tempAttributeName + " to default value.");
                        tempAttribute.setValue(_mapping[i].getDefaultValue());
                    } else {
                        log.debug("Attribute " + tempAttrRef + " is not set in the structured nme. Set empty value into " + tempAttributeName);
                        tempAttribute.setEmptyValue();
                    }
                } else {
                    log.debug("Attribute " + tempAttrRef + " is not set in the structured nme. Clear attribute " + tempAttributeName);
                    if (tempAttribute != null) {
                        _context.setAttribute(null, _mapping[i].getAttributeIndex());
                    }
                }
            } catch (Exception e) {
                log.error("Encountered exception while do attribute conversion [{}]. Exception: {}", new Object[]{tempAttrRef, tempAttributeName}, e);
                throw new RuleException(ExceptionCode.RULE_APPLY_SA_2_Attr_FAILED, "Can not copy attribute " + tempAttrRef + " to " + tempAttributeName, e);
            }
        }
    }

    /**
     * Check the attributeref is set or not?
     *
     * @param _structAttribute
     * @param _attributeRef
     * @return
     * @throws AttributeNotFoundException
     * @throws AttributeTypeMismatchException
     */
    private boolean isAttributeSet(StructAttribute _structAttribute, AttributeRef _attributeRef) throws AttributeNotFoundException, AttributeTypeMismatchException {
        boolean setFlag = false;
        try {
            if (_structAttribute != null && _attributeRef != null) {
                setFlag = _structAttribute.isAttributeSet(_attributeRef);
            }
        } catch (AttributeNotSetException e) {
        }
        return setFlag;
    }

    /**
     * Convert the primitive attribute to Struct attribute.
     *
     * @param _context
     * @param _structAttribute
     * @param _mapping
     * @throws RuleException
     */
    private void convertAttributeToStructAttribute(NormalizedContext _context, StructAttribute _structAttribute, AttributeConversionMapping[] _mapping) throws RuleException {
        if (_mapping == null || _mapping.length == 0) {
            log.info("The Attribute to StructAttribute mapping is empty, so do nothing.");
            return;
        }
        for (int i = 0; i < _mapping.length; ++i) {
            if (_structAttribute == null) {
                throw new RuleException(ExceptionCode.RULE_APPLY_SA_IS_NULL, "StructAttribute is missing. Can not copy attribute from " + _mapping[i].getAttributeName() + " to " + _mapping[i].getStructAttributeName());
            }
            String tempAttributeName = _mapping[i].getAttributeName();
            AttributeRef tempAttrRef = _mapping[i].resolve(_context, _structAttribute);
            Attribute tempAttribute = _context.getAttribute(_mapping[i].getAttributeIndex());
            if (tempAttribute == null) {
                if (!this.createMissingSAAttr) {
                    log.info("Attribute " + tempAttributeName + " is not set in nme. Clear attribute " + tempAttrRef);
                    try {
                        if (_structAttribute.isAttributeSet(tempAttrRef)) {
                            _structAttribute.clear(tempAttrRef);
                        }
                    } catch (StructAttributeException e) {
                        log.debug("Is Attribute set throws exception for Attribute " + tempAttrRef, e);
                    }
                    continue;
                }

                tempAttribute = this.primitiveAttributeSchema.newAttributeInstance(_mapping[i].getAttributeIndex());
                if (_mapping[i].getDefaultValue() != null) {
                    log.debug("Attribute " + tempAttributeName + " is not set in nme. Set " + tempAttrRef + " to default value.");
                    tempAttribute.setValue(_mapping[i].getDefaultValue());
                } else {
                    log.debug("Attribute " + tempAttributeName + " is not set in nme. Set empty value into " + tempAttrRef);
                    tempAttribute.setEmptyValue();
                }
            }
            try {
                StructAttributeUtil.setAttributeToStructAttribute(tempAttribute, _structAttribute, tempAttrRef, this.createStructAttributeFlag);
            } catch (Exception e) {
                log.error("Encountered exception while do attribute conversion [{}]. Exception: {}", new Object[]{tempAttributeName, tempAttrRef}, e);
                throw new RuleException(ExceptionCode.RULE_APPLY_Attr_2_SA_FAILED, "Can not copy attribute " + tempAttributeName + " to " + tempAttrRef, e);
            }
        }
    }
}
