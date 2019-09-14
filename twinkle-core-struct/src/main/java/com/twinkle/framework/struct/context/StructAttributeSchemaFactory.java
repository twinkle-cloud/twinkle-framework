package com.twinkle.framework.struct.context;

import com.twinkle.framework.asm.classloader.SerializerClassLoader;
import com.twinkle.framework.asm.designer.ClassDesigner;
import com.twinkle.framework.struct.asm.designer.DeserializerGeneratorClassDesigner;
import com.twinkle.framework.struct.asm.designer.SerializerGeneratorClassDesigner;
import com.twinkle.framework.asm.factory.BeanFactory;
import com.twinkle.framework.asm.descriptor.BeanTypeDescriptor;
import com.twinkle.framework.struct.serialize.AbstractSchemaBasedDeSerializer;
import com.twinkle.framework.struct.serialize.AbstractSchemaBasedSerializer;
import com.twinkle.framework.struct.serialize.JsonSchemaSerializer;
import com.twinkle.framework.struct.serialize.JsonSerializer;
import com.twinkle.framework.struct.type.BeanStructAttributeType;
import com.twinkle.framework.struct.type.StructAttributeType;
import com.twinkle.framework.struct.util.ArrayAllocator;

import java.util.Iterator;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/4/19 4:11 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class StructAttributeSchemaFactory {
    private final SerializerClassLoader classLoader;

    public StructAttributeSchemaFactory(BeanFactory _factory) {
        this.classLoader = new SerializerClassLoader(_factory.getBeanClassLoader());
    }

    public StructAttributeSchemaFactory() {
        this.classLoader = new SerializerClassLoader();
    }

    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    /**
     * Get Json serializer with
     *
     * @param _schema
     * @param _qualifiedName
     * @param _factory
     * @param _allocator
     * @return
     */
    public JsonSerializer generateSerializers(StructAttributeSchema _schema, String _qualifiedName, BeanFactory _factory, ArrayAllocator _allocator) {
        try {
            Iterator<String> tempNameSpaceItr = _schema.getNamespaces();
            Class tempSerializerClass = null;
            Class tempDeserializerClass = null;
            while(tempNameSpaceItr.hasNext()) {
                String tempNameSpace = tempNameSpaceItr.next();
                Iterator<StructAttributeType> tempSATypeItr = _schema.getStructAttributeTypes(tempNameSpace);
                while(tempSATypeItr.hasNext()) {
                    StructAttributeType tempSAType = tempSATypeItr.next();
                    if (tempSAType instanceof BeanStructAttributeType) {
                        BeanTypeDescriptor tempDescriptor = ((BeanStructAttributeType)tempSAType).getTypeDescriptor();
                        if (tempDescriptor != null) {
                            //Build the serializer and deserializer class for all.
                            Class tempItemSerializerClass = this.defineClass(new SerializerGeneratorClassDesigner(tempDescriptor, this.classLoader));
                            Class tempItemDeserializerClass = this.defineClass(new DeserializerGeneratorClassDesigner(tempDescriptor, this.classLoader));
                            if (tempSAType.getQualifiedName().equals(_qualifiedName)) {
                                tempSerializerClass = tempItemSerializerClass;
                                tempDeserializerClass = tempItemDeserializerClass;
                            }
                        }
                    }
                }
            }

            if (tempSerializerClass != null && tempDeserializerClass != null) {
                AbstractSchemaBasedSerializer tempSerializerInstance = (AbstractSchemaBasedSerializer)tempSerializerClass.newInstance();
                AbstractSchemaBasedDeSerializer tempDeserializerInstance = (AbstractSchemaBasedDeSerializer)tempDeserializerClass.newInstance();
                tempDeserializerInstance.setStructAttributeFactory(_factory, _allocator);
                JsonSchemaSerializer tempSerializer = new JsonSchemaSerializer(_qualifiedName, tempSerializerInstance, tempDeserializerInstance);
                return tempSerializer;
            } else {
                throw new RuntimeException("Type: '" + _qualifiedName + "' not found in Struct Attribute Schema");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load the serializer class.
     *
     * @param _designer
     * @param <T>
     * @return
     */
    private <T> Class<T> defineClass(ClassDesigner _designer) {
        String tempName = _designer.getCanonicalClassName();
        if (this.classLoader.isClassDefined(tempName)) {
            try {
                return (Class<T>) this.classLoader.loadClass(tempName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return (Class<T>) this.classLoader.defineClass(_designer);
        }
    }
}
