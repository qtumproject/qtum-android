package com.pixelplex.qtum.ui.wave_visualizer;

/**
 * Created by kirillvolkov on 07.07.17.
 */

interface InnerVisualization {

    /**
     * Start rendering of data.
     */
    void startRendering();

    /**
     * Stop rendering of data.
     */
    void stopRendering();

    /**
     * Called when data received.
     * @param heightsArray normalized dBm values for every layer
     * @param ampsArray amplitude values for every layer
     */
    void onDataReceived(float[] heightsArray, float[] ampsArray);

}

