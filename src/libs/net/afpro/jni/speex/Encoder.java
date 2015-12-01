package net.afpro.jni.speex;

import net.afpro.jni.Loader;

/**
 * User: afpro
 * Date: 11-12-1
 * Time: 下午5:04
 */
public class Encoder {
    private long native_ptr = 0;
    private native void native_init(boolean wb);
    private native void native_uninit();

    public Encoder(boolean wb) {
        native_init(wb);
    }

    // return: frame needs not be transmitted (DTX only)
    public native boolean encode(short[] data, Bits output);
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
        return Ctl.getInt(Ctl.CTL_TYPE_ENCODER, native_ptr, Ctl.SPEEX_GET_FRAME_SIZE);
    }

    public void setVBR(boolean on) {
        Ctl.setInt(Ctl.CTL_TYPE_ENCODER, native_ptr, Ctl.SPEEX_SET_VBR, on ? 1 : 0);
    }

    public void setSamplingRate(int rate) {
        Ctl.setInt(Ctl.CTL_TYPE_ENCODER, native_ptr, Ctl.SPEEX_SET_SAMPLING_RATE, rate);
    }
    
    public int getLookAhead() {
        return Ctl.getInt(Ctl.CTL_TYPE_ENCODER, native_ptr, Ctl.SPEEX_GET_LOOK_AHEAD);
    }
    
    public void setDTX(boolean on) {
        Ctl.setInt(Ctl.CTL_TYPE_ENCODER, native_ptr, Ctl.SPEEX_SET_DTX, on ? 1 : 0);
    }

    /**
     * @param quality 0-10, def 8
     */
    public void setVBRQuality(float quality) {
        Ctl.setFloat(Ctl.CTL_TYPE_ENCODER, native_ptr, Ctl.SPEEX_SET_VBR_QUALITY, quality);
    }
    
    public void setVBRMaxBitrate(int rate) {
        Ctl.setFloat(Ctl.CTL_TYPE_ENCODER, native_ptr, Ctl.SPEEX_SET_VBR_MAX_BITRATE, rate);
    }

    /**
     * @param quality 0-10, def 8
     */
    public void setQuality(int quality) {
        Ctl.setInt(Ctl.CTL_TYPE_ENCODER, native_ptr, Ctl.SPEEX_SET_QUALITY, quality);
    }

    /**
     * @param complexity 1-10, def 2
     */
    public void setComplexity(int complexity) {
        Ctl.setInt(Ctl.CTL_TYPE_ENCODER, native_ptr, Ctl.SPEEX_SET_COMPLEXITY, complexity);
    }
    
    public void setVAD(boolean on) {
        Ctl.setInt(Ctl.CTL_TYPE_ENCODER, native_ptr, Ctl.SPEEX_SET_VAD, on ? 1 : 0);
    }
}
