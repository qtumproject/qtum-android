package org.qtum.wallet.ui.fragment.wallet_fragment.light;

import android.graphics.Color;
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
import rx.Subscriber;
import rx.Subscription;

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
    @BindView(R.id.tv_getting_info)
    TextView mTextViewGettingInfo;
    Subscription mSubscription;
    History mHistory;
    @BindView(R.id.view_contract_indicator)
    View contractIndicator;
    @BindView(R.id.progress_indicator)
    View progressIndicator;

    TransactionHolderLight(View itemView, final TransactionClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (mHistory.isReceiptUpdated()) {
                    listener.onTransactionClick(mHistory.getTxHash());
                //}
            }
        });
        ButterKnife.bind(this, itemView);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardUtils.copyToClipBoard(mTextViewID.getContext(), mTextViewID.getText().toString(), new ClipboardUtils.CopyCallback() {
                    @Override
                    public void onCopyToClipBoard() {
                        Toast.makeText(mTextViewID.getContext(), mTextViewID.getContext().getString(R.string.copied), Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });
    }

    void bindTransactionData(final History history) {
        mHistory = history;

        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mLinearLayoutTransaction.setBackgroundResource(android.R.color.transparent);
            if (history.getBlockTime() != null) {
                if (history.isReceiptUpdated()) {
                    mImageViewIcon.setVisibility(View.VISIBLE);
                    mTextViewDate.setVisibility(View.VISIBLE);
                    progressIndicator.setVisibility(View.GONE);
                    mTextViewGettingInfo.setVisibility(View.GONE);
                }else {
                    contractIndicator.setBackgroundColor(Color.TRANSPARENT);
                    mImageViewIcon.setVisibility(View.GONE);
                    mTextViewDate.setVisibility(View.GONE);
                    progressIndicator.setVisibility(View.VISIBLE);
                    mTextViewGettingInfo.setVisibility(View.VISIBLE);
                }
                mSubscription = DateCalculator.getUpdater().subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Object o) {
                        mTextViewDate.setText(DateCalculator.getShortDate(history.getBlockTime() * 1000L));
                    }
                });
                if (history.isContractType()) {
                    mImageViewIcon.setImageResource(R.drawable.ic_sent_cont_light);
                    contractIndicator.setBackgroundResource(R.color.contract_transaction_indicator_sent_color);

                } else {
                    contractIndicator.setBackgroundColor(Color.TRANSPARENT);
                    switch (history.getHistoryType()) {
                        case Internal_Transaction:
                            mImageViewIcon.setImageResource(R.drawable.ic_sent_to_myself_light);
                            break;
                        case Sent:
                            mImageViewIcon.setImageResource(R.drawable.ic_sended_light);
                            break;
                        case Received:
                            mImageViewIcon.setImageResource(R.drawable.ic_received_light);
                            break;
                    }

                }
                mTextViewDate.setText(DateCalculator.getShortDate(history.getBlockTime() * 1000L));
            } else {
                progressIndicator.setVisibility(View.GONE);
                mTextViewGettingInfo.setVisibility(View.GONE);
                mImageViewIcon.setImageResource(R.drawable.ic_confirmation_loader);
                mTextViewDate.setText(R.string.confirmation);
                mLinearLayoutTransaction.setBackgroundResource(R.color.bottom_nav_bar_color_light);
            }

        mTextViewID.setText(history.getTxHash());
        mTextViewValue.setText(String.format("%s QTUM", history.getChangeInBalance()));
    }
}
