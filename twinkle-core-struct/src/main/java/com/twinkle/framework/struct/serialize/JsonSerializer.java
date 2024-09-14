package com.twinkle.framework.struct.serialize;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.twinkle.framework.asm.serialize.TextSerializer;
import com.twinkle.framework.struct.lang.StructAttribute;

import java.io.IOException;
import java.util.List;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 4:15 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public interface JsonSerializer extends TextSerializer<StructAttribute> {
    String TYPE_PROPERTY = "TypeQualifiedName";
    String StructAttribute_PROPERTY = "StructAttribute";

    /**
     * Get Root Type.
     * @return
     */
    String getRootType();

    /**
     * Can be serialized or not?
     * @return
     */
    boolean isSerializeType();

    /**
     * Write attr into the JSON writer directly.
     *
     * @param _attr
     * @param _writer
     * @throws IOException
     */
    void write(StructAttribute _attr, JSONWriter _writer) throws IOException;

    /**
     * Read the object from JSON Reader.
     *
     * @param _reader
     * @return
     * @throws IOException
     */
    StructAttribute read(JSONReader _reader) throws IOException;

    /**
     * Read object list from JSON Reader.
     *
     * @param _reader
     * @return
     * @throws IOException
     */
    List<StructAttribute> readMultiple(JSONReader _reader) throws IOException;
}
