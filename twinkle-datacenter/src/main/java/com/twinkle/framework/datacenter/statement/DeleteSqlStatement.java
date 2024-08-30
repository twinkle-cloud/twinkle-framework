package com.twinkle.framework.datacenter.statement;

import com.alibaba.fastjson2.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.struct.error.AttributeNotSetException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/23/19 9:51 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class DeleteSqlStatement extends AbstractUpdateSqlStatement {
    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        super.configure(_conf);
        if (StringUtils.isBlank(this.whereQuery)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "For safety reason, please set the where clause.");
        }
    }

    @Override
    protected void executeSingle(NormalizedContext _context, SqlParameterSource _sqlSource) {
        int tempResult = this.jdbcTemplate.update(this.getPreparedSQL(), _sqlSource);
        if (tempResult > 0) {
            log.debug("The [{}] rows have been deleted from [{}] successfully.", tempResult, this.destTableName);
        } else {
            log.info("There is no row deleted from [{}] this time.", this.destTableName);
        }
    }

    @Override
    protected void executeBatch(NormalizedContext _context, List<SqlParameterSource> _sqlSourceList) {
        int tempFromIndex = 0;
        int tempToIndex = BATCH_SIZE;

        while (tempFromIndex <= _sqlSourceList.size()) {
            if (tempToIndex > _sqlSourceList.size()) {
                tempToIndex = _sqlSourceList.size();
            }
            List<SqlParameterSource> tempSubList = _sqlSourceList.subList(tempFromIndex, tempToIndex);
            int[] tempResult = this.jdbcTemplate.batchUpdate(this.getPreparedSQL(), tempSubList.toArray(new SqlParameterSource[]{}));
            log.debug("This batch [{}]->[{}] removed [{}] rows from [{}].", tempFromIndex, tempToIndex, tempResult, this.destTableName);
            tempFromIndex = tempToIndex;
            tempToIndex += BATCH_SIZE;
        }
    }

    @Override
    protected SqlParameterSource packValuesArray(NormalizedContext _context, int _rowIndex) {
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

    @Override
    protected String packSqlStatement() {
        StringBuffer tempBuffer = new StringBuffer("DELETE FROM ");
        tempBuffer.append(this.destTableName);
        tempBuffer.append(" WHERE ");
        tempBuffer.append(this.whereQuery);
        log.debug("The delete original SQL is: {}", tempBuffer.toString());
        return tempBuffer.toString();
    }
}
