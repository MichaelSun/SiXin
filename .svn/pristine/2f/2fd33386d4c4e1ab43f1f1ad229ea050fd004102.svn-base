package net.afpro.gl;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;
import java.util.Map;

/**
 * at 11:20 AM, 9/6/12
 *
 * @author apfro
 */
public abstract class TexturedRenderer extends Renderer {
    private final Map<String, Integer> map = new HashMap<String, Integer>();

    protected abstract Bitmap getBitmap(String name);

    @Override
    protected int getTexture(GL10 gl, String name) {
        Integer old = map.get(name);
        if (old != null) {
            return old;
        }

        final Bitmap bmp = getBitmap(name);
        if (bmp == null) {
            return 0;
        }

        int[] buf = new int[1];
        gl.glGenTextures(1, buf, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, buf[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);

        return buf[0];
    }
}
