package com.twinkle.framework.core.datastruct.context;

import com.twinkle.framework.core.datastruct.ReflectiveBean;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-06 15:09<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public enum AccessorType {
    BYTE(Byte.TYPE) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setByteAttribute(_attrName, (Byte) _value);
        }
        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getByteAttribute(_attrName);
        }
    },
    SHORT(Short.TYPE) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setShortAttribute(_attrName, (Short) _value);
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getShortAttribute(_attrName);
        }
    },
    INT(Integer.TYPE) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setIntAttribute(_attrName, (Integer) _value);
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getIntAttribute(_attrName);
        }
    },
    LONG(Long.TYPE) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setLongAttribute(_attrName, (Long) _value);
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getLongAttribute(_attrName);
        }
    },
    FLOAT(Float.TYPE) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setFloatAttribute(_attrName, (Float) _value);
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getFloatAttribute(_attrName);
        }
    },
    DOUBLE(Double.TYPE) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setDoubleAttribute(_attrName, (Double) _value);
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getDoubleAttribute(_attrName);
        }
    },
    BOOLEAN(Boolean.TYPE) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setBooleanAttribute(_attrName, (Boolean) _value);
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getDoubleAttribute(_attrName);
        }
    },
    CHAR(Character.TYPE) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setCharAttribute(_attrName, (Character) _value);
        }
        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getCharAttribute(_attrName);
        }
    },
    STRING(String.class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setStringAttribute(_attrName, (String) _value);
        }
        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getStringAttribute(_attrName);
        }
    },
    OBJECT(Object.class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setObjectAttribute(_attrName, _value);
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getObjectAttribute(_attrName);
        }
    },
    BYTE_ARRAY(byte[].class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setByteArrayAttribute(_attrName, (byte[])((byte[]) _value));
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getByteArrayAttribute(_attrName);
        }
    },
    SHORT_ARRAY(short[].class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setShortArrayAttribute(_attrName, (short[])((short[]) _value));
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getShortArrayAttribute(_attrName);
        }
    },
    INT_ARRAY(int[].class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setIntArrayAttribute(_attrName, (int[])((int[]) _value));
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getIntArrayAttribute(_attrName);
        }
    },
    LONG_ARRAY(long[].class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setLongArrayAttribute(_attrName, (long[])((long[]) _value));
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getLongArrayAttribute(_attrName);
        }
    },
    FLOAT_ARRAY(float[].class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setFloatArrayAttribute(_attrName, (float[])((float[]) _value));
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getFloatArrayAttribute(_attrName);
        }
    },
    DOUBLE_ARRAY(double[].class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setDoubleArrayAttribute(_attrName, (double[])((double[]) _value));
        }
        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getDoubleArrayAttribute(_attrName);
        }
    },
    BOOLEAN_ARRAY(boolean[].class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setBooleanArrayAttribute(_attrName, (boolean[])((boolean[]) _value));
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getDoubleArrayAttribute(_attrName);
        }
    },
    CHAR_ARRAY(char[].class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setCharArrayAttribute(_attrName, (char[])((char[]) _value));
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getCharArrayAttribute(_attrName);
        }
    },
    STRING_ARRAY(String[].class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setStringArrayAttribute(_attrName, (String[])((String[]) _value));
        }

        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getStringArrayAttribute(_attrName);
        }
    },
    OBJECT_ARRAY(Object[].class) {
        @Override
        public void set(ReflectiveBean _bean, String _attrName, Object _value) {
            _bean.setObjectArrayAttribute(_attrName, (Object[])((Object[]) _value));
        }
        @Override
        public Object get(ReflectiveBean _bean, String _attrName) {
            return _bean.getObjectArrayAttribute(_attrName);
        }
    };

    private final Class<?> typeClass;
    private final boolean array;
    private final boolean primitive;

    private AccessorType(Class<?> _class) {
        this.typeClass = _class;
        this.array = _class.isArray();
        this.primitive = this.array ? _class.getComponentType().isPrimitive() : _class.isPrimitive();
    }

    public Class<?> getTypeClass() {
        return this.typeClass;
    }

    public boolean isArray() {
        return this.array;
    }

    public boolean isPrimitive() {
        return this.primitive;
    }

    public abstract void set(ReflectiveBean _bean, String _attrName, Object _value);

    public abstract Object get(ReflectiveBean _bean, String _attrName);
}
