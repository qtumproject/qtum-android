package org.qtum.wallet.ui.fragment.wallet_fragment.dark;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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

import static org.qtum.wallet.utils.StringUtils.convertBalanceToString;

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

    @BindView(R.id.view_contract_indicator)
    View contractIndicator;

    @BindView(R.id.progress_indicator)
    View progressIndicator;

    Subscription mSubscription;
    History mHistory;

    public TransactionHolderDark(View itemView, final TransactionClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHistory.isReceiptUpdated()) {
                    listener.onTransactionClick(getAdapterPosition());
                }
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

        if (history.getBlockTime() != null) {
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
            mTextViewDate.setText(DateCalculator.getShortDate(history.getBlockTime() * 1000L));

        } else {
            mTextViewDate.setText(mTextViewDate.getContext().getString(R.string.unconfirmed));
        }
        progressIndicator.setVisibility(View.GONE);
        if (history.isReceiptUpdated()) {
            if (history.getTransactionReceipt() != null) {
                mImageViewIcon.setVisibility(View.VISIBLE);
                if (history.getChangeInBalance().doubleValue() > 0) {
                    mTextViewOperationType.setText(R.string.received_contract);
                    mImageViewIcon.setImageResource(R.drawable.ic_rec_cont_dark);
                    contractIndicator.setBackgroundResource(R.color.colorPrimary);
                } else {
                    mTextViewOperationType.setText(R.string.sent_contract);
                    mImageViewIcon.setImageResource(R.drawable.ic_sent_cont_dark);
                    contractIndicator.setBackgroundResource(R.color.colorAccent);
                }
            } else {
                contractIndicator.setBackgroundColor(Color.TRANSPARENT);
                if (history.getChangeInBalance().doubleValue() > 0) {
                    mTextViewOperationType.setText(R.string.received);
                    mImageViewIcon.setImageResource(R.drawable.ic_received);
                } else {
                    mTextViewOperationType.setText(R.string.sent);
                    mImageViewIcon.setImageResource(R.drawable.ic_sent);
                }
            }
        } else {
            mTextViewOperationType.setText(R.string.getting_info);
            progressIndicator.setVisibility(View.VISIBLE);
            mImageViewIcon.setVisibility(View.GONE);
            contractIndicator.setBackgroundColor(Color.TRANSPARENT);
        }

        mTextViewID.setText(history.getTxHash());


        mTextViewValue.setText(convertBalanceToString(history.getChangeInBalance()));
    }
}