package com.twinkle.framework.core.datastruct.converter;

import com.twinkle.framework.core.datastruct.define.AttributeDef;
import com.twinkle.framework.core.utils.TypeDefUtil;
import lombok.Getter;

import java.util.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 20:29<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class StrictAttributeConverter implements AttributeConverter {
    private final String typeName;
    private final Set<String> reservedNames;

    public StrictAttributeConverter(String _typeName, Set<String> _reservedNameSet) {
        this.typeName = _typeName;
        Objects.requireNonNull(_reservedNameSet);
        this.reservedNames = _reservedNameSet;
    }

    @Override
    public String normalize(String _attrName) {
        return TypeDefUtil.toBeanNormalForm(_attrName);
    }
    @Override
    public List<AttributeDef> normalize(List<AttributeDef> _attrDefList) {
        this.validateInternal(_attrDefList, true);
        return _attrDefList;
    }
    @Override
    public void validate(List<AttributeDef> _attrDefList) {
        this.validateInternal(_attrDefList, true);
    }

    /**
     * Validate the attribute define.
     *
     * @param _attrDefineList
     * @param _validateNameFlag
     */
    protected void validateInternal(List<AttributeDef> _attrDefineList, boolean _validateNameFlag) {
        Map tempMap1 = new HashMap(_attrDefineList.size());
        Map tempMap2 = new HashMap(_attrDefineList.size());
        _attrDefineList.stream().parallel().forEach(item ->{
            if (item == null) {
                throw new NullPointerException("Encountered null attribute definition for type " + this.typeName);
            }
            String tempAttrDefineName = item.getName();
            if (tempAttrDefineName == null) {
                throw new NullPointerException("Encountered null attribute name definition for type " + this.typeName);
            }
            if (tempAttrDefineName.length() == 0) {
                throw new IllegalArgumentException("Encountered empty attribute name definition for type " + this.typeName);
            }
            AttributeDef tempAttrDef = (AttributeDef)tempMap1.put(tempAttrDefineName.hashCode(), item);
            if (tempAttrDef != null) {
                throw new IllegalArgumentException("Hash code clashes for attributes: [" + tempAttrDef + "] and [" + item + "] for type " + this.typeName);
            }
            if (_validateNameFlag) {
                this.validateName(tempMap2, item);
            }
        });

    }

    /**
     * Check the attribute name.
     * validate the hashcode.
     *
     * @param _attrDefMap
     * @param _attrDef
     * @return
     */
    protected boolean validateName(Map<String, AttributeDef> _attrDefMap, AttributeDef _attrDef) {
        if (this.isReservedName(_attrDef)) {
            throw new IllegalArgumentException("Reserved word used as an attribute name [" + _attrDef + "] for type " + this.typeName);
        } else {
            AttributeDef tempAttrDef = _attrDefMap.put(_attrDef.getGetterName(), _attrDef);
            if (tempAttrDef != null) {
                throw new IllegalArgumentException("Name clash of the attributes [" + tempAttrDef + "] and [" + _attrDef + "] for type " + this.typeName);
            } else {
                return true;
            }
        }
    }

    protected boolean isReservedName(AttributeDef _attrDef) {
        return this.reservedNames.contains(_attrDef.getGetterName());
    }
}
