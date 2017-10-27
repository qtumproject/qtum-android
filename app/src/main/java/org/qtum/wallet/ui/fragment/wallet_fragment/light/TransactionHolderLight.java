package org.qtum.wallet.ui.fragment.wallet_fragment.light;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.ui.fragment.wallet_fragment.TransactionClickListener;
import org.qtum.wallet.utils.ClipboardUtils;
import org.qtum.wallet.utils.DateCalculator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public class TransactionHolderLight extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_value)
    TextView mTextViewValue;
    @BindView(R.id.tv_date)
    TextView mTextViewDate;
    @BindView(R.id.tv_id)
    TextView mTextViewID;
    @BindView(R.id.iv_icon)
    ImageView mImageViewIcon;
    @BindView(R.id.ll_transaction)
    LinearLayout mLinearLayoutTransaction;

    @OnLongClick(R.id.tv_id)
    public boolean onIdLongClick() {
        ClipboardUtils.copyToClipBoard(mTextViewID.getContext(), mTextViewID.getText().toString(), new ClipboardUtils.CopyCallback() {
            @Override
            public void onCopyToClipBoard() {
                Toast.makeText(mTextViewID.getContext(),mTextViewID.getContext().getString(R.string.copied), Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    TransactionHolderLight(View itemView, final TransactionClickListener listener) {
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

        mLinearLayoutTransaction.setBackgroundResource(android.R.color.transparent);

        if (history.getChangeInBalance().doubleValue() > 0) {
            mImageViewIcon.setImageResource(R.drawable.ic_received_light);
        } else {
            mImageViewIcon.setImageResource(R.drawable.ic_sended_light);
        }

        if(history.getBlockTime() != null) {
            mTextViewDate.setText(DateCalculator.getShortDate(history.getBlockTime()*1000L));
        } else {
            mImageViewIcon.setImageResource(R.drawable.ic_confirmation_loader);
            mTextViewDate.setText(R.string.confirmation);
            mLinearLayoutTransaction.setBackgroundResource(R.color.bottom_nav_bar_color_light);
        }

        mTextViewID.setText(history.getTxHash());
        mTextViewValue.setText(getSpannedBalance(history.getChangeInBalance().toString() + " QTUM"));
    }

    private SpannableString getSpannedBalance(String balance){

        SpannableString span =  new SpannableString(balance);
        if(balance.length() > 4) {
            span.setSpan(new RelativeSizeSpan(.6f), balance.length() - 4, balance.length(), 0);
        }
        return span;
    }
}
