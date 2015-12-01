package net.afpro.gl;

import javax.microedition.khronos.opengles.GL10;

/**
 * at 11:51 AM, 8/31/12
 *
 * @author apfro
 */
public class Renderer {
    public long scene = -1;
    public long time = 0;
    public final float[] direction = {0, 0, 1};
    public final float[] ambient = {0.5f, 0.5f, 0.5f, 1.0f};
    public final float[] diffuse = {0.7f, 0.7f, 0.7f, 1.0f};
    public final float[] specular = {0.7f, 0.7f, 0.7f, 1.0f};

    public Renderer() {
        createInst();
    }

    public void destroy() {
        destroyAllTextures();
        destroyInst();
    }

    protected int getTexture(GL10 gl, String name) {
        return 0;
    }

    protected void destroyAllTextures() {
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destroy();
    }

    // ********* native part *********** //
    private long ptr;
    private native void createInst();
    private native void destroyInst();
    public native void render(GL10 gl);

    static {
        NativeLoader.dummy();
    }
}
