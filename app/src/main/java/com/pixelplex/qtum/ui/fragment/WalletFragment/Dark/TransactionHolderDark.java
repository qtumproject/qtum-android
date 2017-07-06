package com.pixelplex.qtum.ui.fragment.WalletFragment.Dark;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.ui.fragment.WalletFragment.TransactionClickListener;
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

    Date date = new Date();
    long currentTime = date.getTime() / 1000L;

    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM");

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
            long transactionTime = history.getBlockTime();
            long delay = currentTime - transactionTime;
            String dateString;
            if(delay < 60){
                dateString = delay + " sec ago";
            }else
            if (delay < 3600) {
                dateString = delay / 60 + " min ago";
            } else {
                Calendar calendarNow = Calendar.getInstance();
                calendarNow.set(Calendar.HOUR_OF_DAY, 0);
                calendarNow.set(Calendar.MINUTE, 0);
                calendarNow.set(Calendar.SECOND, 0);
                date = calendarNow.getTime();
                Date dateTransaction = new Date(transactionTime * 1000L);
                dateString = ((transactionTime - date.getTime() / 1000L) > 0)? timeFormat.format(dateTransaction) : dateFormat.format(dateTransaction);
            }
            mTextViewDate.setText(dateString);
        } else {
            mTextViewDate.setText("Not confirmed");
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