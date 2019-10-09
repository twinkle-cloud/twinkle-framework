package com.twinkle.framework.ruleengine.rule.operation;

import com.twinkle.framework.api.constant.ExceptionCode;
import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.api.exception.ConfigurationException;
import com.twinkle.framework.api.exception.RuleException;
import com.twinkle.framework.core.lang.Attribute;
import com.twinkle.framework.ruleengine.rule.support.ConcatenateDigest;
import com.twinkle.framework.ruleengine.rule.support.DummyDigest;
import com.twinkle.framework.ruleengine.rule.support.NullOutputStream;
import com.twinkle.framework.ruleengine.rule.support.OperationAttributeExtractor;
import lombok.extern.slf4j.Slf4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/7/19 10:00 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class DigestOperation extends AbstractConfigurableOperation {
    public static final String OP_CODE = "digest";
    protected final List<OperationAttributeExtractor> extractorList = new ArrayList<>();
    protected OperationAttributeExtractor targetExtractor;
    protected OperationAttributeExtractor digestExtractor;
    protected MessageDigest hashDigest = null;
    private static final String QUOTE = "\"";

    @Override
    public void loadOperation(String _operation) throws ConfigurationException {
        log.debug("DigestOperation.loadOperation({})", _operation);
        StringTokenizer tempTokenizer = new StringTokenizer(_operation);
        if (tempTokenizer.countTokens() < 4) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_EXPRESSION_FIELD_MISSED, "In DigestOperation.loadOperation(): operation missing fields (" + _operation + ")");
        }
        String tempOperator = tempTokenizer.nextToken();
        if (!OP_CODE.equals(tempOperator)) {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "In DigestOperation.loadOperation(): only digest operation supported, not (" + tempOperator + ")");
        }
        String tempToken = tempTokenizer.nextToken().trim();
        if ("\"\"".equals(tempToken)) {
            this.hashDigest = new ConcatenateDigest();
        } else if (tempToken.length() > 2 && tempToken.endsWith(QUOTE) && tempToken.startsWith(QUOTE)) {
            tempToken = tempToken.substring(1, tempToken.length() - 1);
            try {
                this.hashDigest = MessageDigest.getInstance(tempToken);
            } catch (NoSuchAlgorithmException e) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_DIGEST_NOT_SUPPORTED, "In DigestOperation.loadOperation(): unknown algorithm (" + _operation + ")");
            }
            log.info("DigestOperationMsg1", tempToken);
        } else {
            this.digestExtractor = new OperationAttributeExtractor(tempToken);
            if (this.digestExtractor.attributeHelper == null || !this.digestExtractor.attributeHelper.canHoldDigestName()) {
                throw new ConfigurationException(ExceptionCode.LOGIC_CONF_DIGEST_INVALID, "The " + this.digestExtractor.attrClass.toString() + " attribute (" + tempToken + ") cannot hold the digest name");
            }
            log.info("DigestOperationMsg2", tempToken);
        }

        int tempCountTokens = tempTokenizer.countTokens();
        for (int i = 0; i < tempCountTokens - 1; ++i) {
            String tempAttrName = tempTokenizer.nextToken();
            this.extractorList.add(new OperationAttributeExtractor(tempAttrName));
        }

        this.targetExtractor = new OperationAttributeExtractor(tempTokenizer.nextToken());
        if (this.targetExtractor.attributeHelper != null && this.targetExtractor.attributeHelper.canHoldDigest()) {
            this.initialized = true;
            log.debug("DigestOperation.loadOperation() completed.");
        } else {
            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, "Cannot store digest in the " + this.targetExtractor.attrClass.toString() + " attribute (" + this.targetExtractor.attrName + ")");
        }
    }

    @Override
    public void _applyRule(NormalizedContext _context) throws RuleException {
        Object tempDigest;
        if (this.hashDigest == null) {
            String tempDigestName = this.digestExtractor.attributeHelper.getDigestName(this.digestExtractor.getAttributeIfPresent(_context));
            if (tempDigestName == null) {
                throw new RuleException(ExceptionCode.RULE_APPLY_BASE64_DIGEST_IS_EMPTY, "The attribute with the digest algorithm name is empty.");
            }

            try {
                tempDigest = MessageDigest.getInstance(tempDigestName);
            } catch (NoSuchAlgorithmException e) {
                log.warn("The Digest Algorithm[{}] is not found.", tempDigestName);
                tempDigest = new DummyDigest(tempDigestName);
            }
        } else {
            tempDigest = this.hashDigest;
        }

        synchronized(tempDigest) {
            DigestOutputStream tempDigestOutputStream = new DigestOutputStream(NullOutputStream.NULL_OUTPUT_STREAM, (MessageDigest)tempDigest);
            DataOutputStream tempOutputStream = new DataOutputStream(tempDigestOutputStream);

            try {
                Iterator<OperationAttributeExtractor> tempItr = this.extractorList.iterator();

                while(true) {
                    if (!tempItr.hasNext()) {
                        tempOutputStream.close();
                        break;
                    }

                    OperationAttributeExtractor tempExtractor = tempItr.next();
                    tempExtractor.extractData(tempOutputStream, _context);
                }
            } catch (IOException e) {
                throw new RuleException(ExceptionCode.RULE_APPLY_DIGEST_ATTRIBUTE_EXTRACT_FAILED, "Attribute extraction failed", e);
            }

            Attribute tempAttr = this.targetExtractor.getAttribute(_context);
            this.targetExtractor.attributeHelper.setDigest(tempAttr, tempDigestOutputStream.getMessageDigest().digest());
        }
    }
}
