package com.twinkle.framework.ruleengine.rule.support;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.AttributeInfo;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.error.StructAttributeException;
import com.twinkle.framework.struct.factory.StructAttributeFactory;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.ref.CompositeName;
import com.twinkle.framework.struct.ref.DynamicAttributeRef;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.type.StructType;
import lombok.Getter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/6/19 10:32 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class AttributeConversionMapping {
    private StructType structType;
    private final String structAttributeName;
    private AttributeRef structAttributeRef;
    private final String attributeName;
    private final int attributeIndex;
    private final String defaultValue;
    private final String configPath;
    private DynamicAttributeRef dynamicAttributeRef;
    private int[] dynamicAttributeIndexes;
    private int[] dynamicAttributeValues;

    public AttributeConversionMapping(String _structAttributeName, String _attrName, String _defaultValue, StructType _structType, String _configPath) throws ConfigurationException {
        this.attributeName = _attrName;
        this.attributeIndex = PrimitiveAttributeSchema.getInstance().getAttributeIndex(_attrName, _configPath);
        this.defaultValue = _defaultValue;
        this.configPath = _configPath;
        this.structAttributeName = _structAttributeName;
        if (_structType != null) {
            try {
                this.setStructAttributeRef(_structType);
            } catch (Exception e) {
                throw new ConfigurationException(ExceptionCode.RULE_SA_RESOLVED_FAILED, _configPath + " failed while resolving " + this.structAttributeName, e);
            }
        }

    }

    private void setStructAttributeRef(StructType _structType) throws ParseException {
        this.structType = _structType;
        StructAttributeFactory tempFactory = StructAttributeSchemaManager.getStructAttributeFactory();
        List<Integer> tempIndexList = new ArrayList<>();
        CompositeName tempCompositeName = new CompositeName(this.structAttributeName);
        tempCompositeName = tempCompositeName.head();
        boolean isTailFlag = false;

        while (true) {
            if (tempCompositeName.hasIndex() && !tempCompositeName.isNumericIndex()) {
                isTailFlag = true;
                try {
                    tempFactory.getAttributeRef(_structType, tempCompositeName.indexString());
                } catch (Exception e) {
                    AttributeInfo tempAttributeInfo = PrimitiveAttributeSchema.getInstance().getAttribute(tempCompositeName.indexString());
                    if (tempAttributeInfo == null) {
                        throw e;
                    }
                    tempIndexList.add(tempAttributeInfo.getIndex());
                    tempCompositeName = tempCompositeName.replicate("*");
                }
            }

            if (tempCompositeName.isTail()) {
                if (isTailFlag) {
                    this.dynamicAttributeRef = tempFactory.getDynamicAttributeRef(this.structType, tempCompositeName.fullName());
                    this.dynamicAttributeIndexes = new int[tempIndexList.size()];
                    this.dynamicAttributeValues = new int[this.dynamicAttributeIndexes.length];

                    for (int i = 0; i < tempIndexList.size(); i++) {
                        this.dynamicAttributeIndexes[i] = tempIndexList.get(i);
                    }
                } else {
                    this.structAttributeRef = tempFactory.getAttributeRef(_structType, this.structAttributeName);
                }
                return;
            }
            tempCompositeName = tempCompositeName.next();
        }
    }

    public AttributeRef resolve(NormalizedContext _context, StructAttribute _structAttribute) throws RuleException {
        if (this.dynamicAttributeRef != null) {
            for (int i = 0; i < this.dynamicAttributeIndexes.length; i++) {
                Attribute tempAttribute = _context.getAttribute(this.dynamicAttributeIndexes[i]);
                if (tempAttribute == null) {
                    throw new RuleException(ExceptionCode.RULE_ATTR_NOT_INITIALIZED, this.configPath + ": Primitive attribute " + PrimitiveAttributeSchema.getInstance().getAttribute(this.dynamicAttributeIndexes[i]).getName() + " not set. Unable to resolve " + this.structAttributeName);
                }

                String tempAttrValue = tempAttribute.toString();

                try {
                    this.dynamicAttributeValues[i] = Integer.decode(tempAttrValue);
                } catch (NumberFormatException e) {
                    throw new RuleException(ExceptionCode.RULE_ATTR_VALUE_UNEXPECTED, this.configPath + ": Primitive attribute " + PrimitiveAttributeSchema.getInstance().getAttribute(this.dynamicAttributeIndexes[i]).getName() + " set to invalid value " + tempAttrValue + ". Unable to resolve " + this.structAttributeName, e);
                }
            }

            try {
                return this.dynamicAttributeRef.getConcreteRef(_structAttribute, this.dynamicAttributeValues);
            } catch (StructAttributeException e) {
                throw new RuleException(ExceptionCode.RULE_SA_RESOLVED_FAILED, this.configPath + " failed while resolving " + this.structAttributeName, e);
            }
        }
        StructType tempStructType = _structAttribute.getType();
        if (this.structAttributeRef != null && tempStructType.equals(this.structType)) {
            return this.structAttributeRef;
        }
        try {
            this.setStructAttributeRef(tempStructType);
        } catch (Exception e) {
            throw new RuleException(ExceptionCode.RULE_SA_RESOLVED_FAILED, this.configPath + " failed while resolving " + this.structAttributeName, e);
        }

        return this.resolve(_context, _structAttribute);
    }
}
