package com.twinkle.framework.ruleengine.utils;

import com.twinkle.framework.api.context.NormalizedContext;
import com.twinkle.framework.core.lang.StringAttribute;
import com.twinkle.framework.ruleengine.rule.support.OperationAttributeInfo;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.security.GeneralSecurityException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     10/10/19 9:57 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class CipherUtil {
    public static Cipher getCipher(OperationAttributeInfo _attrInfo, NormalizedContext _context) throws Exception {
        String tempName = ((StringAttribute)_attrInfo.getAttributeIfPresent(_context)).getValue();
        return Cipher.getInstance(tempName);
    }

    public static KeyGenerator getKeyGenerator(OperationAttributeInfo _attrInfo, NormalizedContext _context) throws Exception {
        String tempName = ((StringAttribute)_attrInfo.getAttributeIfPresent(_context)).getValue();
        return KeyGenerator.getInstance(extractName(tempName));
    }

    public static Mac getMac(OperationAttributeInfo _attrInfo, NormalizedContext _context) throws Exception {
        String tempName = ((StringAttribute)_attrInfo.getAttributeIfPresent(_context)).getValue();
        return Mac.getInstance(tempName);
    }

    public static String extractName(String _str) {
        // Get the index of '/'
        int tempIndex = _str.indexOf(47);
        return tempIndex < 0 ? _str : _str.substring(0, tempIndex);
    }

    public static Cipher getCipherByName(String _name, String _expression) {
        if (_name.length() > 2 && _name.endsWith("\"") && _name.startsWith("\"")) {
            _name = _name.substring(1, _name.length() - 1);

            try {
                Cipher tempCipher = Cipher.getInstance(_name);
                log.info("Load cipher [{}] successfully.", new Object[]{_name, _expression});
                return tempCipher;
            } catch (GeneralSecurityException e) {
                log.error("Failed to initialize cipher: {}, Exception: {}", _name, e);
                String tempMsg = String.format("Failed to initialize cipher: [%s]", _name);
                throw new RuntimeException(tempMsg);
            }
        } else {
            if (_expression != null) {
                log.info("The cipher name [[]] is invalid, so return nothing.", new Object[]{_name, _expression});
            }

            return null;
        }
    }

    public static KeyGenerator getKeyGeneratorByName(String _name) {
        if (_name.length() > 2 && _name.endsWith("\"") && _name.startsWith("\"")) {
            _name = extractName(_name.substring(1, _name.length() - 1).trim());

            try {
                KeyGenerator tempGenerator = KeyGenerator.getInstance(_name);
                log.info("Load key generator [{}] successfully.", _name);
                return tempGenerator;
            } catch (GeneralSecurityException e) {
                log.error("Failed to initialize KeyGenerator for [{}], Exception: {}", _name, e);
                String tempMsg = String.format("Failed to initialize KeyGenerator for [%s]", _name);
                throw new RuntimeException(tempMsg);
            }
        } else {
            log.info("The key generator name [[]] is invalid, so return nothing.", _name);
            return null;
        }
    }

    public static Mac getMacByName(String _name) {
        if (_name.length() > 2 && _name.endsWith("\"") && _name.startsWith("\"")) {
            _name = _name.substring(1, _name.length() - 1).trim();

            try {
                Mac tempMac = Mac.getInstance(_name);
                log.info("Load mac[{}] successfully.", _name);
                return tempMac;
            } catch (GeneralSecurityException e) {
                log.error("Failed to initialize Mac for [{}], Exception: {}", _name, e);
                String tempMsg = String.format("Failed to initialize Mac for [%s]", _name);
                throw new RuntimeException(tempMsg);
            }
        } else {
            log.info("The mac name [[]] is invalid, so return nothing.", _name);
            return null;
        }
    }
}
