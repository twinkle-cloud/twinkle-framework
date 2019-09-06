package com.twinkle.framework.asm;

import com.twinkle.framework.asm.codec.BinEncoding;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2019-08-01 16:55<br/>
 *
 * @author chenxj
 * @see
 * @since JDK 1.8
 */
public class Blob {
    private static int maxBytesToPrint = 512;
    public static final BinEncoding DEFAULT_ENCODING = BinEncoding.Hex;;
    /**
     * Blob buffer.
     */
    private ByteBuffer bytes;

    public Blob() {
        this.bytes = ByteBuffer.allocate(0);
    }

    public Blob(byte[] _byteArray) {
        this.bytes = _byteArray != null && _byteArray.length > 0 ? ByteBuffer.wrap(_byteArray) : ByteBuffer.allocate(0);
    }

    public Blob(ByteArrayOutputStream _outputStream) {
        this.bytes = _outputStream != null && _outputStream.size() > 0 ? ByteBuffer.wrap(_outputStream.toByteArray()) : ByteBuffer.allocate(0);
    }

    /**
     * Initialize the Blob with bytebuffer and size.
     *
     * @param _buffer
     * @param _size
     */
    public Blob(ByteBuffer _buffer, int _size) {
        if (_buffer != null && _buffer.remaining() != 0 && _size > 0) {
            ByteBuffer tempBuffer = ByteBuffer.allocate(_size);
            for (int i = _size; i > 0; --i) {
                tempBuffer.put(_buffer.get());
            }

            this.bytes = tempBuffer;
        } else {
            this.bytes = ByteBuffer.allocate(0);
        }
    }

    /**
     * Initialize the Blob with String and BinEncoding.
     *
     * @param _str
     * @param _encoding
     */
    public Blob(String _str, BinEncoding _encoding) {
        if (_str != null && _str.length() > 0) {
            BinEncoding tempEncoding = BinEncoding.which(_str, _encoding);
            if (tempEncoding == null) {
                throw new IllegalArgumentException("Cannot find decoder for: " + _str);
            }

            this.bytes = ByteBuffer.wrap(tempEncoding.codec().decode(tempEncoding.stripPrefixOff(_str)));
        } else {
            this.bytes = ByteBuffer.allocate(0);
        }
    }

    public Blob(String _str) {
        this(_str, DEFAULT_ENCODING);
    }

    /**
     * Reads the byte at this buffer's
     * current position, and then increments the position.
     *
     * @return
     */
    public byte get() {
        return this.bytes.get();
    }

    /**
     * Reads the byte at the given index.
     *
     * @param _index
     * @return
     */
    public byte get(int _index) {
        return this.bytes.get(_index);
    }

    /**
     * This method transfers bytes from this buffer into the given
     * destination array.  If there are fewer bytes remaining in the
     * buffer than are required to satisfy the request, that is, if
     * <tt>length</tt>&nbsp;<tt>&gt;</tt>&nbsp;<tt>remaining()</tt>, then no
     * bytes are transferred and a {@link BufferUnderflowException} is
     * thrown.
     *
     * @param _dst
     * @param _offset
     * @param _length
     * @return
     */
    public Blob get(byte[] _dst, int _offset, int _length) {
        this.bytes.get(_dst, _offset, _length);
        return this;
    }

    /**
     * Relative bulk <i>get</i> method.
     *
     * <p> This method transfers bytes from this buffer into the given
     * destination array.  An invocation of this method of the form
     * <tt>src.get(a)</tt> behaves in exactly the same way as the invocation
     * @param   _dst
     *          The destination array
     */
    public Blob get(byte[] _dst) {
        this.bytes.get(_dst);
        return this;
    }

    public int capacity() {
        return this.bytes.capacity();
    }

    public int position() {
        return this.bytes.position();
    }

    public Blob position(int var1) {
        this.bytes.position(var1);
        return this;
    }

    public Blob mark() {
        this.bytes.mark();
        return this;
    }

    public Blob reset() {
        this.bytes.reset();
        return this;
    }

    public Blob rewind() {
        this.bytes.rewind();
        return this;
    }

    public int remaining() {
        return this.bytes.remaining();
    }

    public boolean hasRemaining() {
        return this.bytes.hasRemaining();
    }

    public ByteBuffer asByteBuffer() {
        return this.bytes.asReadOnlyBuffer();
    }

    public byte[] toByteArray() {
        return (byte[]) this.bytes.array().clone();
    }

    @Override
    public boolean equals(Object _obj) {
        if (this == _obj) {
            return true;
        } else if (_obj != null && this.getClass() == _obj.getClass()) {
            Blob tempBlob = (Blob) _obj;
            if (this.bytes.capacity() != tempBlob.bytes.capacity()) {
                return false;
            } else {
                this.bytes.rewind();
                tempBlob.bytes.rewind();

                while (this.bytes.hasRemaining() && tempBlob.bytes.hasRemaining()) {
                    if (this.bytes.get() != tempBlob.bytes.get()) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int tempCode = 1;
        this.bytes.rewind();

        while (this.bytes.hasRemaining()) {
            tempCode = 31 * tempCode + this.bytes.get();
        }

        return tempCode;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Blob newObj = (Blob) super.clone();
        newObj.bytes = ByteBuffer.wrap((byte[]) this.bytes.array().clone());
        return newObj;
    }

    @Override
    public String toString() {
        StringBuilder tempBuilder = new StringBuilder("[");
        int tempSize = maxBytesToPrint;
        this.bytes.rewind();
        if (this.bytes.remaining() > tempSize) {
            int tempRemaining = this.bytes.remaining() - tempSize;
            byte[] tempPrintByteArray = new byte[tempSize];
            this.bytes.get(tempPrintByteArray);
            tempBuilder.append(DEFAULT_ENCODING.codec().encode(tempPrintByteArray));
            tempBuilder.append('<').append(tempRemaining).append(" bytes skipped").append('>');
        } else {
            tempBuilder.append(DEFAULT_ENCODING.codec().encode(this.bytes.array()));
        }

        tempBuilder.append(']');
        return tempBuilder.toString();
    }

    /**
     * Get encoded String.
     *
     * @param _encoding
     * @param _addPrefixFlag: with the encoding prefix or not.
     * @return
     */
    public String encode(BinEncoding _encoding, boolean _addPrefixFlag) {
        if (_encoding != null) {
            return _addPrefixFlag ? _encoding.prefix() + _encoding.codec().encode(this.bytes.array()) : _encoding.codec().encode(this.bytes.array());
        } else {
            throw new NullPointerException("encoding");
        }
    }

    /**
     * Get encoded String without the encoding prefix by default.
     *
     * @param _encoding
     * @return
     */
    public String encode(BinEncoding _encoding) {
        return this.encode(_encoding, false);
    }

    /**
     * Get the max bytes which is used to print the array in toString() method.
     *
     * @return
     */
    public static int getMaxBytesToPrint() {
        return maxBytesToPrint;
    }

    /**
     * Set the max bytes' size which will used to print the array in toString() method.
     *
     * @param _size
     */
    public static void setMaxBytesToPrint(int _size) {
        maxBytesToPrint = _size;
    }
}
