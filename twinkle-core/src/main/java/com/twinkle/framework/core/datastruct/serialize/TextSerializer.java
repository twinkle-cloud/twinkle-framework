package com.twinkle.framework.core.datastruct.serialize;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 11:51<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface TextSerializer<T> extends Serializer<T> {
    void setPrettyPrint(boolean var1) throws OperationNotSupportedException;

    boolean isPrettyPrint();

    Charset getCharset();

    void setCharset(String var1) throws UnsupportedCharsetException;

    void setCharset(Charset var1);

    String write(T var1);

    void write(T var1, Writer var2) throws IOException;

    T read(String var1);

    T read(Reader var1) throws IOException;

    void writeMultiple(T[] var1, Writer var2) throws IOException;

    void writeMultiple(List<T> var1, Writer var2) throws IOException;

    List<T> readMultiple(Reader var1) throws IOException;
}
