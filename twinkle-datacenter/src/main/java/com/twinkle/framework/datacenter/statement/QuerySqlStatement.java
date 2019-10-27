package com.twinkle.framework.datacenter.statement;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.DataCenterException;
import com.twinkle.framework.datacenter.support.HybridAttribute;
import com.twinkle.framework.datasource.annotation.TwinkleDataSource;
import com.twinkle.framework.struct.error.AttributeNotSetException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/24/19 3:26 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class QuerySqlStatement extends AbstractSqlStatement {
    /**
     * From tables, can be 1 or 1more tables join model.
     */
    private String fromTables;
    /**
     * To keep multi rows result.
     * <p>
     * Source Attribute name, The attribute type should be List or Array.
     * If the sub-attribute of an Struct Attribute is array, then could be set to
     * xxx.aaa
     */
    protected HybridAttribute destAttribute;
    /**
     * DB Field name without owner and AS name.
     */
    private String[] simpleDbFieldArray;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.fromTables = _conf.getString("FromTables");
        if (StringUtils.isBlank(this.fromTables)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "From tables is mandatory for Query Statement.");
        }
        super.configure(_conf);
        if (StringUtils.isBlank(this.whereQuery)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "For safety reason, please set the where clause.");
        }
        String tempDestAttrName = _conf.getString("DestAttribute");
        if (StringUtils.isBlank(tempDestAttrName)) {
            log.info("There is no dest attribute, so will keep ONLY first row of the result, and place the result values into primitive attributes.");
        } else {
            this.destAttribute = new HybridAttribute(tempDestAttrName);
            if (!this.destAttribute.isArrayFlag()) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_TYPE_IS_UNEXPECTED, "The attribute [" + destAttribute + "]'s type should be array or list type.");
            }
        }
        this.simpleDbFieldArray = new String[this.dbFieldArray.length];
        for (int i = 0; i < this.dbFieldArray.length; i++) {
            String tempFieldName = this.dbFieldArray[i].trim();
            int tempIndex = tempFieldName.lastIndexOf(' ');
            if(tempIndex >= 0) {
                this.simpleDbFieldArray[i] = tempFieldName.substring(tempIndex + 1);
                continue;
            }
            tempIndex = tempFieldName.indexOf('.');
            if(tempIndex >= 0) {
                this.simpleDbFieldArray[i] = tempFieldName.substring(tempIndex + 1);
            } else {
                this.simpleDbFieldArray[i] = tempFieldName;
            }
        }
    }

    @Override
    protected String packSqlStatement() {
        StringBuffer tempBuffer = new StringBuffer("SELECT ");
        StringBuffer tempValueBuffer = new StringBuffer();
        for (int i = 0; i < this.dbFieldArray.length; i++) {
            if (i == 0) {
                tempBuffer.append(this.dbFieldArray[i]);
            } else {
                tempBuffer.append(", ");
                tempBuffer.append(this.dbFieldArray[i]);
            }
        }
        tempBuffer.append(" FROM ");
        tempBuffer.append(this.fromTables);
        tempBuffer.append(" WHERE ");
        tempBuffer.append(this.whereQuery);
        log.debug("The query original SQL is: {}", tempBuffer.toString());
        return tempBuffer.toString();
    }

    @TwinkleDataSource(value = "#_dataSource")
    @Override
    public void execute(NormalizedContext _context, String _dataSource) throws DataCenterException {
        log.debug("Going to apply query statement [{}].", this.getPreparedSQL());
        SqlParameterSource tempParameterSource = this.packValuesArray(_context, -1);
        SqlRowSet tempRowSet = this.jdbcTemplate.queryForRowSet(this.getPreparedSQL(), tempParameterSource);
        int tempIndex = 0;
        while (tempRowSet.next()) {
            for (int i = 0; i < this.dbFieldArray.length; i++) {
                HybridAttribute tempAttr = this.attributeArray[i];
                try {
                    tempAttr.setValue(_context, tempRowSet, this.simpleDbFieldArray[i], this.dbFieldTypeArray[i], tempIndex, this.defaultValue[i]);
                } catch (SQLException e) {
                    log.error("Extract the field []'s value failed. Exception: e", this.dbFieldArray[i], e);
                    throw new DataCenterException(ExceptionCode.DATACENTER_QUERY_EXTRACT_FAILED, "Extract the field [" + this.dbFieldTypeArray[i] + "]'s value failed, Please check the Field and Attribute's type.");
                }
            }
            if (this.destAttribute == null) {
                break;
            }
        }
    }

    /**
     * Pack the parameters' values for the where clause.
     *
     * @param _context
     * @param _rowIndex
     * @return
     */
    private SqlParameterSource packValuesArray(NormalizedContext _context, int _rowIndex) {
        MapSqlParameterSource tempSource = new MapSqlParameterSource();
        for (int j = 0; j < this.conditionAttrArray.length; j++) {
            Object tempObj = null;
            try {
                tempObj = this.conditionAttrArray[j].getObjectValue(_context, this.conditionFieldTypeArray[j], _rowIndex);
            } catch (AttributeNotSetException e) {
                log.warn("The Attribute has not been initialized, so use null value.");
            }
            tempSource.addValue(this.conditionAttrArray[j].getAttributeName(), new SqlParameterValue(this.conditionFieldTypeArray[j], tempObj));
        }
        return tempSource;
    }

}
