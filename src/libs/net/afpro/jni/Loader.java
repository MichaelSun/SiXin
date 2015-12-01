package net.afpro.jni;

/**
 * User: afpro
 * Date: 11-12-2
 * Time: 下午1:52
 */
public class Loader {
    static {
        System.loadLibrary("rrspeex");
    }

    /**
     * 这样做是有目的的啦
     */
    public static void init() {
    }
}
