package com.twinkle.framework.ruleengine.rule;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.context.model.NormalizedContext;
import com.twinkle.framework.ruleengine.rule.operation.AttributeOperation;
import com.twinkle.framework.ruleengine.rule.operation.MapOperation;
import com.twinkle.framework.ruleengine.rule.operation.MathOperation;
import com.twinkle.framework.ruleengine.utils.MapHash;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-07-18 14:06<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class AdornmentRule extends AbstractRule{

    private AttributeOperation attrOperation;
    /**
     * Will be used by Map Operation,
     * to load the map content from URL. Usually from a local file or web URL.
     */
    private MapHash mapHash;
    public AdornmentRule() {
        super();
        log.info("AdornmentRule.initialized().");
    }
    @Override
    public void configure(JSONObject _conf) throws ConfigurationException {
        JSONArray tempArray = _conf.getJSONArray("AdornOps");
        if (tempArray == null) {
            throw new ConfigurationException(ExceptionCode.RULE_MADANTORY_ATTR_MISSED, "AdornmentRule.configure(): AdornOps is a mandatory parameter. ");
        } else {
            String tempUrl = _conf.getString("MapURL");
            JSONArray tempURLCommentsJsonArray = _conf.getJSONArray("MapURLComments");
            String[] tempURLCommnetArray = tempURLCommentsJsonArray.toArray(new String[]{});
            boolean tempUseArrayFlag = _conf.getBooleanValue("MapUseArray");
            boolean tempEnableRefresh = _conf.getBooleanValue("MapRefresh");
            String tempOperator = _conf.getString("MapSeparator");
            this.mapHash = MapHash.createMapHash(tempUrl, tempURLCommnetArray, tempUseArrayFlag, tempEnableRefresh, tempOperator);
            this.loadOperations(tempArray.toArray(new String[]{}), _conf);
        }
    }

    public void loadOperations(String[] _operations, JSONObject _conf) throws ConfigurationException {
        log.debug("AdornmentRule.loadOperations()");
        AttributeOperation tempPreOperation = null;
        AttributeOperation tempOperation = null;

        for(int i = 0; i < _operations.length; ++i) {
            StringTokenizer tempTokenizer = new StringTokenizer(_operations[i]);
            String tempToken = tempTokenizer.nextToken();
            tempPreOperation = tempOperation;

            try {
                tempOperation = (AttributeOperation)AdornmentRule.Operation.getOperation(tempToken).newInstance();
                tempOperation.configure(_conf);
            } catch (Exception e) {
                throw new ConfigurationException(ExceptionCode.RULE_ADN_OPERATION_INIT_FAILED, "Cannot create instance of operation class for operation-" + _operations[i]);
            }

            tempOperation.loadOperation(_operations[i]);
            if (tempOperation instanceof MapOperation) {
                ((MapOperation)tempOperation).setMapHash(this.mapHash);
            }
            // add the next rule into the pre-rule.
            if (tempPreOperation != null) {
                tempPreOperation.addNextRule(tempOperation);
            }

            if (i == 0) {
                this.attrOperation = tempOperation;
            }
        }

    }
    @Override
    public void applyRule(NormalizedContext _context) throws RuleException {
        log.debug("AdornmentRule.applyRule()");

        try {
            this.attrOperation.applyRule(_context);
        } catch (RuntimeException e) {
            throw new RuleException(ExceptionCode.RULE_APPLY_ERROR, "Error in applying the rule error.", e);
        }

        if (this.nextRule != null) {
            this.nextRule.applyRule(_context);
        }
    }

    public static class Operation {
        private static final Map byName = new HashMap();
//        public static final AdornmentRule.Operation MIN = new AdornmentRule.Operation("min", MinOperation.class);
//        public static final AdornmentRule.Operation MAX = new AdornmentRule.Operation("max", MaxOperation.class);
//        public static final AdornmentRule.Operation SETNULL = new AdornmentRule.Operation("setifnull", SetIfNullOperation.class);
//        public static final AdornmentRule.Operation COPYNULL = new AdornmentRule.Operation("copyifnull", CopyIfNullOperation.class);
//        public static final AdornmentRule.Operation SETSYSTIME = new AdornmentRule.Operation("setsystime", SetSystemTime.class);
//        public static final AdornmentRule.Operation SET = new AdornmentRule.Operation("set", SetOperation.class);
//        public static final AdornmentRule.Operation MOVE = new AdornmentRule.Operation("move", MoveOperation.class);
//        public static final AdornmentRule.Operation COPY = new AdornmentRule.Operation("copy", CopyOperation.class);
//        public static final AdornmentRule.Operation SWAP = new AdornmentRule.Operation("swap", SwapOperation.class);
//        public static final AdornmentRule.Operation COPYFROM = new AdornmentRule.Operation("copyfrom", CopyFromOperation.class);
//        public static final AdornmentRule.Operation COPYTO = new AdornmentRule.Operation("copyto", CopyToOperation.class);
//        public static final AdornmentRule.Operation MAP = new AdornmentRule.Operation("map", MapOperation.class);
//        public static final AdornmentRule.Operation DIGEST = new AdornmentRule.Operation("digest", DigestOperation.class);
//        public static final AdornmentRule.Operation BASE64_ENCODE = new AdornmentRule.Operation("encBase64", Encode.class);
//        public static final AdornmentRule.Operation BASE64_DECODE = new AdornmentRule.Operation("decBase64", Decode.class);
//        public static final AdornmentRule.Operation SECRET_KEY_GEN = new AdornmentRule.Operation("genKey", GenKey.class);
//        public static final AdornmentRule.Operation ENCRYPT = new AdornmentRule.Operation("encrypt", Encrypt.class);
//        public static final AdornmentRule.Operation DECRYPT = new AdornmentRule.Operation("decrypt", Decrypt.class);
//        public static final AdornmentRule.Operation HMAC = new AdornmentRule.Operation("hMac", Hmac.class);
        private final String name;
        private final Class clazz;
        private static final Class MATH_CLASS = MathOperation.class;

        private Operation(String _name, Class _class) {
            this.name = _name;
            this.clazz = _class;
            byName.put(_name, this);
        }

        public static Class getOperation(String _operation) {
            if (_operation != null && _operation.length() != 0) {
                if (!Character.isLetter(_operation.charAt(0))) {
                    return MATH_CLASS;
                } else {
                    AdornmentRule.Operation tempOperation = (AdornmentRule.Operation)byName.get(_operation);
                    if (tempOperation != null) {
                        return tempOperation.clazz;
                    } else {
                        try {
                            return Class.forName("com.twinkle.framework.ruleengine.rule.attributeops." + Character.toUpperCase(_operation.charAt(0)) + _operation.substring(1) + "Operation");
                        } catch (Throwable ex) {
                            AdornmentRule.log.error("AdornmentRuleMsg1-Operation Initialize Failed.", ex);
                            throw new IllegalArgumentException("Invalid operation name: [" + _operation + "]");
                        }
                    }
                }
            } else {
                throw new IllegalArgumentException("Operation name cannot be null or empty");
            }
        }
    }
}
