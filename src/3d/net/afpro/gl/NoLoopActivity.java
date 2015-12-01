package net.afpro.gl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.IOException;

/**
 * at 12:04 PM, 9/6/12
 *
 * @author apfro
 */
public class NoLoopActivity extends Activity {
    private GLSurfaceView glSurfaceView = null;
    private Renderer renderer = null;
    private AnimationArgs args = null;

    private final static String ARGS = "args";

    protected Bitmap getBitmap(String name) {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        args = getIntent().getParcelableExtra(ARGS);
        if (args == null) {
            finish();
            return;
        }

        long scene = 0;

        if (args.modelAsset != null && !args.modelAsset.isEmpty()) {
            try {
                scene = Scene.loadScene(getAssets(), args.modelAsset);
            } catch (IOException ignored) {
            }
        }

        if (scene <= 0 && args.modelFile != null && !args.modelFile.isEmpty()) {
            try {
                scene = Scene.loadScene(args.modelFile, args.modelFileGz);
            } catch (IOException ignored) {
            }
        }

        if (scene <= 0) {
            finish();
            return;
        }

        renderer = new TexturedRenderer() {
            @Override
            protected Bitmap getBitmap(String name) {
                return NoLoopActivity.this.getBitmap(name);
            }
        };

        renderer.scene = scene;
        System.arraycopy(args.ambient, 0, renderer.ambient, 0, 4);
        System.arraycopy(args.diffuse, 0, renderer.diffuse, 0, 4);
        System.arraycopy(args.specular, 0, renderer.specular, 0, 4);
        System.arraycopy(args.direction, 0, renderer.direction, 0, 3);

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        glSurfaceView.setRenderer(new GLSurfaceView.Renderer() {
            private long startTime;

            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
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
                renderer.time = System.currentTimeMillis() - startTime;
                if (renderer.time >= args.duration) {
                    NoLoopActivity.this.finish();
                    return;
                }

                gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
                renderer.render(gl);
            }
        });
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        glSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        glSurfaceView.setZOrderOnTop(true);
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        final GLSurfaceView gv = glSurfaceView;
        if (gv != null) {
            gv.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final GLSurfaceView gv = glSurfaceView;
        if (gv != null) {
            gv.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        final Renderer rd = renderer;
        if (rd != null) {
            Scene.releaseScene(renderer.scene);
            rd.destroy();
        }
    }

    public static void show(Context context, Class<? extends NoLoopActivity> clz, AnimationArgs args) {
        final Intent intent = new Intent(context, clz);
        intent.putExtra(ARGS, args);
        context.startActivity(intent);
    }
}
