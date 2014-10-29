package ch.bfh.ti.jts.utils.deepcopy;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * ByteArrayOutputStream implementation that doesn't synchronize methods and
 * doesn't copy the data on toByteArray().
 *
 * @author Philip Isenhour {@link http
 *         ://javatechniques.com/blog/faster-deep-copies-of-java-objects/}
 * @author ente
 */
public class FastByteArrayOutputStream extends OutputStream {
    
    /**
     * Buffer and size
     */
    protected byte[] buf  = null;
    protected int    size = 0;
    
    /**
     * Constructs a stream with buffer capacity size 5K
     */
    public FastByteArrayOutputStream() {
        this(5 * 1024);
    }
    
    /**
     * Constructs a stream with the given initial size
     */
    public FastByteArrayOutputStream(final int initSize) {
        size = 0;
        buf = new byte[initSize];
    }
    
    /**
     * Returns the byte array containing the written data. Note that this array
     * will almost always be larger than the amount of data actually written.
     */
    public byte[] getByteArray() {
        return buf;
    }
    
    /**
     * Returns a ByteArrayInputStream for reading back the written data
     */
    public InputStream getInputStream() {
        return new FastByteArrayInputStream(buf, size);
    }
    
    public int getSize() {
        return size;
    }
    
    public void reset() {
        size = 0;
    }
    
    /**
     * Ensures that we have a large enough buffer for the given size.
     */
    private void verifyBufferSize(final int sz) {
        if (sz > buf.length) {
            byte[] old = buf;
            buf = new byte[Math.max(sz, 2 * buf.length)];
            System.arraycopy(old, 0, buf, 0, old.length);
            old = null;
        }
    }
    
    @Override
    public final void write(final byte b[]) {
        verifyBufferSize(size + b.length);
        System.arraycopy(b, 0, buf, size, b.length);
        size += b.length;
    }
    
    @Override
    public final void write(final byte b[], final int off, final int len) {
        verifyBufferSize(size + len);
        System.arraycopy(b, off, buf, size, len);
        size += len;
    }
    
    @Override
    public final void write(final int b) {
        verifyBufferSize(size + 1);
        buf[size++] = (byte) b;
    }
}
