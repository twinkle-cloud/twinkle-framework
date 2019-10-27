package com.twinkle.framework.ruleengine.rule.support;

import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.context.support.DynamicStructAttribute;
import com.twinkle.framework.struct.lang.StructAttribute;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.type.StructType;
import lombok.Getter;

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
    private final String attributeName;
    private final int attributeIndex;
    private final String defaultValue;
    private final String configPath;
    private final DynamicStructAttribute structAttribute;

    public AttributeConversionMapping(String _structAttributeName, String _attrName, String _defaultValue, StructType _structType, String _configPath) throws ConfigurationException {
        this.attributeName = _attrName;
        this.attributeIndex = PrimitiveAttributeSchema.getInstance().getAttributeIndex(_attrName, _configPath);
        this.defaultValue = _defaultValue;
        this.configPath = _configPath;
        this.structAttribute = new DynamicStructAttribute(_structAttributeName, _structType, _configPath);
    }

    /**
     * Get the StructAttribute's name.
     *
     * @return
     */
    public String getStructAttributeName(){
        return this.structAttribute.getStructAttributeName();
    }

    /**
     * Resolve the StructAttribute to get the attribute Ref.
     *
     * @param _context
     * @param _structAttribute
     * @return
     * @throws RuleException
     */
    public AttributeRef resolve(NormalizedContext _context, StructAttribute _structAttribute) throws RuleException {
        return this.structAttribute.resolve(_context, _structAttribute);
    }
}
