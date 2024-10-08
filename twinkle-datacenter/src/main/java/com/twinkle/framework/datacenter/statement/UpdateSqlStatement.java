package com.twinkle.framework.datacenter.statement;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/20/19 2:07 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class UpdateSqlStatement extends InsertSqlStatement {
    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        super.configure(_conf);
        if(StringUtils.isBlank(this.whereQuery)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "For safety reason, please set the where clause.");
        }
        this.attributeArray = ArrayUtils.addAll(this.attributeArray, this.conditionAttrArray);
        this.dbFieldTypeArray = ArrayUtils.addAll(this.dbFieldTypeArray, this.conditionFieldTypeArray);
    }

    @Override
    protected String packSqlStatement() {
        StringBuilder tempBuffer = new StringBuilder("UPDATE ");
        tempBuffer.append(this.destTableName);
        tempBuffer.append(" SET ");
        for (int i = 0; i < this.dbFieldArray.length; i++) {
            if (i == 0) {
                tempBuffer.append(this.dbFieldArray[i]);
                tempBuffer.append("=:");
                tempBuffer.append(this.attributeArray[i].getAttributeName());
            } else {
                tempBuffer.append(", ");
                tempBuffer.append(this.dbFieldArray[i]);
                tempBuffer.append("=:");
                tempBuffer.append(this.attributeArray[i].getAttributeName());
            }
        }
        tempBuffer.append(" WHERE ");
        tempBuffer.append(this.whereQuery);
        log.debug("The update original SQL is: {}", tempBuffer.toString());
        return tempBuffer.toString();
    }
}
