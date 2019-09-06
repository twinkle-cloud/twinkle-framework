package com.twinkle.framework.asm.bytecode;

import java.io.*;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-02 10:51<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ByteCode extends Code {
    ByteArrayOutputStream outputStream;

    public ByteCode(String _className, byte[] _classByteArray) throws IOException {
        super(_className, Kind.CLASS);
        this.outputStream = new ByteArrayOutputStream();
        this.outputStream.write(_classByteArray);
    }

    public ByteCode(String _className) {
        super(_className, Kind.CLASS);
    }
    @Override
    public boolean delete() {
        super.delete();
        if (this.outputStream != null) {
            try {
                this.outputStream.close();
            } catch (IOException e) {
            }
            this.outputStream = null;
        }
        return true;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        return new ByteArrayInputStream(this.outputStream.toByteArray());
    }
    @Override
    public OutputStream openOutputStream() throws IOException {
        this.outputStream = new ByteArrayOutputStream();
        this.lastModified = System.currentTimeMillis();
        return this.outputStream;
    }

    public byte[] getBytes() {
        return this.outputStream.toByteArray();
    }
}
