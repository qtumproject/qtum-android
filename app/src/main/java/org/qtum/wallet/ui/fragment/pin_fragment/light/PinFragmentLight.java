package org.qtum.wallet.ui.fragment.pin_fragment.light;

import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;
import org.qtum.wallet.ui.wave_visualizer.WaveHelper;
import org.qtum.wallet.ui.wave_visualizer.WaveView;

import butterknife.BindView;


public class PinFragmentLight extends PinFragment {

    @BindView(org.qtum.wallet.R.id.wave_view)
    WaveView waveView;
    private WaveHelper mWaveHelper;

    boolean isBottomNavigationViewVisible;

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_pin_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        ((MainActivity)getActivity()).hideBottomNavigationView(org.qtum.wallet.R.color.title_color_light);
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
            ((MainActivity)getActivity()).showBottomNavigationView(org.qtum.wallet.R.color.title_color_light);
        }else{
            ((MainActivity)getActivity()).hideBottomNavigationView(org.qtum.wallet.R.color.title_color_light);
        }
    }

}
