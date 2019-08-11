package com.twinkle.framework.core.datastruct.serialize;

import javax.naming.OperationNotSupportedException;
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
    public void setPrettyPrint(boolean var1) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("PrettyPrint not supported");
    }
    @Override
    public boolean isPrettyPrint() {
        return false;
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
    public void setCharset(Charset var1) {
        this.charset = (Charset) Objects.requireNonNull(var1);
    }
    @Override
    public void setCharset(String var1) throws UnsupportedCharsetException {
        this.setCharset(Charset.forName(var1));
    }

    protected Reader reader(InputStream var1) {
        return new InputStreamReader(var1, this.charset);
    }

    protected Writer writer(OutputStream var1) {
        return new OutputStreamWriter(var1, this.charset);
    }
    @Override
    public void write(T var1, OutputStream var2) throws IOException {
        this.write(var1, this.writer(var2));
    }
    @Override
    public T read(InputStream var1) throws IOException {
        return this.read(this.reader(var1));
    }
    @Override
    public void writeMultiple(T[] var1, OutputStream var2) throws IOException {
        this.writeMultiple(Arrays.asList(var1), var2);
    }
    @Override
    public void writeMultiple(List<T> var1, OutputStream var2) throws IOException {
        this.writeMultiple(var1, this.writer(var2));
    }
    @Override
    public void writeMultiple(T[] var1, Writer var2) throws IOException {
        this.writeMultiple(Arrays.asList(var1), var2);
    }
    @Override
    public List<T> readMultiple(InputStream var1) throws IOException {
        return this.readMultiple(this.reader(var1));
    }
}
