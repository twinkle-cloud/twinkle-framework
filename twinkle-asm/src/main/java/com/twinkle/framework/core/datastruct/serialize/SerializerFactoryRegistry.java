package com.twinkle.framework.core.datastruct.serialize;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/2/19 7:05 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
@Data
public class SerializerFactoryRegistry {
    /**
     * Format name -> serializer factory.
     */
    private final Map<String, SerializerFactory> factoryMap = new HashMap<>();
    private String defaultFormatName;

    public SerializerFactoryRegistry() {
    }

    /**
     * Registry the serializer with name.
     * 
     * @param _name
     * @param _factory
     */
    public void register(String _name, SerializerFactory _factory) {
        this.factoryMap.put(_name, _factory);
    }

    /**
     * Registry the serializer with name.
     *
     * @param _name
     * @param _factoryClass
     */
    public void register(String _name, String _factoryClass) {
        try {
            SerializerFactory tempFactory = (SerializerFactory) Class.forName(_factoryClass).newInstance();
            this.register(_name, tempFactory);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            log.warn("SerializerFactory[{}] -> [{}], exception: {}", _factoryClass, _name, e);
        }
    }

    /**
     * Register multi factories.
     *
     * @param _factoryMap
     */
    public void registerMultiple(Map<String, String> _factoryMap) {
        if(MapUtils.isEmpty(_factoryMap)) {
            return;
        }
        _factoryMap.forEach((k, v) -> this.register(k, v));
    }

    /**
     * Get the serializer factory with given format name.
     *
     * @param _formatName
     * @return
     */
    public SerializerFactory getSerializerFactory(String _formatName) {
        return this.factoryMap.get(_formatName);
    }

    /**
     * Get the default serializer factory.
     *
     * @return
     */
    public SerializerFactory getDefaultSerializerFactory() {
        return this.factoryMap.get(this.getDefaultFormatName());
    }
}
