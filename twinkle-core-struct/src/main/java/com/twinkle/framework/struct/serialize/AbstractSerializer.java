package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson2.JSONWriter;
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
//            _writer.writeName(JsonSerializer.TYPE_PROPERTY);//writeKey
            _writer.writeNameValue(JsonSerializer.TYPE_PROPERTY, _attr.getType().getQualifiedName());//writeValue
            _writer.writeName(JsonSerializer.StructAttribute_PROPERTY);
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
