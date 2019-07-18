package com.twinkle.framework.ruleengine.rule.operation;

import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.context.model.NormalizedContext;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.core.lang.INumericAttribute;
import com.twinkle.framework.ruleengine.utils.MapHash;
import com.twinkle.framework.ruleengine.utils.TreeMarker;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-18 17:10<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class MapOperation extends AttributeOperation {
    private String defaultValue;
    private int srcIndex = -1;
    private int dstIndex = -1;
    private boolean isSrcTree = false;
    private boolean isDstTree = false;
    private Map mapHash;
    private String[] mapArray;

    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        super.configure(_conf);
    }

    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("MapOperation::applyRule()");

        Attribute tempDstAttr = _context.getAttribute(this.dstIndex);
        if (tempDstAttr == null) {
            tempDstAttr = this.contextSchema.newAttributeInstance(this.dstIndex);
            _context.setAttribute(tempDstAttr, this.dstIndex);
        }

        Attribute tempSrcAttr = _context.getAttribute(this.srcIndex);
        String tempSrcAttrStr = null;
        if (this.mapHash != null) {
            tempSrcAttrStr = (String)this.mapHash.get(tempSrcAttr.toString());
        } else {
            int tempAttrValue = ((INumericAttribute)tempSrcAttr).getInt();
            if (tempAttrValue >= 0 && tempAttrValue < this.mapArray.length) {
                tempSrcAttrStr = this.mapArray[tempAttrValue];
            } else {
                tempSrcAttrStr = null;
            }
        }

        if (tempSrcAttrStr == null) {
            if (this.defaultValue != null) {
                tempSrcAttrStr = this.defaultValue;
            } else {
                tempSrcAttrStr = tempSrcAttr.toString();
            }
        }

        tempDstAttr.setValue(tempSrcAttrStr);
        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        StringTokenizer tempST = new StringTokenizer(_operation);
        if (tempST.countTokens() < 3) {
            throw new ConfigurationException(ExceptionCode.RULE_ADN_MAP_OPERATION_INVALID, "In MapOperation.loadOperation(): operation missing fields (" + _operation + ")");
        } else {
            String tempOprToken = tempST.nextToken();
            if (!tempOprToken.equals("map")) {
                throw new ConfigurationException(ExceptionCode.RULE_ADN_MAP_OPERATION_INVALID, "In MapOperation.loadOperation(): only map operation supported, not (" + _operation + ")");
            } else {
                String tempToken = tempST.nextToken();
                if (TreeMarker.isTreeAttribute(tempToken)) {
                    this.isSrcTree = true;
                    tempToken = TreeMarker.extractAttributeName(tempToken);
                }

                this.srcIndex = this.contextSchema.getAttributeIndex(tempToken, _operation);
                tempToken = tempST.nextToken();
                if (TreeMarker.isTreeAttribute(tempToken)) {
                    this.isDstTree = true;
                    tempToken = TreeMarker.extractAttributeName(tempToken);
                }

                this.dstIndex = this.contextSchema.getAttributeIndex(tempToken, _operation);
                if (tempST.hasMoreTokens()) {
                    this.defaultValue = tempST.nextToken();
                } else {
                    this.defaultValue = null;
                }
            }
        }
    }

    /**
     * Set the Map-Hash.
     *
     * @param _mapHash
     * @throws ConfigurationException
     */
    public void setMapHash(MapHash _mapHash) throws ConfigurationException {
        if (_mapHash != null) {
            this.mapHash = _mapHash.getMapHash();
            this.mapArray = _mapHash.getMapArray();
        } else {
            throw new ConfigurationException(ExceptionCode.RULE_ADN_OPERATION_MAP_MISSED, "MapOperation:createMapHash(), map file is null");
        }
    }
}
