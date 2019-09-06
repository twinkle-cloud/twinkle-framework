package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson.JSONReader;
import com.twinkle.framework.struct.type.StructAttribute;

import java.io.IOException;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 5:30 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractDeserializer {
    protected final boolean isSerializable;
    protected final String rootType;

    public AbstractDeserializer(boolean _serializable, String _rootType) {
        this.isSerializable = _serializable;
        this.rootType = _rootType;
    }

    public StructAttribute deserialize(JSONReader _reader) throws IOException {
        String tempHeader = this.readHeader(_reader);
        StructAttribute tempAttr = this.readStructAttribute(_reader, tempHeader);
        this.readTrailer(_reader);
        return tempAttr;
    }

    protected String readHeader(JSONReader _reader) throws IOException {
        if (this.isSerializable) {
            _reader.startObject();
            this.readProperty(_reader, JsonSerializer.TYPE_PROPERTY);
            String tempHeader = _reader.readString();
            this.readProperty(_reader, JsonSerializer.StructAttribute_PROPERTY);
//                var1.endObject();
            return tempHeader;
        } else {
            return this.rootType;
        }
    }

    protected void readProperty(JSONReader _reader, String _property) throws IOException {
        String tempProperty = _reader.readString();
        if (!_property.equals(tempProperty)) {
            throw new IOException("Unexpected property, expected " + _property + ", got: " + tempProperty);
        }
    }

    protected abstract StructAttribute readStructAttribute(JSONReader _reader, String _property) throws IOException;

    protected void readTrailer(JSONReader _reader) throws IOException {
        if (this.isSerializable) {
            _reader.endObject();
        }
    }
}
