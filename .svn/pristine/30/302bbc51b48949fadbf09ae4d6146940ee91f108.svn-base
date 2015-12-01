package net.afpro.jni.speex;

import net.afpro.jni.Loader;

/**
 * User: afpro
 * Date: 11-12-2
 * Time: 下午2:46
 */
public class Ctl {
    static {
        Loader.init();
    }
    
    // int
    public final static int SPEEX_GET_LOOK_AHEAD = 39;
    // int, on(1) or off(0)
    public final static int SPEEX_SET_ENH = 0;
    public final static int SPEEX_GET_ENH = 1;
    // int
    public final static int SPEEX_GET_FRAME_SIZE = 3;
    // int, 0-10, def 8
    public final static int SPEEX_SET_QUALITY = 4;
    // int
    public final static int SPEEX_SET_MODE = 6;
    public final static int SPEEX_GET_MODE = 7;
    // int, on(1) or off(0)
    public final static int SPEEX_SET_VBR = 12;
    public final static int SPEEX_GET_VBR = 13;
    // float, 0-10, def 8
    public final static int SPEEX_SET_VBR_QUALITY = 14;
    public final static int SPEEX_GET_VBR_QUALITY = 15;
    // int, 1-10, def 2, 控制资源使用
    public final static int SPEEX_SET_COMPLEXITY = 16;
    public final static int SPEEX_GET_COMPLEXITY = 17;
    // int
    public final static int SPEEX_SET_BITRATE = 18;
    public final static int SPEEX_GET_BITRATE = 19;
    // int, in Hz
    public final static int SPEEX_SET_SAMPLING_RATE = 24;
    public final static int SPEEX_GET_SAMPLING_RATE = 25;
    // void
    public final static int SPEEX_RESET_STATE = 26;
    // int, on(1) or off(0), voice activity detection
    public final static int SPEEX_SET_VAD = 30;
    public final static int SPEEX_GET_VAD = 31;
    // int, average bit-rate
    public final static int SPEEX_SET_ABR = 32;
    public final static int SPEEX_GET_ABR = 33;
    // int, on(1) or off(0), discontinuous transmissio
    public final static int SPEEX_SET_DTX = 34;
    public final static int SPEEX_GET_DTX = 35;
    // int, Tell the encoder to optimize encoding for a certain percentage of packet loss (in percent)
    public final static int SPEEX_SET_PLC_TUNING = 40;
    public final static int SPEEX_GET_PLC_TUNING = 41;
    // int
    public final static int SPEEX_SET_VBR_MAX_BITRATE = 42;
    public final static int SPEEX_GET_VBR_MAX_BITRATE = 43;
    // int, on(1) or off(0), high-pass filter
    public final static int SPEEX_SET_HIGHPASS = 44;
    public final static int SPEEX_GET_HIGHPASS = 45;
    // int, on(1) or off(0)
    public final static int SPEEX_PREPROCESS_SET_DENOISE = 0;
    public final static int SPEEX_PREPROCESS_GET_DENOISE = 1;
    // int, on(1) or off(0)
    public final static int SPEEX_PREPROCESS_SET_AGC = 2;
    public final static int SPEEX_PREPROCESS_GET_AGC = 3;
    // int, on(1) or off(0), voice activity detector, deprecate
    public final static int SPEEX_PREPROCESS_SET_VAD = 4;
    public final static int SPEEX_PREPROCESS_GET_VAD = 5;
    // float, 1-32768, def 8000
    public final static int SPEEX_PREPROCESS_SET_AGC_LEVEL = 6;
    public final static int SPEEX_PREPROCESS_GET_AGC_LEVEL = 7;
    // int, on(1) or off(0), reverberation removal
    public final static int SPEEX_PREPROCESS_SET_DEREVERB = 8;
    public final static int SPEEX_PREPROCESS_GET_DEREVERB = 9;
    //int, 最大噪音降低, dB, 负值
    public final static int SPEEX_PREPROCESS_SET_NOISE_SUPPRESS = 18;
    public final static int SPEEX_PREPROCESS_GET_NOISE_SUPPRESS = 19;
    public final static int SPEEX_PREPROCESS_SET_AGC_INCREMENT = 26;
    public final static int SPEEX_PREPROCESS_GET_AGC_INCREMENT = 27;
    public final static int SPEEX_PREPROCESS_SET_AGC_DECREMENT = 28;
    public final static int SPEEX_PREPROCESS_GET_AGC_DECREMENT = 29;
    public final static int SPEEX_PREPROCESS_SET_AGC_MAX_GAIN = 30;
    public final static int SPEEX_PREPROCESS_GET_AGC_MAX_GAIN = 31;
    public final static int SPEEX_PREPROCESS_GET_AGC_LOUDNESS = 33;
    public final static int SPEEX_PREPROCESS_GET_AGC_GAIN = 35;

    // 控制对象的类型
    public final static int CTL_TYPE_UNKNOWN = 0;
    public final static int CTL_TYPE_ENCODER = 1;
    public final static int CTL_TYPE_DECODER = 2;
    public final static int CTL_TYPE_PREPROCESSOR = 3;

    // native 方法
    public native static int getInt(int type, long native_ptr, int arg);

    public native static float getFloat(int type, long native_ptr, int arg);

    public native static void setInt(int type, long native_ptr, int arg, int value);

    public native static void setFloat(int type, long native_ptr, int arg, float value);

    public native static void ctl(int type, long native_ptr, int arg);

    // utils
    private static long[] reflect(Object obj) {
        if (obj != null) {
            if (obj instanceof Encoder) {
                return new long[]{
                        CTL_TYPE_ENCODER,
                        ((Encoder) obj).getNativePtr(),
                };
            }

            if (obj instanceof Decoder) {
                return new long[]{
                        CTL_TYPE_DECODER,
                        ((Decoder) obj).getNativePtr(),
                };
            }

            if (obj instanceof Preprocessor) {
                return new long[]{
                        CTL_TYPE_PREPROCESSOR,
                        ((Preprocessor) obj).getNativePtr(),
                };
            }
        }

        return new long[]{
                CTL_TYPE_UNKNOWN,
                0,
        };
    }

    public static int getInt(Object obj, int arg) {
        long[] ref = reflect(obj);

        if (ref[0] == CTL_TYPE_UNKNOWN || ref[1] == 0) {
            return 0;
        }

        return getInt((int) ref[0], ref[1], arg);
    }

    public static float getFloat(Object obj, int arg) {
        long[] ref = reflect(obj);

        if (ref[0] == CTL_TYPE_UNKNOWN || ref[1] == 0) {
            return 0;
        }

        return getFloat((int) ref[0], ref[1], arg);
    }

    public static void setInt(Object obj, int arg, int value) {
        long[] ref = reflect(obj);

        if (ref[0] == CTL_TYPE_UNKNOWN || ref[1] == 0) {
            return;
        }

        setInt((int) ref[0], ref[1], arg, value);
    }

    public static void setFloat(Object obj, int arg, float value) {
        long[] ref = reflect(obj);

        if (ref[0] == CTL_TYPE_UNKNOWN || ref[1] == 0) {
            return;
        }

        setFloat((int) ref[0], ref[1], arg, value);
    }

    public static void ctl(Object obj, int arg) {
        long[] ref = reflect(obj);

        if (ref[0] == CTL_TYPE_UNKNOWN || ref[1] == 0) {
            return;
        }

        ctl((int) ref[0], ref[1], arg);
    }
}
