package com.twinkle.framework.struct.serialize;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 5:20 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class JsonSchemaSerializer extends AbstractJsonSerializer {
    private static final String[] FORMAT_NAMES = new String[]{"JSON", "JSON-Schema"};

    public JsonSchemaSerializer(String _rootType, AbstractSerializer _serializer, AbstractDeserializer _deserializer) {
        super(_rootType, false);
        this.serializer = _serializer;
        this.deserializer = _deserializer;
    }
    @Override
    public String[] getFormatNames() {
        return FORMAT_NAMES;
    }
}
