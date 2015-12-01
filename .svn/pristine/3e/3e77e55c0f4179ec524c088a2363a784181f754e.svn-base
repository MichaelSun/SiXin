package net.afpro.gl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.IOException;

/**
 * at 下午1:42, 12-9-10
 *
 * @author afpro
 */
public class AutoView extends GLSurfaceView {
    public final AnimationArgs args = new AnimationArgs();
    private net.afpro.gl.Renderer renderer = null;
    private long startTime = 0;

    public AutoView(Context context) {
        super(context);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setRenderer(new Renderer() {
            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
                startTime = System.currentTimeMillis();
                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadMatrixf(args.modelview, 0);
                gl.glClearColor(0, 0, 0, 0);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                gl.glViewport(0, 0, width, height);

                final float[] mat = new float[16];
                Matrix.perspectiveM(mat, 0, args.projectionFov, width / (float) height, args.projectionZNear, args.projectionZFar);

                gl.glMatrixMode(GL10.GL_PROJECTION);
                gl.glLoadMatrixf(mat, 0);
                gl.glMatrixMode(GL10.GL_MODELVIEW);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                if(renderer.scene <= 0) {
                    onAnimationFinished();
                    return;
                }

                renderer.time = System.currentTimeMillis() - startTime;
                if (renderer.time >= args.duration) {
                    onAnimationFinished();
                    return;
                }

                gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
                renderer.render(gl);
            }
        });
        setRenderMode(RENDERMODE_CONTINUOUSLY);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        renderer = new TexturedRenderer() {
            @Override
            protected Bitmap getBitmap(String name) {
                return AutoView.this.getBitmap(name);
            }
        };

        if (args.modelAsset != null && !args.modelAsset.isEmpty()) {
            try {
                renderer.scene = Scene.loadScene(getContext().getAssets(), args.modelAsset);
            } catch (IOException ignored) {
            }
        }

        if (renderer.scene <= 0 && args.modelFile != null && !args.modelFile.isEmpty()) {
            try {
                renderer.scene = Scene.loadScene(args.modelFile, args.modelFileGz);
            } catch (IOException ignored) {
            }
        }

        if (renderer.scene <= 0) {
            return;
        }

        System.arraycopy(args.ambient, 0, renderer.ambient, 0, 4);
        System.arraycopy(args.diffuse, 0, renderer.diffuse, 0, 4);
        System.arraycopy(args.specular, 0, renderer.specular, 0, 4);
        System.arraycopy(args.direction, 0, renderer.direction, 0, 3);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Scene.releaseScene(renderer.scene);
        renderer.destroy();
        renderer = null;
    }

    protected Bitmap getBitmap(String name) {
        return null;
    }

    protected void onAnimationFinished() {
    }
}
