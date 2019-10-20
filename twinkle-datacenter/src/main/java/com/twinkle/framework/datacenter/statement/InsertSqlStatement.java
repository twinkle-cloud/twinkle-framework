package com.twinkle.framework.datacenter.statement;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.DataCenterException;
import com.twinkle.framework.datacenter.support.HybridAttribute;
import com.twinkle.framework.datacenter.utils.JDBCUtil;
import com.twinkle.framework.datasource.annotation.TwinkleDataSource;
import com.twinkle.framework.struct.error.AttributeNotSetException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/16/19 10:01 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class InsertSqlStatement extends AbstractSqlStatement {
    /**
     * The destination table's name.
     */
    private String destTableName;
    /**
     * Is batch or not?
     */
    private boolean batchFlag = false;
    /**
     * If batch model, this attribute is required.
     * <p>
     * Source Attribute name, The attribute type should be List or Array.
     * If the sub-attribute of an Struct Attribute is array, then could be set to
     * xxx.aaa
     */
    private HybridAttribute sourceAttribute;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        super.configure(_conf);
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
            return;
        } else {
            if (this.batchFlag) {
                this.sourceAttribute = new HybridAttribute(tempSourceAttrName);
                if (!this.sourceAttribute.isArrayFlag()) {
                    throw new ConfigurationException(ExceptionCode.LOGIC_CONF_ATTR_TYPE_IS_UNEXPECTED, "The attribute [" + tempSourceAttrName + "]'s type should be array or list type.");
                }
            }
        }
    }

    @Override
    protected String packSqlStatement() {
        StringBuffer tempBuffer = new StringBuffer("INSERT INTO ");
        tempBuffer.append(this.destTableName);
        tempBuffer.append(" (");
        StringBuffer tempValueBuffer = new StringBuffer();
        for (int i = 0; i < this.dbFieldArray.length; i++) {
            if (i == 0) {
                tempBuffer.append(this.dbFieldArray[i]);
                tempValueBuffer.append("?");
            } else {
                tempBuffer.append(",");
                tempBuffer.append(this.dbFieldArray[i]);
                tempValueBuffer.append(",?");
            }
        }
        tempBuffer.append(") VALUES (");
        tempBuffer.append(tempValueBuffer.toString());
        tempBuffer.append(")");
        log.debug("The Insert original SQL is: {}", tempBuffer.toString());
        return tempBuffer.toString();
    }

    @TwinkleDataSource(value = "#_dataSource")
    @Override
    public void execute(NormalizedContext _context, String _dataSource) throws DataCenterException {
        int tempFromIndex = 0;
        int tempToIndex = BATCH_SIZE;
        List<Object[]> tempBatchList = this.prepareValueArrays(_context);
        while (tempFromIndex != tempBatchList.size()) {
            if (tempToIndex > tempBatchList.size()) {
                tempToIndex = tempBatchList.size();
            }
            int[] tempResult = this.jdbcTemplate.batchUpdate(this.packSqlStatement(), tempBatchList.subList(tempFromIndex, tempToIndex));
            tempFromIndex = tempToIndex;
            tempToIndex += BATCH_SIZE;
            if (tempToIndex > tempBatchList.size()) {
                tempToIndex = tempBatchList.size();
            }
            log.debug("This batch insert result is: {}", tempResult);
        }
    }

    /**
     * Pack the Batch values array.
     *
     * @param _context
     * @return
     */
    private List<Object[]> prepareValueArrays(NormalizedContext _context) {
        int tempSize = 1;
        List<Object[]> tempBatchList;
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
     * Pack db fields' value array.
     *
     * @param _context
     * @param _rowIndex
     * @return
     */
    private Object[] packValuesArray(NormalizedContext _context, int _rowIndex) {
        Object[] tempObjArray = new Object[this.attributeArray.length];
        for (int j = 0; j < this.attributeArray.length; j++) {
            Object tempObj = null;
            try {
                tempObj = this.attributeArray[j].getObjectValue(_context, this.dbFieldTypeArray[j], _rowIndex);
                if (tempObj == null) {
                    if (this.defaultValue[j] != null) {
                        tempObj = JDBCUtil.getSqlObjectDefaultValue(this.defaultValue[j], this.dbFieldTypeArray[j]);
                    }
                }
            } catch (AttributeNotSetException e) {
                log.warn("The Attribute has not been initialized, so use the default value.");
                if (this.defaultValue[j] != null) {
                    tempObj = JDBCUtil.getSqlObjectDefaultValue(this.defaultValue[j], this.dbFieldTypeArray[j]);
                }
            }
            tempObjArray[j] = tempObj;
        }
        return tempObjArray;
    }
}
