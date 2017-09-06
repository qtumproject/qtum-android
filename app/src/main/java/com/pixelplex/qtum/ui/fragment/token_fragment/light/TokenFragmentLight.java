package com.pixelplex.qtum.ui.fragment.token_fragment.light;

import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.token_fragment.TokenFragment;
import com.pixelplex.qtum.ui.wave_visualizer.WaveHelper;
import com.pixelplex.qtum.ui.wave_visualizer.WaveView;

import butterknife.BindView;


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

    @BindView(R.id.bt_share)
    ImageButton mShareBtn;

    @BindView(R.id.iv_choose_address)
    ImageView mIvChooseAddress;

    @BindView(R.id.tv_token_name)
    TextView mTokenTitle;

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelper = new WaveHelper(waveView);

        //mSwipeRefreshLayout.setEnabled(false);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                percents = (((getTotalRange() - Math.abs(verticalOffset))*1.0f)/getTotalRange());

                float testPercents = percents - (1 - percents);
                float testP2 = (percents >= .8f)? 0 : (1 - percents) - percents;

                mShareBtn.setAlpha(testPercents);
                mIvChooseAddress.setAlpha(testPercents);
                mTokenTitle.setAlpha(testPercents);
                balanceView.setAlpha(testPercents);
                collapseLinearLayout.collapseFromPercents(percents);
                appbarPlaceholder.setAlpha(testP2);
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
            case name:
                mTextViewTokenName.setText(propValue);
                break;
        }
    }

}
