package net.afpro.jni.speex;

import net.afpro.jni.Loader;

/**
 * User: afpro
 * Date: 11-12-2
 * Time: 下午3:50
 */
public class Bits {
    private long native_ptr = 0;
    private native void native_init();
    private native void native_uninit();

    public Bits() {
        native_init();
    }

    @Override
    protected void finalize() throws Throwable {
        native_uninit();
        super.finalize();
    }

    public native void reset();
    public native void rewind();
    public native int size();
    public native void insertTerminator();

    public native void set(byte[] data, boolean whole);
    public native byte[] get(int size, boolean whole);

    static {
        Loader.init();
    }
}
