package com.twinkle.framework.core.context;

import com.twinkle.framework.core.datastruct.AbstractStructAttributeFactoryCenter;
import com.twinkle.framework.core.datastruct.StructAttributeFactoryCenter;
import com.twinkle.framework.core.lang.struct.StructAttributeFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     9/3/19 11:31 AM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
@Slf4j
public class StructAttributeManager {
    private static volatile StructAttributeSchema structAttributeSchema = null;
    private static volatile StructAttributeFactory structAttributeFactory = null;
    private static volatile StructAttributeFactoryCenter structAttributeFactoryCenter = null;
    private static final Object SCHEMA_SYNC = new Object();

    public static StructAttributeSchema getStructAttributeSchema() {
        if (structAttributeSchema == null) {
            synchronized (SCHEMA_SYNC) {
                if (structAttributeSchema == null) {
                    String tempSchemaClassName = "com.twinkle.framework.core.context.DefaultStructAttributeSchema";
                    try {
                        structAttributeSchema = (StructAttributeSchema) StructAttributeManager.class.getClassLoader().loadClass(tempSchemaClassName).newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException("Load Struct Attribute schema failed.", e);
                    }
                    log.info("Loaded Struct Attribute Schema instance - {}", tempSchemaClassName);
                }
            }
        }

        return structAttributeSchema;
    }

    public static StructAttributeFactory getStructAttributeFactory() {
        return structAttributeFactory;
    }

    public static StructAttributeFactoryCenter registerStructAttributeImpl(StructAttributeFactoryCenter _factoryCenter) throws IllegalArgumentException {
        if (_factoryCenter == null) {
            throw new IllegalArgumentException("StructAttribute implementation cannot be null.");
        } else {
            if (_factoryCenter instanceof AbstractStructAttributeFactoryCenter) {
                ((AbstractStructAttributeFactoryCenter) _factoryCenter).setSchema(getStructAttributeSchema());
                log.info("Bound Struct Attribute Schema {} with Struct Attribute Factory Center {}", structAttributeSchema.getClass().getName(), _factoryCenter.getName());
            }

            StructAttributeFactoryCenter tempCenter = structAttributeFactoryCenter;
            structAttributeFactoryCenter = _factoryCenter;
            structAttributeFactory = _factoryCenter.getStructAttributeFactory();
            log.info("Registered StructAttributeFactoryCenter: {}", _factoryCenter.getName());

            return tempCenter;
        }
    }

    private StructAttributeManager() {
    }

    private static void reset() {
        structAttributeSchema = null;
        structAttributeFactory = null;
        structAttributeFactoryCenter = null;
    }
}
