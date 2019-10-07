package com.twinkle.framework.ruleengine.rule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.asm.factory.BeanFactory;
import com.twinkle.framework.asm.serialize.SerializerFactory;
import com.twinkle.framework.asm.serialize.TextSerializer;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.ruleengine.rule.support.EscapeString;
import com.twinkle.framework.ruleengine.rule.support.Level;
import com.twinkle.framework.struct.context.StructAttributeSchemaManager;
import com.twinkle.framework.struct.lang.StructAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 11:19 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class LogAttributeRule extends AbstractRule {
    private boolean printNullFlag;
    private boolean convertToJsonFlag = false;
    private boolean inSingleLineFlag = false;
    private int[] attrIndexArray;
    private String message;
    private String attributeSeparatorChar;
    private Level level;
    private String[] attributeArray;
    private TextSerializer<StructAttribute> jsonSerializer;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        log.debug("LogAttributeRule:configure()");
        String tempLevelStr = _conf.getString("LogLevel");
        if (tempLevelStr == null) {
            tempLevelStr = new String("DEBUG");
        }
        this.level = (Level) Level.parse(tempLevelStr);
        if (this.level == null) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_VALUE_INVALID, "Configured LogLevel=" + tempLevelStr + " in " + this.getFullPathName() + " is not a valid log level");
        }
        this.message = _conf.getString("Message");
        if (StringUtils.isBlank(this.message)) {
            this.message = "Attribute @ " + this.getFullPathName() + " is ";
        }

        JSONArray tempAttrArray = _conf.getJSONArray("Attributes");
        if (!tempAttrArray.isEmpty()) {
            this.attributeArray = tempAttrArray.toArray(new String[tempAttrArray.size()]);
            this.attrIndexArray = new int[this.attributeArray.length];

            for (int i = 0; i < this.attributeArray.length; i++) {
                this.attrIndexArray[i] = this.primitiveAttributeSchema.getAttributeIndex(this.attributeArray[i], "Attribute " + this.attributeArray[i] + " not configured in Attribute schema");
            }
        }
        this.printNullFlag = _conf.getBooleanValue("NullAlso");
        this.inSingleLineFlag = _conf.getBooleanValue("SingleLineStructAttribute");
        this.attributeSeparatorChar = _conf.getString("StructAttributeSeparatorCharacter");
        if (this.attributeSeparatorChar == null) {
            this.attributeSeparatorChar = System.getProperty("line.separator", "\n");
        }
        this.attributeSeparatorChar = (new EscapeString(this.attributeSeparatorChar)).getString();
        this.convertToJsonFlag = _conf.getBooleanValue("ConvertToJson");
        if (this.convertToJsonFlag) {
            BeanFactory tempBeanFactory = (BeanFactory) StructAttributeSchemaManager.getStructAttributeFactory();
            if (tempBeanFactory == null) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_BEAN_FACTORY_MISSING, "StructAttribute BeanFactory not configured");
            }

            SerializerFactory tempSerializerFactory = tempBeanFactory.getSerializerFactory("JSON.INTROSPECTION");
            if (tempSerializerFactory == null) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_SA_SERIALIZER_FACTORY_MISSING, "Failed to get SerializerFactory for JSON.INTROSPECTION format");
            }

            this.jsonSerializer = (TextSerializer) tempSerializerFactory.getSerializer("");
        }
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        StringBuilder tempBuilder = new StringBuilder();
        if (this.message != null) {
            tempBuilder.append(this.message);
        }
        if (this.attributeArray != null) {
            List<Attribute> tempStructAttributeList = new ArrayList<>(8);
            for (int i = 0; i < this.attributeArray.length; i++) {
                Attribute tempAttrItem = _context.getAttribute(this.attrIndexArray[i]);
                if (tempAttrItem != null && (tempAttrItem.getPrimitiveType() == Attribute.STRUCT_TYPE
                        || tempAttrItem.getPrimitiveType() == Attribute.LIST_STRUCT_ATTRIBUTE_TYPE)) {
                    tempStructAttributeList.add(tempAttrItem);
                    continue;
                }
                if (this.printNullFlag || tempAttrItem != null) {
                    tempBuilder.append(this.attributeArray[i] + "=" + _context.getAttribute(this.attrIndexArray[i]));
                    if (i < this.attributeArray.length - 1) {
                        tempBuilder.append(", ");
                    }
                }
            }
            for (Attribute tempAttrItem : tempStructAttributeList) {
                if (tempAttrItem.getPrimitiveType() == Attribute.STRUCT_TYPE) {
                    tempBuilder.append(this.attributeSeparatorChar);
                    StructAttribute tempAttr = (StructAttribute) tempAttrItem.getObjectValue();
                    String tempStr = this.convertToJsonFlag ? this.jsonSerializer.write(tempAttr) : tempAttr.toString();
                    tempBuilder.append(tempStr);
                } else {
                    List<StructAttribute> tempList = (List<StructAttribute>) tempAttrItem.getObjectValue();
                    if (CollectionUtils.isEmpty(tempList)) {
                        continue;
                    }
                    for (StructAttribute tempItem : tempList) {
                        tempBuilder.append(this.attributeSeparatorChar);
                        String tempStr = this.convertToJsonFlag ? this.jsonSerializer.write(tempItem) : tempItem.toString();
                        tempBuilder.append(tempStr);
                    }
                }
            }
        }
        String tempPrintStr = tempBuilder.toString();
        if(level.intValue() > 0 && level.intValue() < Level.DEBUG.intValue()) {
            log.trace(tempPrintStr);
        } else if(level.intValue() < Level.INFO.intValue()) {
            log.debug(tempPrintStr);
        } else if(level.intValue() < Level.WARNING.intValue()) {
            log.info(tempPrintStr);
        } else if(level.intValue() < Level.ERROR.intValue()) {
            log.warn(tempPrintStr);
        } else {
            log.error(tempPrintStr);
        }
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }
}
