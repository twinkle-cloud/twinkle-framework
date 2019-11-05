package com.twinkle.framework.ruleengine.rule.support;

import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.ref.AttributeRef;
import com.twinkle.framework.struct.type.PrimitiveType;
import com.twinkle.framework.struct.type.StructType;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     11/5/19 4:06 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class StructCriteriaList extends CriteriaList {
    /**
     * The Struct Type name.
     */
    private String structTypeName;
    public StructCriteriaList(String _structType) {
        this.structTypeName = _structType;
    }

    public StructCriteriaList(Criteria[] _criterias, String _structType) {
        this(_structType);
        if (_criterias != null) {
            for(int i = 0; i < _criterias.length; ++i) {
                this.criteria.add(_criterias[i]);
            }
        }

    }
    @Override
    public void addCriteria(String _operand, String _operator, String _value) {
        if (this.structTypeName == null) {
            super.addCriteria(_operand, _operator, _value);
        } else {
            StructType tempStructType = StructAttributeSchemaManager.getStructAttributeSchema().getStructAttributeType(this.structTypeName);
            AttributeRef tempAttrRef = null;

            try {
                StructAttributeSchemaManager.getStructAttributeFactory().getAttributeRef(tempStructType, _operand);
            } catch (Exception e) {
                log.warn("Parse Struct Attribute's attribute ref failed.");
            }

            if (_value.charAt(0) != '%') {
                tempAttrRef = StructAttributeSchemaManager.getStructAttributeFactory().getAttributeRef(tempStructType, _value);
                if (tempAttrRef != null) {
                    int tempTypeId = tempAttrRef.getType().getID();
                    switch(tempTypeId) {
                        case PrimitiveType.INT_ID:
                            _value = Integer.parseInt(_value) + "";
                            break;
                        case PrimitiveType.LONG_ID:
                            _value = Long.parseLong(_value) + "";
                        case PrimitiveType.CHAR_ID:
                        case PrimitiveType.BOOLEAN_ID:
                        case 7:
                        case 8:
                        default:
                            break;
                        case PrimitiveType.FLOAT_ID:
                            _value = Float.parseFloat(_value) + "";
                            break;
                        case PrimitiveType.DOUBLE_ID:
                            _value = Double.parseDouble(_value) + "";
                    }
                }
            }

            this.criteria.add(new Criteria(_operand, _operator, _value));
        }
    }
}
