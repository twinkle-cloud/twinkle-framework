package com.twinkle.framework.datacenter.statement;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.component.AbstractConfigurableComponent;
import com.twinkle.framework.api.component.datacenter.ISqlStatement;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.datacenter.support.HybridAttribute;
import com.twinkle.framework.datacenter.support.WhereClauseSupport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/16/19 4:25 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractConfigurableSqlStatement extends AbstractConfigurableComponent implements ISqlStatement {
    /**
     * The batch size for batch insert or update.
     */
    protected static final int BATCH_SIZE = 1000;

    /**
     * The database fields array, which will be used by this statement.
     */
    protected String[] dbFieldArray;
    /**
     * The database field type array.
     * <p>
     * Refer to java.sql.Types.
     */
    protected int[] dbFieldTypeArray;
    /**
     * The attributes which will be used by this statement,
     * Set the fetched values into the attributes,
     * or update the database fields with the attributes' value.
     */
    protected HybridAttribute[] attributeArray;
    /**
     * The default value for the database field, or for the attribute.
     */
    protected String[] defaultValue;
    /**
     * The where condition.
     */
    protected String whereQuery;
    /**
     * The attributes which will be used in where clause by this statement,
     * Set the fetched values into the attributes,
     * or update the database fields with the attributes' value.
     */
    protected HybridAttribute[] conditionAttrArray;
    /**
     * The Condition Fields type array.
     */
    protected int[] conditionFieldTypeArray;
    /**
     * The Result
     */
    protected HybridAttribute resultIndexAttributeIndex;
    /**
     * The Result Attribute Index's HybridAttribute.
     */
    protected HybridAttribute resultAttributeIndex;
    /**
     * Error Name'a Hybrid Attribute.
     */
    protected HybridAttribute errorNameAttribute;
    /**
     * The Error message's Hybrid Attribute.
     */
    protected HybridAttribute errorMessageAttribute;

    protected PrimitiveAttributeSchema primitiveAttributeSchema;
    /**
     * The JDBC template, From Spring Bean context.
     */
    @Autowired
    protected NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * The SQL will be executed by this statement.
     */
    @Getter
    private String preparedSQL;

    public AbstractConfigurableSqlStatement() {
        this.primitiveAttributeSchema = PrimitiveAttributeSchema.getInstance();
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        super.configure(_conf);
        if (_conf.isEmpty()) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_DATACENTER, "The configuration for SQL statement is empty.");
        }
        JSONArray tempArray = _conf.getJSONArray("FieldMap");
        if (tempArray != null && !tempArray.isEmpty()) {
            this.dbFieldArray = new String[tempArray.size()];
            this.dbFieldTypeArray = new int[tempArray.size()];
            this.attributeArray = new HybridAttribute[tempArray.size()];
            this.defaultValue = new String[tempArray.size()];
            String tempItemValue;
            for (int i = 0; i < tempArray.size(); i++) {
                JSONArray tempItemArray = tempArray.getJSONArray(i);
                if (tempItemArray.isEmpty()) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "The FieldMap item is empty.");
                }
                if (tempItemArray.size() < 3) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "The FieldMap item is invalid.");
                }
                this.dbFieldArray[i] = tempItemArray.getString(0);
                this.dbFieldTypeArray[i] = tempItemArray.getIntValue(1);
                tempItemValue = tempItemArray.getString(2);
                this.attributeArray[i] = new HybridAttribute(tempItemValue, tempItemArray.toJSONString());
                if (tempItemArray.size() > 3) {
                    tempItemValue = tempItemArray.getString(3);
                } else {
                    tempItemValue = null;
                }
                this.defaultValue[i] = tempItemValue;
            }
        }
        WhereClauseSupport tempSupport = new WhereClauseSupport();
        tempSupport.configure(_conf);
        this.whereQuery = tempSupport.getWhereQuery();
        this.conditionAttrArray = tempSupport.getConditionAttrArray();
        this.conditionFieldTypeArray = tempSupport.getConditionFieldTypeArray();

        String tempAttrName = _conf.getString("ResultIndexAttribute");
        if (!StringUtils.isBlank(tempAttrName)) {
            this.resultIndexAttributeIndex = new HybridAttribute(tempAttrName);
        } else {
            this.resultIndexAttributeIndex = null;
        }

        tempAttrName = _conf.getString("ResultAttribute");
        if (!StringUtils.isBlank(tempAttrName)) {
            this.resultAttributeIndex = new HybridAttribute(tempAttrName);
        } else {
            this.resultAttributeIndex = null;
        }

        tempAttrName = _conf.getString("ErrorNameAttribute");
        if (!StringUtils.isBlank(tempAttrName)) {
            this.errorNameAttribute = new HybridAttribute(tempAttrName);
        }

        tempAttrName = _conf.getString("ErrorMessageAttribute");
        if (!StringUtils.isBlank(tempAttrName)) {
            this.errorMessageAttribute = new HybridAttribute(tempAttrName);
        }
        this.preparedSQL = this.packSqlStatement();
    }

    /**
     * Build the Real sql statement which will be executed by JDBC Template.
     *
     * @return
     */
    protected abstract String packSqlStatement();
}
