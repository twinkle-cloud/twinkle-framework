package com.twinkle.framework.asm.serialize;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-11 14:32<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class TextSerializerBase<T> implements TextSerializer<T> {
    protected Charset charset = Charset.defaultCharset();

    public TextSerializerBase() {
    }

    @Override
    public boolean isBinary() {
        return false;
    }
    @Override
    public Charset getCharset() {
        return this.charset;
    }
    @Override
    public void setCharset(Charset _charSet) {
        this.charset = (Charset) Objects.requireNonNull(_charSet);
    }
    @Override
    public void setCharset(String _str) throws UnsupportedCharsetException {
        this.setCharset(Charset.forName(_str));
    }

    protected Reader reader(InputStream var1) {
        return new InputStreamReader(var1, this.charset);
    }

    protected Writer writer(OutputStream var1) {
        return new OutputStreamWriter(var1, this.charset);
    }
    @Override
    public void write(T _attr, OutputStream _outputStream) throws IOException {
        this.write(_attr, this.writer(_outputStream));
    }
    @Override
    public T read(InputStream _inputStream) throws IOException {
        return this.read(this.reader(_inputStream));
    }
    @Override
    public void writeMultiple(T[] _attrArray, OutputStream _outputStream) throws IOException {
        this.writeMultiple(Arrays.asList(_attrArray), _outputStream);
    }
    @Override
    public void writeMultiple(List<T> _attrList, OutputStream _outputStream) throws IOException {
        this.writeMultiple(_attrList, this.writer(_outputStream));
    }
    @Override
    public void writeMultiple(T[] _attrArray, Writer _writer) throws IOException {
        this.writeMultiple(Arrays.asList(_attrArray), _writer);
    }
    @Override
    public List<T> readMultiple(InputStream _inputStream) throws IOException {
        return this.readMultiple(this.reader(_inputStream));
    }
}
