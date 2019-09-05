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
    /**
     * Get format names.
     *
     * @return
     */
    String[] getFormatNames();

    /**
     * Is binary or not?
     *
     * @return
     */
    boolean isBinary();

    /**
     * Write the given attr into the output stream.
     *
     * @param _attr
     * @param _outputStream
     * @throws IOException
     */
    void write(T _attr, OutputStream _outputStream) throws IOException;

    /**
     * Read T from the given input stream.
     *
     * @param _inputStream
     * @return
     * @throws IOException
     */
    T read(InputStream _inputStream) throws IOException;

    /**
     * Write multiple attrs into the output steam.
     *
     * @param _attrArray
     * @param _outputStream
     * @throws IOException
     */
    void writeMultiple(T[] _attrArray, OutputStream _outputStream) throws IOException;

    /**
     * Write multiple attrs into the output stream.
     *
     * @param _attrList
     * @param _outputStream
     * @throws IOException
     */
    void writeMultiple(List<T> _attrList, OutputStream _outputStream) throws IOException;

    /**
     * Read multiple attrs from the input stream.
     *
     * @param _inputStream
     * @return
     * @throws IOException
     */
    List<T> readMultiple(InputStream _inputStream) throws IOException;
}
