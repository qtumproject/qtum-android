package org.qtum.wallet.ui.fragment.transaction_fragment.dark;

import android.support.v4.content.ContextCompat;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.transaction_fragment.TransactionFragment;
import org.qtum.wallet.ui.fragment.addresses_detail_fragment.AddressesDetailFragment;
import org.qtum.wallet.utils.FontTextView;

import java.util.List;

import butterknife.BindView;

public class TransactionFragmentDark extends TransactionFragment {

    @BindView(R.id.tv_time_type)
    FontTextView mTextViewTimeType;

    @Override
    protected int getLayout() {
        return R.layout.fragment_transaction;
    }

    @Override
    public void setUpTransactionData(String value, String symbol,String fee,String receivedTime, boolean confirmed, boolean isContractCall) {
        super.setUpTransactionData(value, symbol,fee, receivedTime,confirmed, isContractCall);
        notConfFlag.setVisibility((!confirmed) ? View.VISIBLE : View.INVISIBLE);

        if (Double.valueOf(value) > 0) {
            mTextViewTimeType.setText(R.string.received_time);
        } else {
            mTextViewTimeType.setText(R.string.sent_time);
        }
    }

    @Override
    public void recolorTab(int position) {
        switch (position){
            case 0:
                tabAddresses.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                tabOverview.setTextColor(ContextCompat.getColor(getContext(),R.color.file_size_color));
                tabEventLog.setTextColor(ContextCompat.getColor(getContext(),R.color.file_size_color));
                break;
            case 1:
                tabAddresses.setTextColor(ContextCompat.getColor(getContext(),R.color.file_size_color));
                tabOverview.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                tabEventLog.setTextColor(ContextCompat.getColor(getContext(),R.color.file_size_color));
                break;
            case 2:
                tabAddresses.setTextColor(ContextCompat.getColor(getContext(),R.color.file_size_color));
                tabOverview.setTextColor(ContextCompat.getColor(getContext(),R.color.file_size_color));
                tabEventLog.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                break;
        }
    }

}
