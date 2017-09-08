package com.pixelplex.qtum.ui.fragment.pin_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.pin_fragment.PinFragment;
import com.pixelplex.qtum.ui.wave_visualizer.WaveHelper;
import com.pixelplex.qtum.ui.wave_visualizer.WaveView;

import butterknife.BindView;


public class PinFragmentLight extends PinFragment {

    @BindView(R.id.wave_view)
    WaveView waveView;
    private WaveHelper mWaveHelper;

    boolean isBottomNavigationViewVisible;

    @Override
    protected int getLayout() {
        return R.layout.fragment_pin_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        ((MainActivity)getActivity()).hideBottomNavigationView(R.color.title_color_light);
        isBottomNavigationViewVisible = ((MainActivity)getActivity()).isBottomNavigationViewVisible();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(isBottomNavigationViewVisible){
            ((MainActivity)getActivity()).showBottomNavigationView(R.color.title_color_light);
        }else{
            ((MainActivity)getActivity()).hideBottomNavigationView(R.color.title_color_light);
        }
    }

}
