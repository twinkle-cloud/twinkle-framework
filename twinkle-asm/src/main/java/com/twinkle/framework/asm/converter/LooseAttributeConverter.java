package com.twinkle.framework.asm.converter;

import com.twinkle.framework.asm.define.AttributeDef;
import com.twinkle.framework.asm.define.AttributeDefImpl;
import com.twinkle.framework.asm.utils.ClassDesignerUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 20:27<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class LooseAttributeConverter extends StrictAttributeConverter implements AttributeConverter {
    public static String ESCAPE_SEQUENCE = "_";

    public LooseAttributeConverter(String _typeName, Set<String> _reservedNameSet) {
        super(_typeName, _reservedNameSet);
    }
    @Override
    public List<AttributeDef> normalize(List<AttributeDef> _attrDefList) {
        this.validateInternal(_attrDefList, false);
        Map tempValidateMap = new HashMap(_attrDefList.size());

        return _attrDefList.stream().parallel().map(item ->{
            AttributeDef tempAttrDef = item;
            for(int i = 1; !this.validateName(tempValidateMap, tempAttrDef); tempAttrDef = this.fixAttribute(tempAttrDef, i++)) {
            }
            return tempAttrDef;
        }).collect(Collectors.toList());
    }

    @Override
    protected boolean validateName(Map<String, AttributeDef> _attrDefMap, AttributeDef _attrDef) {
        if (this.isReservedName(_attrDef)) {
            if (log.isWarnEnabled()) {
                log.warn("Reserved word used as an attribute name [{}] for type {}", new Object[]{_attrDef.getName(), this.getTypeName()});
            }
            return false;
        } else {
            AttributeDef tempAttrDef = _attrDefMap.put(_attrDef.getGetterName(), _attrDef);
            if (tempAttrDef != null) {
                _attrDefMap.put(tempAttrDef.getGetterName(), tempAttrDef);
                if (log.isWarnEnabled()) {
                    log.warn("Name clash of the attributes [{}] and [{}] for type {}", new Object[]{tempAttrDef.getName(), _attrDef.getName(), this.getTypeName()});
                }
                return false;
            } else {
                return true;
            }
        }
    }

    protected AttributeDef fixAttribute(AttributeDef _attrDef, int _index) {
        StringBuilder tempBuilder = new StringBuilder();

        for(int i = 0; i < _index; i++) {
            tempBuilder.append(ESCAPE_SEQUENCE);
        }

        tempBuilder.append(_attrDef.getName());
        String tempAttrName = tempBuilder.toString();
        String tempGetterName = ClassDesignerUtil.getGetterName(tempAttrName);
        String tempSetterName = ClassDesignerUtil.getSetterName(tempAttrName);
        if (log.isWarnEnabled()) {
            log.warn("Replacing getter [{}] with [{}] for type {}", new Object[]{_attrDef.getGetterName(), tempGetterName, this.getTypeName()});
            log.warn("Replacing setter [{}] with [{}] for type {}", new Object[]{_attrDef.getSetterName(), tempSetterName, this.getTypeName()});
        }

        AttributeDefImpl tempAttrDef = new AttributeDefImpl(_attrDef);
        tempAttrDef.setGetterName(tempGetterName);
        tempAttrDef.setSetterName(tempSetterName);
        return tempAttrDef;
    }
}
