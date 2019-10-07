package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson.JSONWriter;
import com.twinkle.framework.struct.lang.StructAttribute;

import java.io.IOException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 5:28 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractSerializer {
    protected final boolean isSerializable;
    protected final String rootType;

    public AbstractSerializer(boolean _serializable, String _rootType) {
        this.isSerializable = _serializable;
        this.rootType = _rootType;
    }

    public void serialize(StructAttribute _attr, JSONWriter _writer) throws IOException {
        this.writeHeader(_attr, _writer);
        this.writeStructAttribute(_attr, _writer);
        this.writeTrailer(_writer);
    }

    protected void writeHeader(StructAttribute _attr, JSONWriter _writer) throws IOException {
        if (this.isSerializable) {
            _writer.startObject();
            _writer.writeKey(JsonSerializer.TYPE_PROPERTY);
            _writer.writeValue(_attr.getType().getQualifiedName());
            _writer.writeKey(JsonSerializer.StructAttribute_PROPERTY);
        }
    }

    /**
     * Output struct attribute to JSON writer.
     *
     * @param _attr
     * @param _writer
     * @throws IOException
     */
    protected abstract void writeStructAttribute(StructAttribute _attr, JSONWriter _writer) throws IOException;

    protected void writeTrailer(JSONWriter _writer) throws IOException {
        if (this.isSerializable) {
            _writer.endObject();
        }
    }
}
