package com.pixelplex.qtum.ui.fragment.WalletFragment.Light;

import android.support.design.widget.AppBarLayout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.ui.fragment.WalletFragment.WalletFragment;
import com.pixelplex.qtum.ui.wave_visualizer.WaveHelper;
import com.pixelplex.qtum.ui.wave_visualizer.WaveView;

import java.util.List;
import butterknife.BindView;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public class WalletFragmentLight extends WalletFragment {

    @BindView(R.id.app_bar_placeholder) View appbarPlaceholder;
    @BindView(R.id.not_confirmed_balance_view) View notConfirmedBalancePlaceholder;
    @BindView(R.id.tv_placeholder_balance_value) TextView placeHolderBalance;
    @BindView(R.id.tv_placeholder_not_confirmed_balance_value) TextView placeHolderBalanceNotConfirmed;
    @BindView(R.id.balance_view) FrameLayout waveContainer;

    @BindView(R.id.wave_view) WaveView waveView;
    private WaveHelper mWaveHelper;

    @Override
    protected int getLayout() {
        return R.layout.fragment_wallet_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        showBottomNavView(R.color.title_color_light);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (!mSwipeRefreshLayout.isActivated()) {
                    if (verticalOffset == 0) {
                        mSwipeRefreshLayout.setEnabled(true);
                    } else {
                        mSwipeRefreshLayout.setEnabled(false);
                    }
                }

                percents = (((getTotalRange() - Math.abs(verticalOffset))*1.0f)/getTotalRange());

                balanceView.setAlpha(percents);
                mButtonQrCode.setAlpha(percents);
                mTextViewWalletName.setAlpha(percents);
                appbarPlaceholder.setAlpha(1-percents);

            }
        });

        appbarPlaceholder.setVisibility(View.VISIBLE);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelper = new WaveHelper(waveView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public void updateHistory(List<History> historyList) {
        super.updateHistory(new TransactionAdapterLight(historyList,getAdapterListener()));
    }

    @Override
    public void updateBalance(String balance, String unconfirmedBalance) {
        try {
        balanceValue.setText(String.format("%s QTUM",balance));
        placeHolderBalance.setText(String.format("%s QTUM",balance));
        if(unconfirmedBalance != null) {
            notConfirmedBalancePlaceholder.setVisibility(View.VISIBLE);
            uncomfirmedBalanceValue.setVisibility(View.VISIBLE);
            uncomfirmedBalanceTitle.setVisibility(View.VISIBLE);
            uncomfirmedBalanceValue.setText(String.format("%s QTUM", unconfirmedBalance));
            placeHolderBalanceNotConfirmed.setText(String.format("%s QTUM", unconfirmedBalance));
        } else {
            notConfirmedBalancePlaceholder.setVisibility(View.GONE);
            uncomfirmedBalanceValue.setVisibility(View.GONE);
            uncomfirmedBalanceTitle.setVisibility(View.GONE);
        }
        } catch (NullPointerException e){
            Log.d("WalletFragmentLight", "updateBalance: " + e.getMessage());
        }
    }
}
