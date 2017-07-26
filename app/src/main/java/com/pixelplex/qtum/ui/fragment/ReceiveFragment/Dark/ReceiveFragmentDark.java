package com.pixelplex.qtum.ui.fragment.ReceiveFragment.Dark;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class ReceiveFragmentDark extends ReceiveFragment {

    @BindView(R.id.tv_total_balance_number) TextView mTextViewTotalBalanceNumber;

    @Override
    protected int getLayout() {
        return R.layout.fragment_receive;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getPresenter().setQrColors(ContextCompat.getColor(getContext(),R.color.colorPrimary), mCoordinatorLayout.getDrawingCacheBackgroundColor());
    }

    @Override
    public void setBalance(String balance) {
        mTextViewTotalBalanceNumber.setText((balance != null)? String.format("%s QTUM",balance) : "N/A");
    }
}
