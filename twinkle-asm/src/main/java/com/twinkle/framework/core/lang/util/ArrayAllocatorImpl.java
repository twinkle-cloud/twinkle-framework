package com.twinkle.framework.core.lang.util;

import com.twinkle.framework.core.lang.struct.ArrayType;
import com.twinkle.framework.core.lang.struct.PrimitiveType;
import com.twinkle.framework.core.lang.struct.StructAttribute;
import com.twinkle.framework.core.lang.struct.StructType;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     8/30/19 7:52 PM<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class ArrayAllocatorImpl implements ArrayAllocator {
    private static final String STRUCT_ATTRIBUTE_CLASS_NAME = StructAttribute[].class.getName();
    private static BooleanArrayFactory booleanArrayFactory = new BooleanArrayFactory();
    private static CharArrayFactory charArrayFactory = new CharArrayFactory();
    private static ByteArrayFactory byteArrayFactoryrayFactory = new ByteArrayFactory();
    private static ShortArrayFactory shortArrayFactory = new ShortArrayFactory();
    private static IntegerArrayFactory integerArrayFactory = new IntegerArrayFactory();
    private static LongArrayFactory longArrayFactory = new LongArrayFactory();
    private static FloatArrayFactory floatArrayFactory = new FloatArrayFactory();
    private static DoubleArrayFactory doubleArrayFactory = new DoubleArrayFactory();
    private static StringArrayFactory stringArrayFactory = new StringArrayFactory();
    private static StructAttributeArrayFactory structAttributeArrayFactory = new StructAttributeArrayFactory();

    public ArrayAllocatorImpl() {
    }

    @Override
    public ArrayWrapperFactory<?> getArrayWrapperFactory(String _className) {
        if (STRUCT_ATTRIBUTE_CLASS_NAME.equals(_className)) {
            return structAttributeArrayFactory;
        } else if ("[Ljava.lang.String;".equals(_className)) {
            return stringArrayFactory;
        } else if (_className == null) {
            throw new NullPointerException("The given array class name is null.");
        } else {
            if (_className.length() == 2 && _className.charAt(0) == '[') {
                switch (_className.charAt(1)) {
                    case 'B':
                        return byteArrayFactoryrayFactory;
                    case 'C':
                        return charArrayFactory;
                    case 'D':
                        return doubleArrayFactory;
                    case 'E':
                    case 'G':
                    case 'H':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    default:
                        break;
                    case 'F':
                        return floatArrayFactory;
                    case 'I':
                        return integerArrayFactory;
                    case 'J':
                        return longArrayFactory;
                    case 'S':
                        return shortArrayFactory;
                    case 'Z':
                        return booleanArrayFactory;
                }
            }

            throw new IllegalArgumentException("Class " + _className + " represents unsupported array type.");
        }
    }

    @Override
    public ArrayWrapperFactory<?> getArrayWrapperFactory(Class<?> _class) {
        return this.getArrayWrapperFactory(_class.getName());
    }

    @Override
    public ArrayWrapperFactory<boolean[]> getBooleanArrayWrapperFactory() {
        return booleanArrayFactory;
    }

    @Override
    public ArrayWrapperFactory<char[]> getCharArrayWrapperFactory() {
        return charArrayFactory;
    }

    @Override
    public ArrayWrapperFactory<byte[]> getByteArrayWrapperFactory() {
        return byteArrayFactoryrayFactory;
    }

    @Override
    public ArrayWrapperFactory<short[]> getShortArrayWrapperFactory() {
        return shortArrayFactory;
    }

    @Override
    public ArrayWrapperFactory<int[]> getIntegerArrayWrapperFactory() {
        return integerArrayFactory;
    }

    @Override
    public ArrayWrapperFactory<long[]> getLongArrayWrapperFactory() {
        return longArrayFactory;
    }

    @Override
    public ArrayWrapperFactory<float[]> getFloatArrayWrapperFactory() {
        return floatArrayFactory;
    }

    @Override
    public ArrayWrapperFactory<double[]> getDoubleArrayWrapperFactory() {
        return doubleArrayFactory;
    }

    @Override
    public ArrayWrapperFactory<String[]> getStringArrayWrapperFactory() {
        return stringArrayFactory;
    }

    @Override
    public <T> ArrayWrapperFactory<T[]> getObjectArrayWrapperFactory(Class<T[]> _class) {
        return (ArrayWrapperFactory<T[]>) this.getArrayWrapperFactory(_class);
    }
    @Override
    public MutableArray newArray(ArrayType _arrayType, int _size) {
        StructType tempStructType = _arrayType.getElementType();
        if (tempStructType.isStructType()) {
            return this.newStructAttributeArray(_size);
        } else if (tempStructType.isStringType()) {
            return this.newStringArray(_size);
        }
        if (tempStructType.isPrimitiveType()) {
            switch (tempStructType.getID()) {
                case PrimitiveType.BYTE_ID:
                    return this.newByteArray(_size);
                case PrimitiveType.SHORT_ID:
                    return this.newShortArray(_size);
                case PrimitiveType.INT_ID:
                    return this.newIntegerArray(_size);
                case PrimitiveType.LONG_ID:
                    return this.newLongArray(_size);
                case PrimitiveType.CHAR_ID:
                    return this.newCharArray(_size);
                case PrimitiveType.BOOLEAN_ID:
                    return this.newBooleanArray(_size);
                case 7:
                case 8:
                default:
                    break;
                case PrimitiveType.FLOAT_ID:
                    return this.newFloatArray(_size);
                case PrimitiveType.DOUBLE_ID:
                    return this.newDoubleArray(_size);
            }
        }
        throw new IllegalArgumentException("Unsupported array type: " + _arrayType);
    }

    @Override
    public MutableBooleanArray newBooleanArray(int _size) {
        return booleanArrayFactory.newArrayWrapper(_size);
    }

    @Override
    public MutableByteArray newByteArray(int _size) {
        return byteArrayFactoryrayFactory.newArrayWrapper(_size);
    }

    @Override
    public MutableCharArray newCharArray(int _size) {
        return charArrayFactory.newArrayWrapper(_size);
    }

    @Override
    public MutableDoubleArray newDoubleArray(int _size) {
        return doubleArrayFactory.newArrayWrapper(_size);
    }

    @Override
    public MutableFloatArray newFloatArray(int _size) {
        return floatArrayFactory.newArrayWrapper(_size);
    }

    @Override
    public MutableIntegerArray newIntegerArray(int _size) {
        return integerArrayFactory.newArrayWrapper(_size);
    }

    @Override
    public MutableLongArray newLongArray(int _size) {
        return longArrayFactory.newArrayWrapper(_size);
    }

    @Override
    public MutableShortArray newShortArray(int _size) {
        return shortArrayFactory.newArrayWrapper(_size);
    }
    @Override
    public MutableStringArray newStringArray(int _size) {
        return stringArrayFactory.newArrayWrapper(_size);
    }

    @Override
    public MutableStructAttributeArray newStructAttributeArray(int _size) {
        return structAttributeArrayFactory.newArrayWrapper(_size);
    }
    @Override
    public MutableBooleanArray wrap(boolean[] _array, int _size) {
        return booleanArrayFactory.wrap(_array, _size);
    }
    @Override
    public MutableBooleanArray wrap(boolean[] _array) {
        return booleanArrayFactory.wrap(_array);
    }
    @Override
    public MutableByteArray wrap(byte[] _array, int _size) {
        return byteArrayFactoryrayFactory.wrap(_array, _size);
    }
    @Override
    public MutableByteArray wrap(byte[] _array) {
        return byteArrayFactoryrayFactory.wrap(_array);
    }
    @Override
    public MutableCharArray wrap(char[] _array, int _size) {
        return charArrayFactory.wrap(_array, _size);
    }
    @Override
    public MutableCharArray wrap(char[] _array) {
        return charArrayFactory.wrap(_array);
    }
    @Override
    public MutableDoubleArray wrap(double[] _array, int _size) {
        return doubleArrayFactory.wrap(_array, _size);
    }
    @Override
    public MutableDoubleArray wrap(double[] _array) {
        return doubleArrayFactory.wrap(_array);
    }
    @Override
    public MutableFloatArray wrap(float[] _array, int _size) {
        return floatArrayFactory.wrap(_array, _size);
    }
    @Override
    public MutableFloatArray wrap(float[] _array) {
        return floatArrayFactory.wrap(_array);
    }
    @Override
    public MutableIntegerArray wrap(int[] _array, int _size) {
        return integerArrayFactory.wrap(_array, _size);
    }
    @Override
    public MutableIntegerArray wrap(int[] _array) {
        return integerArrayFactory.wrap(_array);
    }
    @Override
    public MutableLongArray wrap(long[] _array, int _size) {
        return longArrayFactory.wrap(_array, _size);
    }
    @Override
    public MutableLongArray wrap(long[] _array) {
        return longArrayFactory.wrap(_array);
    }
    @Override
    public MutableShortArray wrap(short[] _array, int _size) {
        return shortArrayFactory.wrap(_array, _size);
    }
    @Override
    public MutableShortArray wrap(short[] _array) {
        return shortArrayFactory.wrap(_array);
    }
    @Override
    public MutableStringArray wrap(String[] _array, int _size) {
        return stringArrayFactory.wrap(_array, _size);
    }
    @Override
    public MutableStringArray wrap(String[] _array) {
        return stringArrayFactory.wrap(_array);
    }
    @Override
    public MutableStructAttributeArray wrap(StructAttribute[] _array, int _size) {
        return structAttributeArrayFactory.wrap(_array, _size);
    }
    @Override
    public MutableStructAttributeArray wrap(StructAttribute[] _array) {
        return structAttributeArrayFactory.wrap(_array);
    }

    private static class StructAttributeArrayFactory implements ArrayWrapperFactory<StructAttribute[]> {
        private StructAttributeArrayFactory() {
        }
        @Override
        public MutableStructAttributeArray newArrayWrapper(int _size) {
            return new StructAttributeArrayImpl(_size);
        }
        @Override
        public StructAttribute[] newArray(int _size) {
            return new StructAttribute[_size];
        }
        @Override
        public Class<StructAttribute[]> getArrayClass() {
            return StructAttribute[].class;
        }
        @Override
        public MutableStructAttributeArray wrap(StructAttribute[] _array, int _size) {
            return new StructAttributeArrayImpl(_array, _size);
        }

        public MutableStructAttributeArray wrap(StructAttribute[] _array) {
            return new StructAttributeArrayImpl(_array, _array != null ? _array.length : 0);
        }
    }

    private static class StringArrayFactory implements ArrayWrapperFactory<String[]> {
        private StringArrayFactory() {
        }
        @Override
        public MutableStringArray newArrayWrapper(int _size) {
            return new StringArrayImpl(_size);
        }
        @Override
        public String[] newArray(int _size) {
            return new String[_size];
        }
        @Override
        public Class<String[]> getArrayClass() {
            return String[].class;
        }
        @Override
        public MutableStringArray wrap(String[] _array, int _size) {
            return new StringArrayImpl(_array, _size);
        }

        public MutableStringArray wrap(String[] _array) {
            return new StringArrayImpl(_array, _array != null ? _array.length : 0);
        }
    }

    private static class DoubleArrayFactory implements ArrayWrapperFactory<double[]> {
        private DoubleArrayFactory() {
        }
        @Override
        public MutableDoubleArray newArrayWrapper(int _size) {
            return new DoubleArrayImpl(_size);
        }
        @Override
        public double[] newArray(int _size) {
            return new double[_size];
        }
        @Override
        public Class<double[]> getArrayClass() {
            return double[].class;
        }
        @Override
        public MutableDoubleArray wrap(double[] _array, int _size) {
            return new DoubleArrayImpl(_array, _size);
        }

        public MutableDoubleArray wrap(double[] _array) {
            return new DoubleArrayImpl(_array, _array != null ? _array.length : 0);
        }
    }

    private static class FloatArrayFactory implements ArrayWrapperFactory<float[]> {
        private FloatArrayFactory() {
        }
        @Override
        public MutableFloatArray newArrayWrapper(int _size) {
            return new FloatArrayImpl(_size);
        }
        @Override
        public float[] newArray(int _size) {
            return new float[_size];
        }
        @Override
        public Class<float[]> getArrayClass() {
            return float[].class;
        }
        @Override
        public MutableFloatArray wrap(float[] _array, int _size) {
            return new FloatArrayImpl(_array, _size);
        }

        public MutableFloatArray wrap(float[] _array) {
            return new FloatArrayImpl(_array, _array != null ? _array.length : 0);
        }
    }

    private static class LongArrayFactory implements ArrayWrapperFactory<long[]> {
        private LongArrayFactory() {
        }
        @Override
        public MutableLongArray newArrayWrapper(int _size) {
            return new LongArrayImpl(_size);
        }
        @Override
        public long[] newArray(int _size) {
            return new long[_size];
        }
        @Override
        public Class<long[]> getArrayClass() {
            return long[].class;
        }
        @Override
        public MutableLongArray wrap(long[] _array, int _size) {
            return new LongArrayImpl(_array, _size);
        }

        public MutableLongArray wrap(long[] _array) {
            return new LongArrayImpl(_array, _array != null ? _array.length : 0);
        }
    }

    private static class IntegerArrayFactory implements ArrayWrapperFactory<int[]> {
        private IntegerArrayFactory() {
        }
        @Override
        public MutableIntegerArray newArrayWrapper(int _size) {
            return new IntegerArrayImpl(_size);
        }
        @Override
        public int[] newArray(int _size) {
            return new int[_size];
        }
        @Override
        public Class<int[]> getArrayClass() {
            return int[].class;
        }
        @Override
        public MutableIntegerArray wrap(int[] _array, int _size) {
            return new IntegerArrayImpl(_array, _size);
        }

        public MutableIntegerArray wrap(int[] _array) {
            return new IntegerArrayImpl(_array, _array != null ? _array.length : 0);
        }
    }

    private static class ShortArrayFactory implements ArrayWrapperFactory<short[]> {
        private ShortArrayFactory() {
        }
        @Override
        public MutableShortArray newArrayWrapper(int _size) {
            return new ShortArrayImpl(_size);
        }
        @Override
        public short[] newArray(int _size) {
            return new short[_size];
        }
        @Override
        public Class<short[]> getArrayClass() {
            return short[].class;
        }
        @Override
        public MutableShortArray wrap(short[] _array, int _size) {
            return new ShortArrayImpl(_array, _size);
        }

        public MutableShortArray wrap(short[] _array) {
            return new ShortArrayImpl(_array, _array != null ? _array.length : 0);
        }
    }

    private static class ByteArrayFactory implements ArrayWrapperFactory<byte[]> {
        private ByteArrayFactory() {
        }
        @Override
        public MutableByteArray newArrayWrapper(int _size) {
            return new ByteArrayImpl(_size);
        }
        @Override
        public byte[] newArray(int _size) {
            return new byte[_size];
        }
        @Override
        public Class<byte[]> getArrayClass() {
            return byte[].class;
        }
        @Override
        public MutableByteArray wrap(byte[] _array, int _size) {
            return new ByteArrayImpl(_array, _size);
        }

        public MutableByteArray wrap(byte[] _array) {
            return new ByteArrayImpl(_array, _array != null ? _array.length : 0);
        }
    }

    private static class CharArrayFactory implements ArrayWrapperFactory<char[]> {
        private CharArrayFactory() {
        }
        @Override
        public MutableCharArray newArrayWrapper(int _size) {
            return new CharArrayImpl(_size);
        }
        @Override
        public char[] newArray(int _size) {
            return new char[_size];
        }
        @Override
        public Class<char[]> getArrayClass() {
            return char[].class;
        }
        @Override
        public MutableCharArray wrap(char[] _array, int _size) {
            return new CharArrayImpl(_array, _size);
        }

        public MutableCharArray wrap(char[] _array) {
            return new CharArrayImpl(_array, _array != null ? _array.length : 0);
        }
    }

    private static class BooleanArrayFactory implements ArrayWrapperFactory<boolean[]> {
        private BooleanArrayFactory() {
        }
        @Override
        public MutableBooleanArray newArrayWrapper(int _size) {
            return new BooleanArrayImpl(_size);
        }
        @Override
        public boolean[] newArray(int _size) {
            return new boolean[_size];
        }
        @Override
        public Class<boolean[]> getArrayClass() {
            return boolean[].class;
        }
        @Override
        public MutableBooleanArray wrap(boolean[] _array, int _size) {
            return new BooleanArrayImpl(_array, _size);
        }

        public MutableBooleanArray wrap(boolean[] _array) {
            return new BooleanArrayImpl(_array, _array != null ? _array.length : 0);
        }
    }
}
