package com.pixelplex.qtum.ui.wave_visualizer;

/**
 * Created by kirillvolkov on 07.07.17.
 */

import android.content.Context;
import android.opengl.GLES20;
import android.support.annotation.NonNull;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * OpenGL renderer implementation.
 */
class GLRenderer implements GLWaveVisualizationView.VisualizationRenderer {

    private static final long ANIMATION_TIME = 10000;
    private static final float D_ANGLE = (float) (2 * Math.PI / ANIMATION_TIME);

    private final GLWaveVisualizationView.Configuration configuration;
    private GLWaveLayer[] layers;
    private long startTime;
    private final float height;
    private final Random random;
    private float ratioY = 1;

    public GLRenderer(@NonNull Context context, GLWaveVisualizationView.Configuration configuration) {
        this.configuration = configuration;
        this.random = new Random();
        startTime = System.currentTimeMillis();
        height = context.getResources().getDisplayMetrics().heightPixels/3;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA_SATURATE, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_ALPHA_BITS);
        layers = new GLWaveLayer[configuration.layersCount];
        float layerHeightPerc = (configuration.waveHeight) / height;
        for (int i = 0; i < layers.length; i++) {
            float fromY = -1;
            float toY = fromY + layerHeightPerc;
            layers[i] = new GLWaveLayer(configuration, configuration.layerColors[i], fromY, toY, random);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        ratioY = (float) width / height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        long endTime = System.currentTimeMillis();
        long dt = endTime - startTime;
        startTime = endTime;
        int i = 0;

        for (GLWaveLayer layer : layers) {
            // slow down speed of wave from top to bottom of screen
            float speedCoef = (1 - 1f * i / (layers.length) * 0.8f);
            layer.update(dt, D_ANGLE * speedCoef, ratioY);
            i++;
        }
        for (GLWaveLayer layer : layers) {
            layer.draw();
        }
    }

    public final void onDataReceived(float[] heightsArray, float[] ampsArray) {
        if (layers == null)
            return;
        for (int i = 0; i < layers.length; i++) {
            if (layers[i] == null)
                return;
            layers[i].updateData(heightsArray[i], ampsArray[i]);
        }
    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * @param type       - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    @Override
    public void updateConfiguration(@NonNull GLWaveVisualizationView.ColorsBuilder builder) {
        if (layers == null)
            return;
        float[][] colors = builder.layerColors();
        for (int i = 0; i < layers.length; i++) {
            layers[i].setColor(colors[i]);
        }
    }
}