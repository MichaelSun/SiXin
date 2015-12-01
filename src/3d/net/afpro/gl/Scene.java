package net.afpro.gl;

import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * at 2:52 PM, 8/29/12
 *
 * @author apfro
 */
public class Scene {
    public static long loadScene(final AssetManager assetManager, String path) throws IOException {
        final InputStream inputStream = assetManager.open(path);
        return loadScene(inputStream);
    }

    public static long loadScene(InputStream is) throws IOException {
        if (is == null) {
            return 0;
        }

        final ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        final byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) >= 0) {
            tmp.write(buffer, 0, len);
        }

        return loadScene(tmp.toByteArray());
    }

    public static native long loadScene(byte[] data);

    public static native long loadScene(String path, boolean gz) throws IOException;

    public static native void releaseScene(long scene);

    static {
        NativeLoader.dummy();
    }

    class CL extends ClassLoader {
        protected CL(ClassLoader parentLoader) {
            super(parentLoader);
        }
    }

    void fuck() {
        CL c = new CL(ClassLoader.getSystemClassLoader());
    }
}
