package com.pixelplex.qtum.ui.fragment.create_wallet_name_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.create_wallet_name_fragment.CreateWalletNameFragment;
import com.pixelplex.qtum.ui.wave_visualizer.WaveHelper;
import com.pixelplex.qtum.ui.wave_visualizer.WaveView;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 24.07.17.
 */

public class CreateWalletNameFragmentLight extends CreateWalletNameFragment {

    @BindView(R.id.wave_view)
    WaveView waveView;
    private WaveHelper mWaveHelper;

    @Override
    protected int getLayout() {
        return R.layout.fragment_create_wallet_name_light;
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
    }

    @Override
    public void onPause() {
        mWaveHelper.cancel();
        super.onPause();
    }
}
