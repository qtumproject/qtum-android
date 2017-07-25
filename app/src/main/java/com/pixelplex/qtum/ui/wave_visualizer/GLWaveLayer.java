package com.pixelplex.qtum.ui.wave_visualizer;

/**
 * Created by kirillvolkov on 07.07.17.
 */

import java.util.Random;

/**
 * Wave layer implementation.
 */
class GLWaveLayer {
    private final GLWave[] waves;
    private final GLRectangle rectangle;
    private final Random random;

    private float amplitude;

    public GLWaveLayer(GLWaveVisualizationView.Configuration configuration, float[] color, float fromY, float toY, Random random) {
        this.random = random;
        this.waves = new GLWave[configuration.wavesCount];
        float footerToY = fromY + configuration.footerHeight / (configuration.footerHeight + configuration.waveHeight * 2) * (toY - fromY);
        this.rectangle = new GLRectangle(color, -1, 1, fromY, footerToY);
        float waveWidth = 2f / configuration.wavesCount;
        float[] points = randomPoints(this.random, configuration.wavesCount, waveWidth, 0.15f);

        for (int i = 0; i < configuration.wavesCount; i++) {
            byte direction = i % 2 == 0 ? GLWave.DIRECTION_UP : GLWave.DIRECTION_DOWN;
            waves[i] = new GLWave(color, points[i], points[i + 1], footerToY, toY, direction, random);
        }
    }

    /**
     * Generate random points for wave.
     * @param random instance of Random
     * @param wavesCount number of waves
     * @param width width of single wave
     * @param shiftCoef shift coefficient
     * @return generated points for waves
     */
    private static float[] randomPoints(Random random, int wavesCount, float width, float shiftCoef) {
        float shift;
        float[] points = new float[wavesCount + 1];
        for (int i = 0; i < points.length; i++) {
            if (i == 0) {
                points[i] = -1;
            } else if (i == points.length - 1) {
                points[i] = 1;
            } else {
                shift = random.nextFloat() * shiftCoef * width;
                shift *= random.nextBoolean() ? 1 : -1;
                points[i] = -1 + i * width + shift;
            }
        }
        return points;
    }

    /**
     * Update waves and bubbles positions.
     * @param dt time elapsed from last calculations
     * @param dAngle delta angle
     * @param ratioY aspect ratio for Y coordinates
     */
    public void update(long dt, float dAngle, float ratioY) {
        float d = dt * dAngle;
        for (GLWave wave : waves) {
            wave.update(d);
        }
    }

    /**
     * Draw whole wave layer.
     */
    public void draw() {
        for (GLWave wave : waves) {
            wave.draw();
        }
        rectangle.draw();
    }

    /**
     * Update waves data.
     * @param heightCoefficient wave height's coefficient
     * @param amplitude amplitude
     */
    public void updateData(float heightCoefficient, float amplitude) {
        for (GLWave wave : waves) {
            wave.setCoefficient(Utils.randomize(heightCoefficient, random));
        }
        if (amplitude > this.amplitude) {
            this.amplitude = amplitude;
        } else {
            this.amplitude = Utils.smooth(this.amplitude, amplitude, 0.8f);
        }
    }

    public void setColor(float[] color) {
        rectangle.setColor(color);
        for (GLWave wave : waves) {
            wave.setColor(color);
        }
    }
}