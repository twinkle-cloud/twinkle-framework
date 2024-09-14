package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.twinkle.framework.asm.serialize.TextSerializerBase;
import com.twinkle.framework.struct.type.AttributeType;
import com.twinkle.framework.struct.lang.StructAttribute;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 4:16 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public abstract class AbstractJsonSerializer extends TextSerializerBase<StructAttribute> implements JsonSerializer {
    protected StringWriter writer;
    protected boolean serializableType;
    protected String rootType;
    protected AbstractSerializer serializer;
    protected AbstractDeserializer deserializer;
    protected boolean prettyPrint;

    protected AbstractJsonSerializer(String _rootType, boolean _serializable) {
        this.rootType = _rootType;
        this.serializableType = _serializable;
        this.writer = new StringWriter();
        this.prettyPrint = false;
        this.initSerializers();
    }

    protected void initSerializers() {
    }

    protected JSONWriter getJSONWriter(Writer _writer) {
//        JSONWriter tempJsonWriter = new JSONWriter(_writer);

        JSONWriter tempJsonWriter = JSONWriter.of();
        return tempJsonWriter;
    }

    protected JSONReader getJSONReader(Reader _reader) {
        return JSONReader.of(_reader);
    }
    @Override
    public boolean isSerializeType() {
        return this.serializableType;
    }
    @Override
    public String getRootType() {
        return this.rootType;
    }
    @Override
    public String write(StructAttribute _attr) {
        try {
            this.writer.getBuffer().setLength(0);
            this.write(_attr, this.writer);
            return this.writer.toString();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException", e);
        }
    }
    @Override
    public StructAttribute read(String _str) {
        try {
            return this.read((new StringReader(_str)));
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException", e);
        }
    }
    @Override
    public void write(StructAttribute _attr, Writer _writer) throws IOException {
        JSONWriter tempJSONWriter = this.getJSONWriter(_writer);
        this.write(_attr, tempJSONWriter);
        tempJSONWriter.flushTo(_writer);//FastJson2 构造函数不再支持将Writer内置进去2024/08/22
        tempJSONWriter.close();
    }

    @Override
    public void write(StructAttribute _attr, JSONWriter _writer) throws IOException {
        this.serializer.serialize(_attr, _writer);
    }
    @Override
    public void writeMultiple(List<StructAttribute> _attrList, Writer _writer) throws IOException {
        JSONWriter tempJSONWriter = this.getJSONWriter(_writer);
        tempJSONWriter.startArray();
        for(StructAttribute tempItem : _attrList) {
            this.write(tempItem, tempJSONWriter);
        }
        tempJSONWriter.endArray();
        tempJSONWriter.flushTo(_writer);//FastJson2 构造函数不再支持将Writer内置进去2024/08/22
        tempJSONWriter.close();
    }
    @Override
    public StructAttribute read(Reader _reader) throws IOException {
        JSONReader tempReader = this.getJSONReader(_reader);
        StructAttribute tempAttr = this.read(tempReader);
        tempReader.close();
        return tempAttr;
    }
    @Override
    public StructAttribute read(JSONReader _reader) throws IOException {
        return this.deserializer.deserialize(_reader);
    }

    @Override
    public List<StructAttribute> readMultiple(Reader _reader) throws IOException {
        JSONReader tempJSONReader = this.getJSONReader(_reader);
        return readMultiple(tempJSONReader);
    }

    @Override
    public List<StructAttribute> readMultiple(JSONReader _reader) throws IOException {
        List<StructAttribute> tempList = new ArrayList<>();
        _reader.startArray();
        tempList.addAll(_reader.readArray());
//        while(tempJSONReader.readA) {
//            tempList.add(this.read(tempJSONReader));
//        }

        _reader.endArray();
        _reader.close();
        return tempList;
    }

    protected static void unexpectedType(AttributeType _type) {
        throw new RuntimeException("Unexpected type: " + _type);
    }
}
