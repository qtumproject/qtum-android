package org.qtum.wallet.ui.fragment.token_fragment.light;

import android.support.design.widget.AppBarLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.qtum.wallet.ui.fragment.token_fragment.TokenFragment;
import org.qtum.wallet.ui.wave_visualizer.WaveHelper;
import org.qtum.wallet.ui.wave_visualizer.WaveView;
import org.qtum.wallet.utils.ContractBuilder;

import butterknife.BindView;


public class TokenFragmentLight extends TokenFragment {

    private final int LAYOUT = org.qtum.wallet.R.layout.lyt_token_fragment_light;

    @BindView(org.qtum.wallet.R.id.wave_view)
    WaveView waveView;
    private WaveHelper mWaveHelper;

//    @BindView(org.qtum.wallet.R.id.app_bar_placeholder) View appbarPlaceholder;
//    @BindView(org.qtum.wallet.R.id.tv_placeholder_balance_value)
//    TextView placeHolderBalance;

//    @BindView(org.qtum.wallet.R.id.tv_placeholder_currency_value)
//    TextView placeHolderCurrency;

    @BindView(org.qtum.wallet.R.id.bt_share)
    ImageButton mShareBtn;

    @BindView(org.qtum.wallet.R.id.iv_choose_address)
    ImageView mIvChooseAddress;

    @BindView(org.qtum.wallet.R.id.tv_token_name)
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
                //appbarPlaceholder.setAlpha(testP2);
                prevPercents = percents;
            }

        });

        //appbarPlaceholder.setVisibility(View.VISIBLE);
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
    public void setBalance(String balance) {
        mTextViewBalance.setText(String.valueOf(balance));
        //placeHolderBalance.setText(String.valueOf(balance));
    }

    @Override
    public void onContractPropertyUpdated(String propName, String propValue) {
        switch (propName){
            case totalSupply:
                totalSupplyValue.setText(ContractBuilder.getShortBigNumberRepresentation(propValue));
                break;
            case decimals:
                decimalsValue.setText(propValue);
                break;
            case symbol:
                //placeHolderCurrency.setText(propValue);
                mTextViewCurrency.setText(" " + propValue);
                break;
            case name:
                mTextViewTokenName.setText(propValue);
                break;
        }
    }

}
