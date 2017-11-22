package org.qtum.wallet.viewholder;

import android.support.wear.widget.WearableRecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.entity.History;

import butterknife.BindView;
import butterknife.ButterKnife;

import org.qtum.wallet.listener.ItemClickListener;
import org.qtum.wallet.utils.DateCalculator;

/**
 * Created by kirillvolkov on 22.11.2017.
 */

public class HistoryViewHolder extends WearableRecyclerView.ViewHolder {

    @BindView(R.id.tv_value)
    TextView mTextViewValue;

    @BindView(R.id.tv_date)
    TextView mTextViewDate;

    @BindView(R.id.tv_id)
    TextView mTextViewID;

    @BindView(R.id.iv_icon)
    ImageView mImageViewIcon;

    public HistoryViewHolder(View itemView, final ItemClickListener itemClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setClickable(true);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(getAdapterPosition() -1);
            }
        });
    }

    public void bind(History history) {
        if (history.getBlockTime() != null) {
            mTextViewDate.setText(DateCalculator.getShortDate(history.getBlockTime() * 1000L));

        } else {
            mTextViewDate.setText(mTextViewDate.getContext().getString(R.string.unconfirmed));
        }
        if (history.getChangeInBalance().doubleValue() > 0) {
            mTextViewID.setText(history.getTxHash());
            mImageViewIcon.setImageResource(R.drawable.ic_receive);
        } else {
            mTextViewID.setText(history.getTxHash());
            mImageViewIcon.setImageResource(R.drawable.ic_sent);
        }
        mTextViewValue.setText(String.format("%s QTUM", history.getChangeInBalance().toString()));
    }
}
