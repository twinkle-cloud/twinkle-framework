package com.twinkle.framework.core.datastruct.serialize;

import java.util.Objects;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/5/19 2:40 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class JsonIntrospectionSerializer extends AbstractJsonSerializer {
    private static final String[] FORMAT_NAMES = new String[]{"JSON", "JSON-Introspection"};

    public JsonIntrospectionSerializer() {
        this(null, true);
    }

    public JsonIntrospectionSerializer(String _rootType) {
        this(Objects.requireNonNull(_rootType), false);
    }

    private JsonIntrospectionSerializer(String _rootType, boolean _serializable) {
        super(_rootType, _serializable);
    }
    @Override
    public String[] getFormatNames() {
        return FORMAT_NAMES;
    }
    @Override
    protected void initSerializers() {
        this.serializer = new IntrospectionSerializer(this.serializableType, this.rootType);
        this.deserializer = new IntrospectionDeserializer(this.serializableType, this.rootType);
    }
}
