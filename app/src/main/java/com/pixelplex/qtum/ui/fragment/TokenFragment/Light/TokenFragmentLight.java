package com.pixelplex.qtum.ui.fragment.TokenFragment.Light;

import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.TokenFragment.TokenFragment;
import com.pixelplex.qtum.ui.wave_visualizer.WaveHelper;
import com.pixelplex.qtum.ui.wave_visualizer.WaveView;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 26.07.17.
 */

public class TokenFragmentLight extends TokenFragment {

    private final int LAYOUT = R.layout.lyt_token_fragment_light;

    @BindView(R.id.wave_view)
    WaveView waveView;
    private WaveHelper mWaveHelper;

    @BindView(R.id.app_bar_placeholder) View appbarPlaceholder;
    @BindView(R.id.tv_placeholder_balance_value)
    TextView placeHolderBalance;

    @BindView(R.id.tv_placeholder_currency_value)
    TextView placeHolderCurrency;


    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelper = new WaveHelper(waveView);

        mSwipeRefreshLayout.setEnabled(false);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                percents = (((getTotalRange() - Math.abs(verticalOffset))*1.0f)/getTotalRange());
                balanceView.setAlpha((percents>0.5f)? percents : 1 - percents);
                collapseLinearLayout.collapseFromPercents(percents);
                appbarPlaceholder.setAlpha(1-percents);
                prevPercents = percents;
            }

        });

        appbarPlaceholder.setVisibility(View.VISIBLE);
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

    public void setBalance(float balance) {
        mTextViewBalance.setText(String.valueOf(balance));
        placeHolderBalance.setText(String.valueOf(balance));
    }

    @Override
    public void onContractPropertyUpdated(String propName, String propValue) {
        switch (propName){
            case totalSupply:
                totalSupplyValue.setText(propValue);
                break;
            case decimals:
                decimalsValue.setText(propValue);
                break;
            case symbol:
                placeHolderCurrency.setText(propValue);
                mTextViewCurrency.setText(" " + propValue);
                break;
        }
    }

}
