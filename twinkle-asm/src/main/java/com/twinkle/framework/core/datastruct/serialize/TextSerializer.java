package com.twinkle.framework.core.datastruct.serialize;

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
    /**
     * Get Serializer charset.
     *
     * @return
     */
    Charset getCharset();

    /**
     * Update the charset that will be processed.
     *
     * @param _str
     * @throws UnsupportedCharsetException
     */
    void setCharset(String _str) throws UnsupportedCharsetException;

    /**
     * Update the charset that will be processed.
     *
     * @param _charSet
     */
    void setCharset(Charset _charSet);

    /**
     * Write attr.
     *
     * @param _attr
     * @return
     */
    String write(T _attr);

    /**
     * Write attr into the writer.
     *
     * @param _attr
     * @param _writer
     * @throws IOException
     */
    void write(T _attr, Writer _writer) throws IOException;

    /**
     * Read attr from given str.
     *
     * @param _str
     * @return
     */
    T read(String _str);

    /**
     * Read attr from given reader.
     *
     * @param _reader
     * @return
     * @throws IOException
     */
    T read(Reader _reader) throws IOException;

    /**
     * Write multiple attr into the writer.
     *
     * @param _attrArray
     * @param _writer
     * @throws IOException
     */
    void writeMultiple(T[] _attrArray, Writer _writer) throws IOException;

    /**
     * Write multiple attr into the writer.
     *
     * @param _attrList
     * @param _writer
     * @throws IOException
     */
    void writeMultiple(List<T> _attrList, Writer _writer) throws IOException;

    /**
     * Read multiple attrs from reader.
     *
     * @param _reader
     * @return
     * @throws IOException
     */
    List<T> readMultiple(Reader _reader) throws IOException;
}
