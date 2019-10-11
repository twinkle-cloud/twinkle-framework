package com.twinkle.framework.ruleengine.rule.operation;

//import com.sun.deploy.security.CertUtils;
//import com.twinkle.framework.api.constant.ExceptionCode;
//import com.twinkle.framework.api.context.NormalizedContext;
//import com.twinkle.framework.api.exception.ConfigurationException;
//import com.twinkle.framework.api.exception.RuleException;
//import com.twinkle.framework.core.lang.BinaryAttribute;
//import com.twinkle.framework.core.lang.StringAttribute;
//import com.twinkle.framework.ruleengine.rule.support.OperationAttributeInfo;
//import com.twinkle.framework.ruleengine.utils.CipherUtil;
//import lombok.extern.slf4j.Slf4j;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.StringTokenizer;
//
///**
// * Function: TODO ADD FUNCTION. <br/>
// * Reason:	 TODO ADD REASON. <br/>
// * Date:     10/10/19 10:24 PM<br/>
// *
// * @author chenxj
// * @see
// * @since JDK 1.8
// */
//@Slf4j
//public abstract class AbstractCipherOperation extends AbstractConfigurableOperation {
//    protected OperationAttributeInfo algorithm_;
//    protected OperationAttributeInfo key_;
//    protected OperationAttributeInfo iv_;
//    protected OperationAttributeInfo source_;
//    protected OperationAttributeInfo target_;
//    protected Cipher cipher_ = null;
//
//    public AbstractCipherOperation() {
//    }
//
//    protected void loadOperation(String var1, String var2, String var3) throws ConfigurationException {
//        log.debug(String.format("CryptoOperations.%s.loadOperation(%s)", var3, var1));
//        StringTokenizer var4 = new StringTokenizer(var1);
//        if (var4.countTokens() != 6) {
//            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, String.format("In CryptoOperations.%s.loadOperation(): operation incorrect number of fields (%s)", var3, var1));
//        }
//        String var5 = var4.nextToken();
//        if (!var2.equals(var5)) {
//            throw new ConfigurationException(ExceptionCode.LOGIC_CONF_INVALID_EXPRESSION, String.format("In CryptoOperations.%s.loadOperation(): only %s operation supported, not (%s)", var3, var2, var5));
//        } else {
//            String var6 = var4.nextToken();
//            String var7 = var4.nextToken();
//            String var8 = var4.nextToken().trim();
//            if ("-".equals(var7)) {
//                log.info("CipherOperationsMsg15", var1);
//                this.key_ = null;
//            } else {
//                this.cipher_ = CipherUtil.getCipherByName(var6, var3.toLowerCase());
//                if (this.cipher_ == null) {
//                    this.algorithm_ = (new OperationAttributeInfo(var6)).checkIfCompatibleWith(StringAttribute.class);
//                }
//
//                this.key_ = (new OperationAttributeInfo(var7)).checkIfCompatibleWith(BinaryAttribute.class);
//                if (!"-".equals(var8)) {
//                    this.iv_ = (new OperationAttributeInfo(var8)).checkIfCompatibleWith(BinaryAttribute.class);
//                }
//            }
//
//            this.source_ = (new OperationAttributeInfo(var4.nextToken())).checkIfCompatibleWith(BinaryAttribute.class);
//            this.target_ = (new OperationAttributeInfo(var4.nextToken())).checkIfCompatibleWith(BinaryAttribute.class);
//            this.initialized = true;
//            log.debug(String.format("CryptoOperations.%s.loadOperation() OK", var3));
//        }
//    }
//
//    protected void transform(NormalizedContext var1, int var3) throws RuleException {
//        String var4 = var3 == 1 ? "Encryption" : "Decryption";
//        byte[] var5 = ((BinaryAttribute) this.source_.getAttributeIfPresent(var1)).getByteArray();
//        byte[] var6 = CipherOperations.EMPTY;
//
//        try {
//            if (this.key_ == null) {
//                var6 = var3 == 1 ? CertUtils.encrypt(var5) : CertUtils.decrypt(var5);
//            } else {
//                byte[] var7 = ((BinaryAttribute) this.key_.getAttributeIfPresent(var1)).getByteArray();
//                byte[] var8 = null;
//                if (this.iv_ != null) {
//                    var8 = ((BinaryAttribute) this.iv_.getAttribute(var1)).getByteArray();
//                }
//
//                log.debug("Using Key: " + this.key_.getAttribute(var1).toString() + " for " + var4);
//                Cipher var9 = this.cipher_ != null ? this.cipher_ : CipherUtil.getCipher(this.algorithm_, var1);
//                log.debug("Cipher: " + var9.getAlgorithm());
//                synchronized (var9) {
//                    SecretKeySpec var11 = new SecretKeySpec(var7, CipherUtil.extractName(var9.getAlgorithm()));
//                    if (var8 != null) {
//                        log.debug("Using IV from NME");
//                        IvParameterSpec var12 = new IvParameterSpec(var8);
//                        var9.init(var3, var11, var12);
//                    } else {
//                        log.debug("No IV from NME");
//                        var9.init(var3, var11);
//                    }
//
//                    var6 = var9.doFinal(var5);
//                    if (this.iv_ != null && var8 == null && var3 == 1) {
//                        log.debug("Storing IV to NME");
//                        var8 = var9.getIV();
//                        ((BinaryAttribute) this.iv_.getAttribute(var1)).setValue(var8);
//                    }
//                }
//            }
//        } catch (Throwable var15) {
//            log.error("CipherOperationsMsg14", var4, var15);
//        }
//
//        ((BinaryAttribute) this.target_.getAttribute(var1)).setValue(var6);
//    }
//}
