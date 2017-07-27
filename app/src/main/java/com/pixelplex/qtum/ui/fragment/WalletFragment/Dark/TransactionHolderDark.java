package com.pixelplex.qtum.ui.fragment.WalletFragment.Dark;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.ui.fragment.WalletFragment.TransactionClickListener;
import com.pixelplex.qtum.utils.DateCalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public class TransactionHolderDark extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_value)
    TextView mTextViewValue;
    @BindView(R.id.tv_date)
    TextView mTextViewDate;
    @BindView(R.id.tv_id)
    TextView mTextViewID;
    @BindView(R.id.tv_operation_type)
    TextView mTextViewOperationType;
    @BindView(R.id.iv_icon)
    ImageView mImageViewIcon;
    @BindView(R.id.ll_transaction)
    LinearLayout mLinearLayoutTransaction;

    public TransactionHolderDark(View itemView, final TransactionClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTransactionClick(getAdapterPosition());
            }
        });
        ButterKnife.bind(this, itemView);
    }

    void bindTransactionData(History history) {

        if(history.getBlockTime() != null) {
            mTextViewDate.setText(DateCalculator.getShortDate(history.getBlockTime()*1000L));
        } else {
            mTextViewDate.setText(mTextViewDate.getContext().getString(R.string.not_confirmed));
        }
        if (history.getChangeInBalance().doubleValue() > 0) {
            mTextViewOperationType.setText(R.string.received);
            mTextViewID.setText(history.getTxHash());
            mImageViewIcon.setImageResource(R.drawable.ic_received);
        } else {
            mTextViewOperationType.setText(R.string.sent);
            mTextViewID.setText(history.getTxHash());
            mImageViewIcon.setImageResource(R.drawable.ic_sent);
        }
        mTextViewValue.setText(history.getChangeInBalance().toString() + " QTUM");
    }
}