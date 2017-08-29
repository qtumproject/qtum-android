package com.pixelplex.qtum.ui.fragment.start_page_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.start_page_fragment.StartPageFragment;
import com.pixelplex.qtum.ui.wave_visualizer.WaveHelper;
import com.pixelplex.qtum.ui.wave_visualizer.WaveView;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class StartPageFragmentLight extends StartPageFragment {

    @BindView(R.id.wave_view) WaveView waveView;
    private WaveHelper mWaveHelper;

    @Override
    protected int getLayout() {
        return R.layout.fragment_start_page_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        hideBottomNavView(R.color.title_color_light);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelper = new WaveHelper(waveView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

    @Override
    public void onPause() {
        mWaveHelper.cancel();
        super.onPause();
    }
}
