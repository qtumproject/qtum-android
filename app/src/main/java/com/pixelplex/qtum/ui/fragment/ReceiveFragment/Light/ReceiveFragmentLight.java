package com.pixelplex.qtum.ui.fragment.ReceiveFragment.Light;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;
import com.pixelplex.qtum.utils.FontManager;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class ReceiveFragmentLight extends ReceiveFragment {

    @BindView(R.id.not_confirmed_balance_view) View notConfirmedBalancePlaceholder;
    @BindView(R.id.tv_placeholder_balance_value) TextView placeHolderBalance;
    @BindView(R.id.tv_placeholder_not_confirmed_balance_value) TextView placeHolderBalanceNotConfirmed;

    @BindView(R.id.til_amount)
    TextInputLayout mTilAmount;

    @BindView(R.id.et_amount)
    TextInputEditText mEtAmount;

    @Override
    protected int getLayout() {
        return R.layout.fragment_receive_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getPresenter().setQrColors(mCoordinatorLayout.getDrawingCacheBackgroundColor(), ContextCompat.getColor(getContext(),R.color.qr_code_tint_color));
        notConfirmedBalancePlaceholder.setVisibility(View.GONE);
        mTilAmount.setTypeface(FontManager.getInstance().getFont(getResources().getString(R.string.proximaNovaRegular)));
        mEtAmount.setTypeface(FontManager.getInstance().getFont(getResources().getString(R.string.proximaNovaSemibold)));

    }

    @Override
    public void setBalance(String balance) {
        placeHolderBalance.setText((balance != null)? String.format("%s QTUM",balance) : "N/A");
    }
}
