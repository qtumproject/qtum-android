package com.pixelplex.qtum.ui.fragment.transaction_fragment.transaction_detail_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.history.TransactionInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 11.07.17.
 */

public class TransactionDetailHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.tv_single_string)
    TextView mTextViewAddress;
    @BindView(R.id.tv_value)
    TextView mTextViewValue;

    public TransactionDetailHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    void bindTransactionDetail(TransactionInfo transactionInfo){
        mTextViewAddress.setText(transactionInfo.getAddress());
        mTextViewValue.setText(String.format("%s QTUM",transactionInfo.getValue().toString()));
    }
}