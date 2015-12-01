package net.afpro.jni.speex;

import net.afpro.jni.Loader;

/**
 * User: afpro
 * Date: 11-12-1
 * Time: 下午5:04
 */
public class Preprocessor {
    private long native_ptr = 0;
    private int agcMaxLoudness = -1;

    private native void native_init(int frameSize, int samplingRate);

    private native void native_uninit();

    public Preprocessor(int frameSize, int samplingRate) {
        native_init(frameSize, samplingRate);
    }

    // return voice activity (true for speech, false for noise/silence), ONLY if VAD turned on
    private native boolean run(short[] data, boolean update_only);
    private void checkAGC() {
        if(agcMaxLoudness <= 0) {
            return;
        }

        setAGC(agcMaxLoudness > getLoudness());
    }

    public boolean run(short[] data) {
        checkAGC();
        return run(data, false);
    }
    /**!*/
    public void update(short[] data) {
        checkAGC();
        run(data, true);
    }

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
    public void setDeNoise(boolean on) {
        Ctl.setInt(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_SET_DEREVERB, on ? 1 : 0);
    }

    /**
     * @param suppress 最大噪音降低, dB, 负值
     */
    public void setNoiseSuppress(int suppress) {
        Ctl.setInt(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_SET_NOISE_SUPPRESS, suppress);
    }

    public void setDeReverb(boolean on) {
        Ctl.setInt(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_SET_DEREVERB, on ? 1 : 0);
    }

    public boolean getAGC() {
        return Ctl.getInt(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_GET_AGC) != 0;
    }

    public void setAGC(boolean on) {
        Ctl.setInt(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_SET_AGC, on ? 1 : 0);
    }

    public void setAGCLevel(float level) {
        Ctl.setFloat(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_SET_AGC_LEVEL, level);
    }

    public void setAGCIncrement(int dBPerSecond) {
        Ctl.setInt(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_SET_AGC_INCREMENT, dBPerSecond);
    }

    public void setAGCDecrement(int dBPerSecond) {
        Ctl.setInt(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_SET_AGC_DECREMENT, dBPerSecond);
    }

    public void setAGCMaxGain(int dB) {
        Ctl.setInt(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_SET_AGC_MAX_GAIN, dB);
    }

    public int getLoudness() {
        return Ctl.getInt(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_GET_AGC_LOUDNESS);
    }

    public int getAGCGain() {
        return Ctl.getInt(Ctl.CTL_TYPE_PREPROCESSOR, native_ptr, Ctl.SPEEX_PREPROCESS_GET_AGC_GAIN);
    }

    public int getAgcOnMaxLoudness() {
        return agcMaxLoudness;
    }

    public void setAgcOnMaxLoudness(int agcMaxLoudness) {
        this.agcMaxLoudness = agcMaxLoudness;
    }
}
