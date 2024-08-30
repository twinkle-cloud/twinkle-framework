package com.twinkle.framework.datacenter.support;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.config.Configurable;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/21/19 4:12 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class WhereClauseSupport implements Configurable {
    /**
     * The where condition.
     */
    private String whereQuery;

    /**
     * The attributes which will be used in where clause by this statement,
     * Set the fetched values into the attributes,
     * or update the database fields with the attributes' value.
     */
    private HybridAttribute[] conditionAttrArray;
    /**
     * The Condition Fields type array.
     */
    private int[] conditionFieldTypeArray;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.whereQuery = _conf.getString("WhereClause");
        int tempNeedParaCount = 0;
        if (!StringUtils.isBlank(this.whereQuery)) {
            char[] tempWhereArray = this.whereQuery.toCharArray();
            for (char tempChar : tempWhereArray) {
                if (tempChar == ':') {
                    tempNeedParaCount++;
                }
            }
            JSONArray tempConditionFieldArray = _conf.getJSONArray("ConditionFields");
            if (tempNeedParaCount > 0 && (tempConditionFieldArray == null || tempConditionFieldArray.isEmpty())) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The ConditionFields is required for where clause.");
            }
            if (tempNeedParaCount > 0 && tempNeedParaCount > tempConditionFieldArray.size()) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The Condition fields are less than required, please check the clause expression.");
            }
            this.conditionAttrArray = new HybridAttribute[tempNeedParaCount];
            this.conditionFieldTypeArray = new int[tempNeedParaCount];
            for (int i = 0; i < tempNeedParaCount; i++) {
                JSONArray tempConditionArray = tempConditionFieldArray.getJSONArray(i);
                HybridAttribute tempAttr = new HybridAttribute(tempConditionArray.getString(0));
                this.conditionFieldTypeArray[i] = tempConditionArray.getIntValue(1);
                this.conditionAttrArray[i] = tempAttr;
            }
        }
    }
}
