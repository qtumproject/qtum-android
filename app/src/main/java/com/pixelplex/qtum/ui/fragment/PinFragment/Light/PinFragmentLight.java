package com.pixelplex.qtum.ui.fragment.PinFragment.Light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinFragment;
import com.pixelplex.qtum.ui.wave_visualizer.WaveHelper;
import com.pixelplex.qtum.ui.wave_visualizer.WaveView;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class PinFragmentLight extends PinFragment {

    @BindView(R.id.wave_view)
    WaveView waveView;
    private WaveHelper mWaveHelper;

    @Override
    protected int getLayout() {
        return R.layout.fragment_pin_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelper = new WaveHelper(waveView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWaveHelper.start();
        ((MainActivity)getActivity()).hideBottomNavigationView(R.color.title_color_light);
    }

    @Override
    public void onPause() {
        mWaveHelper.cancel();
        super.onPause();
    }

}
