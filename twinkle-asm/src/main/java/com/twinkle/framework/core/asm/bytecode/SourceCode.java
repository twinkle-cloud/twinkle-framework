package com.twinkle.framework.core.asm.bytecode;

import lombok.Getter;

import java.io.IOException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 10:54<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Getter
public class SourceCode extends Code {
    String sourceCode;

    public SourceCode(String _className, String _sourceCode) {
        super(_className, Kind.SOURCE);
        this.sourceCode = _sourceCode;
    }
    @Override
    public boolean delete() {
        super.delete();
        this.sourceCode = null;
        return true;
    }
    @Override
    public CharSequence getCharContent(boolean _ignoreEncodingErrors) throws IOException {
        return this.sourceCode;
    }
}
