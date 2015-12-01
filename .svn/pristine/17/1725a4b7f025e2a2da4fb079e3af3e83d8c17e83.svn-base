package net.afpro.jni.speex;

import net.afpro.jni.Loader;

/**
 * User: afpro
 * Date: 11-12-1
 * Time: 下午5:04
 */
public abstract class Decoder {
    public final static int RET_STATUS_OK = 0;
    public final static int RET_END_OF_STREAM = -1;
    public final static int RET_CORRUPT_STREAM = -2;
    protected abstract void frame(short[] frame);

    private long native_ptr = 0;
    private native void native_init(boolean wideband);
    private native void native_uninit();

    public Decoder(boolean wideband) {
        native_init(wideband);
    }

    public native int decode(Bits bits, int frameCount);
    public long getNativePtr() {
        return native_ptr;
    }

    @Override
    protected void finalize() throws Throwable {
        native_uninit();
        super.finalize();
    }

    static {
        Loader.init();
    }

    // utils
    public int getFrameSize() {
        return Ctl.getInt(Ctl.CTL_TYPE_DECODER, native_ptr, Ctl.SPEEX_GET_FRAME_SIZE);
    }

    public void setEnh(boolean on) {
        Ctl.setInt(Ctl.CTL_TYPE_DECODER, native_ptr, Ctl.SPEEX_SET_ENH, on ? 1 : 0);
    }

    public void setSamplingRate(int rate) {
        Ctl.setInt(Ctl.CTL_TYPE_DECODER, native_ptr, Ctl.SPEEX_SET_SAMPLING_RATE, rate);
    }
}
