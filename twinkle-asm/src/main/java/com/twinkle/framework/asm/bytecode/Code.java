package com.twinkle.framework.asm.bytecode;

import lombok.Getter;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 10:43<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class Code extends SimpleJavaFileObject {
    protected long lastModified = System.currentTimeMillis();
    String className;

    public Code(String _className, Kind _kind) {
        super(getUri(_className, _kind), _kind);
        this.className = _className;
    }

    @Override
    public boolean delete() {
        this.lastModified = 0L;
        return super.delete();
    }

    public String getPackage() {
        return getPackageName(this.className);
    }

    /**
     * Get package name from the full path class name.
     * such as: com.twinkle.framework.core.asm.bytecode
     * will return com.twinkle.framework.core.asm
     *
     * @param _className
     * @return
     */
    public static String getPackageName(String _className) {
        //Get the last .'s index.
        int tempIndex = _className.lastIndexOf(46);
        if (tempIndex < 0) {
            return "";
        } else if (tempIndex == 0) {
            throw new RuntimeException("Bad class name: " + _className);
        } else {
            return _className.substring(0, tempIndex);
        }
    }

    /**
     * Get URI path from the full path class Name.
     * Such as: com.twinkle.framework.core.asm.bytecode.Code
     * Kind: class:
     * will return: com/twinkle/framework/core/asm/bytecode/Code.class
     *
     * @param _className
     * @param _kind
     * @return
     */
    public static URI getUri(String _className, Kind _kind) {
        try {
            return new URI("MEMORY:/" + _className.replace('.', '/') + _kind.extension);
        } catch (URISyntaxException var3) {
            throw new RuntimeException(var3);
        }
    }
}
