package com.twinkle.framework.datacenter.statement;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.DataCenterException;
import com.twinkle.framework.datacenter.support.HybridAttribute;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/23/19 10:01 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public abstract class AbstractUpdateSqlStatement extends AbstractSqlStatement {
    /**
     * The destination table's name.
     */
    protected String destTableName;
    /**
     * Is batch or not?
     */
    protected boolean batchFlag = false;
    /**
     * If batch model, this attribute is required.
     * <p>
     * Source Attribute name, The attribute type should be List or Array.
     * If the sub-attribute of an Struct Attribute is array, then could be set to
     * xxx.aaa
     */
    protected HybridAttribute sourceAttribute;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        this.destTableName = _conf.getString("DestTable");
        if (StringUtils.isBlank(this.destTableName)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The destination table name is required for insert statement.");
        }
        this.batchFlag = _conf.getBooleanValue("IsBatch");
        String tempSourceAttrName = _conf.getString("SourceAttribute");
        if (StringUtils.isBlank(tempSourceAttrName)) {
            if (this.batchFlag) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_REQUIRED_ATTR_MISSED, "The SourceAttribute is required for insert statement batch model.");
            }
            log.debug("There is no source attribute, so will use primitive attributes as the values.");
        } else {
            if (this.batchFlag) {
                this.sourceAttribute = new HybridAttribute(tempSourceAttrName);
                if (!this.sourceAttribute.isArrayFlag()) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_TYPE_IS_UNEXPECTED, "The attribute [" + tempSourceAttrName + "]'s type should be array or list type.");
                }
            }
        }
        super.configure(_conf);
    }

    @DS(value = "#_dataSource")
    @Override
    public void execute(NormalizedContext _context, String _dataSource) throws DataCenterException {
        List<SqlParameterSource> tempBatchList = this.prepareValueArrays(_context);
        if (this.batchFlag) {
            if (tempBatchList.size() == 0) {
                log.info("The [{}]'s batch size is 0, so do nothing this time.", this.destTableName);
                return;
            }
            log.info("Going to execute [{}]'s statement in batch model, and the batch size is [{}].", this.destTableName, tempBatchList.size());
            this.executeBatch(_context, tempBatchList);
        } else {
            log.info("Going to execute [{}]'s statement in single model.", this.destTableName);
            this.executeSingle(_context, tempBatchList.get(0));
        }
    }

    /**
     * Pack the Batch values array.
     *
     * @param _context
     * @return
     */
    protected List<SqlParameterSource> prepareValueArrays(NormalizedContext _context) {
        int tempSize = 1;
        List<SqlParameterSource> tempBatchList;
        if (this.batchFlag) {
            tempSize = this.sourceAttribute.getArraySize(_context);
            tempBatchList = new ArrayList<>(tempSize);
            for (int i = 0; i < tempSize; i++) {
                tempBatchList.add(this.packValuesArray(_context, i));
            }
        } else {
            tempBatchList = new ArrayList<>(tempSize);
            tempBatchList.add(this.packValuesArray(_context, -1));
        }
        return tempBatchList;
    }

    /**
     * Execute the statement with single row.
     *
     * @param _context
     * @param _sqlSource
     */
    protected abstract void executeSingle(NormalizedContext _context, SqlParameterSource _sqlSource);

    /**
     * Execute the statement in batch model.
     *
     * @param _context
     * @param _sqlSourceList
     */
    protected abstract void executeBatch(NormalizedContext _context, List<SqlParameterSource> _sqlSourceList);

    /**
     * Pack the single row parameters.
     *
     * @param _context
     * @param _rowIndex
     * @return
     */
    protected abstract SqlParameterSource packValuesArray(NormalizedContext _context, int _rowIndex);
}
