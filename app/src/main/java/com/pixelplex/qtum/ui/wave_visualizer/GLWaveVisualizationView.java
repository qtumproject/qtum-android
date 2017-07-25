package com.pixelplex.qtum.ui.wave_visualizer;

/**
 * Created by kirillvolkov on 07.07.17.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.support.annotation.ArrayRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.pixelplex.qtum.R;
import java.util.Random;

/**
 * visualization view implementation for OpenGL.
 */
public class GLWaveVisualizationView extends GLSurfaceView implements InnerVisualization, Runnable {

    private static final int EGL_VERSION = 2;
    private GLRenderer renderer;
    private Configuration configuration;

    float[] heights = {0,0,0};
    float[] amplitude ={0,0,0};

    Handler handler;
    Random random = new Random();

    private GLWaveVisualizationView(@NonNull Builder builder) {
        super(builder.context);
        configuration = new Configuration(builder);
        renderer = new GLRenderer(getContext(), configuration);
        init();
    }

    public GLWaveVisualizationView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        configuration = new Configuration(context, attrs, isInEditMode());
        renderer = new GLRenderer(getContext(), configuration);
        init();
    }

    private void init() {
        handler = new Handler();
        setEGLContextClientVersion(EGL_VERSION);
        setZOrderOnTop(true);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(PixelFormat.RGBA_8888);
        setRenderer(renderer);
    }

    @Override
    public void onResume() {
        super.onResume();
        setVisibility(VISIBLE);
        if(handler != null) {
            onDataReceived(heights, amplitude);
            handler.post(this);
        }
    }

    @Override
    public void onPause() {
        setVisibility(GONE);
        if(handler != null) {
            handler.removeCallbacks(this);
        }
        super.onPause();
    }

    @Override
    public void run() {
        generateRandomFloatArrayHeights();
        generateRandomFloatArrayAmplitudes();
        onDataReceived(heights, amplitude);
        handler.postDelayed(this,500);
    }


    void generateRandomFloatArrayHeights(){
        heights = new float[3];
        for (int i = 0; i < 3; i++){
            heights[i] = generateRandomHeightNumber();
        }
    }

    void generateRandomFloatArrayAmplitudes(){
        amplitude = new float[3];
        for (int i = 0; i < 3; i++){
            amplitude[i] = generateRandomAmplitudeNumber();
        }
    }

    float generateRandomAmplitudeNumber(){
        float minX = 0.0f;
        float maxX = 1.5f;
        return random.nextFloat() * (maxX - minX) + minX;
    }

    float generateRandomHeightNumber(){
        float minX = 0.0f;
        float maxX = 1.5f;
        return random.nextFloat() * (maxX - minX) + minX;
    }

    @Override
    public void startRendering() {
        if (getRenderMode() != RENDERMODE_CONTINUOUSLY) {
            setRenderMode(RENDERMODE_CONTINUOUSLY);
        }
    }

    @Override
    public void stopRendering() {
        if (getRenderMode() != RENDERMODE_WHEN_DIRTY) {
            setRenderMode(RENDERMODE_WHEN_DIRTY);
        }
    }

    @Override
    public void onDataReceived(float[] heightsArray, float[] ampsArray) {
        renderer.onDataReceived(heightsArray, ampsArray);
    }

    /**
     * Configuration holder class.
     */
    static class Configuration {

        int wavesCount;
        int layersCount;
        float waveHeight;
        float footerHeight;
        float[][] layerColors;

        public Configuration(Context context, AttributeSet attrs, boolean isInEditMode) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GLWaveVisualizationView);
            int[] colors;
            try {
                layersCount = array.getInt(R.styleable.GLWaveVisualizationView_av_layersCount, Constants.DEFAULT_LAYERS_COUNT);
                layersCount = Utils.between(layersCount, Constants.MIN_LAYERS_COUNT, Constants.MAX_LAYERS_COUNT);
                wavesCount = array.getInt(R.styleable.GLWaveVisualizationView_av_wavesCount, Constants.DEFAULT_WAVES_COUNT);
                wavesCount = Utils.between(wavesCount, Constants.MIN_WAVES_COUNT, Constants.MAX_WAVES_COUNT);
                waveHeight = array.getDimensionPixelSize(R.styleable.GLWaveVisualizationView_av_wavesHeight, (int) Constants.DEFAULT_WAVE_HEIGHT);
                waveHeight = Utils.between(waveHeight, Constants.MIN_WAVE_HEIGHT, Constants.MAX_WAVE_HEIGHT);
                footerHeight = array.getDimensionPixelSize(R.styleable.GLWaveVisualizationView_av_wavesFooterHeight, (int) Constants.DEFAULT_FOOTER_HEIGHT);
                footerHeight = Utils.between(footerHeight, Constants.MIN_FOOTER_HEIGHT, Constants.MAX_FOOTER_HEIGHT);
                if (isInEditMode) {
                    colors = new int[layersCount];
                } else {
                    int arrayId = array.getResourceId(R.styleable.GLWaveVisualizationView_av_wavesColors, R.array.av_colors);
                   TypedArray colorsArray = array.getResources().obtainTypedArray(arrayId);
                    colors = new int[colorsArray.length()];
                    for (int i = 0; i < colorsArray.length(); i++) {
                        colors[i] = colorsArray.getColor(i, Color.TRANSPARENT);
                    }
                    colorsArray.recycle();
                }
            } finally {
                array.recycle();

            }
            if (colors.length < layersCount) {
                throw new IllegalArgumentException("You specified more layers than colors.");
            }

            layerColors = new float[colors.length][];
            for (int i = 0; i < colors.length; i++) {
                layerColors[i] = Utils.convertColor(colors[i]);
            }
        }

        private Configuration(@NonNull Builder builder) {
            this.waveHeight = builder.waveHeight;
            waveHeight = Utils.between(waveHeight, Constants.MIN_WAVE_HEIGHT, Constants.MAX_WAVE_HEIGHT);
            this.wavesCount = builder.wavesCount;
            wavesCount = Utils.between(wavesCount, Constants.MIN_WAVES_COUNT, Constants.MAX_WAVES_COUNT);
            this.layerColors = builder.layerColors();
            this.footerHeight = builder.footerHeight;
            footerHeight = Utils.between(footerHeight, Constants.MIN_FOOTER_HEIGHT, Constants.MAX_FOOTER_HEIGHT);
            this.layersCount = builder.layersCount;
            layersCount = Utils.between(layersCount, Constants.MIN_LAYERS_COUNT, Constants.MAX_LAYERS_COUNT);
            if (layerColors.length < layersCount) {
                throw new IllegalArgumentException("You specified more layers than colors.");
            }
        }
    }

    public static class ColorsBuilder<T extends ColorsBuilder> {
        private float[][] layerColors;
        private final Context context;

        public ColorsBuilder(@NonNull Context context) {
            this.context = context;
        }

        float[][] layerColors() {
            return layerColors;
        }

        /**
         * Set layer colors from array resource
         *
         * @param arrayId array resource
         */
        public T setLayerColors(@ArrayRes int arrayId) {
            TypedArray colorsArray = context.getResources().obtainTypedArray(arrayId);
            int[] colors = new int[colorsArray.length()];
            for (int i = 0; i < colorsArray.length(); i++) {
                colors[i] = colorsArray.getColor(i, Color.TRANSPARENT);
            }
            colorsArray.recycle();
            return setLayerColors(colors);
        }

        /**
         * Set layer colors.
         *
         * @param colors array of colors
         */
        public T setLayerColors(int[] colors) {
            layerColors = new float[colors.length][];
            for (int i = 0; i < colors.length; i++) {
                layerColors[i] = Utils.convertColor(colors[i]);
            }
            return getThis();
        }

        protected T getThis() {
            return (T) this;
        }
    }

    public static class Builder extends ColorsBuilder<Builder> {

        private Context context;
        private int wavesCount;
        private int layersCount;
        private float waveHeight;
        private float footerHeight;

        public Builder(@NonNull Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        /**
         * Set waves count
         *
         * @param wavesCount waves count
         */
        public Builder setWavesCount(int wavesCount) {
            this.wavesCount = wavesCount;
            return this;
        }

        /**
         * Set layers count
         *
         * @param layersCount layers count
         */
        public Builder setLayersCount(int layersCount) {
            this.layersCount = layersCount;
            return this;
        }

        /**
         * Set wave height in pixels
         *
         * @param waveHeight wave height in pixels
         */
        public Builder setWavesHeight(float waveHeight) {
            this.waveHeight = waveHeight;
            return this;
        }

        /**
         * Set wave height from dimension resource
         *
         * @param waveHeight dimension resource
         */
        public Builder setWavesHeight(@DimenRes int waveHeight) {
            return setWavesHeight((float) context.getResources().getDimensionPixelSize(waveHeight));
        }

        /**
         * Set footer height in pixels
         *
         * @param footerHeight footer height in pixels
         */
        public Builder setWavesFooterHeight(float footerHeight) {
            this.footerHeight = footerHeight;
            return this;
        }

        /**
         * Set footer height from dimension resource
         *
         * @param footerHeight dimension resource
         */
        public Builder setWavesFooterHeight(@DimenRes int footerHeight) {
            return setWavesFooterHeight((float) context.getResources().getDimensionPixelSize(footerHeight));
        }

        public GLWaveVisualizationView build() {
            return new GLWaveVisualizationView(this);
        }
    }

    /**
     * Audio Visualization renderer interface that allows to change waves' colors at runtime.
     */
    public interface VisualizationRenderer extends Renderer {

        /**
         * Update colors configuration.
         *
         * @param builder instance of color builder.
         */
        void updateConfiguration(@NonNull ColorsBuilder builder);
    }
}
