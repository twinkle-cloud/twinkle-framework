package com.twinkle.framework.datacenter.statement;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.component.AbstractComponent;
import com.twinkle.framework.api.component.datacenter.ISqlStatement;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.context.PrimitiveAttributeSchema;
import com.twinkle.framework.datacenter.support.HybridAttribute;
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
public abstract class AbstractSqlStatement extends AbstractComponent implements ISqlStatement {
    /**
     * The batch size for batch insert or update.
     */
    protected static final int BATCH_SIZE = 1000;
    /**
     * Data Source name.
     */
    @Getter
    private String dataSourceName;
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

    public AbstractSqlStatement() {
        this.primitiveAttributeSchema = PrimitiveAttributeSchema.getInstance();
    }

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        if (_conf.isEmpty()) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_DATACENTER, "The configuration for SQL statement is empty.");
        }
        this.dataSourceName = _conf.getString("DataSource");
        if (StringUtils.isBlank(this.dataSourceName)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The DataSource is mandatory for SQL Statement Component.");
        }
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
