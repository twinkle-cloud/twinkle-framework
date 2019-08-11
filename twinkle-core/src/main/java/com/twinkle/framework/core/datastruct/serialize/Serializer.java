package com.twinkle.framework.core.datastruct.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 11:50<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface Serializer<T> {
    String[] getFormatNames();

    boolean isBinary();

    void write(T var1, OutputStream var2) throws IOException;

    T read(InputStream var1) throws IOException;

    void writeMultiple(T[] var1, OutputStream var2) throws IOException;

    void writeMultiple(List<T> var1, OutputStream var2) throws IOException;

    List<T> readMultiple(InputStream var1) throws IOException;

}
