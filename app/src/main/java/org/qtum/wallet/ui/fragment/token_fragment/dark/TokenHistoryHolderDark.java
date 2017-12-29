package org.qtum.wallet.ui.fragment.token_fragment.dark;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.token_history.TokenHistory;
import org.qtum.wallet.ui.fragment.token_fragment.TokenHistoryClickListener;
import org.qtum.wallet.ui.fragment.wallet_fragment.TransactionClickListener;
import org.qtum.wallet.utils.ClipboardUtils;
import org.qtum.wallet.utils.DateCalculator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import rx.Subscriber;
import rx.Subscription;

public class TokenHistoryHolderDark extends RecyclerView.ViewHolder {

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

    Subscription mSubscription;
    String mSymbol;

    @OnLongClick(R.id.tv_id)
    public boolean onIdLongClick() {
        ClipboardUtils.copyToClipBoard(mTextViewID.getContext(), mTextViewID.getText().toString(), new ClipboardUtils.CopyCallback() {
            @Override
            public void onCopyToClipBoard() {
                Toast.makeText(mTextViewID.getContext(), mTextViewID.getContext().getString(R.string.copied), Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    public TokenHistoryHolderDark(View itemView, final TokenHistoryClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTokenHistoryClick(getAdapterPosition());
            }
        });
        ButterKnife.bind(this, itemView);
    }

    void bindTransactionData(final TokenHistory history, String symbol) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSymbol = symbol;
        if (history.getTxTime() != null) {
            mSubscription = DateCalculator.getUpdater().subscribe(new Subscriber() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(Object o) {
                    mTextViewDate.setText(DateCalculator.getShortDate(history.getTxTime() * 1000L));
                }
            });
            mTextViewDate.setText(DateCalculator.getShortDate(history.getTxTime() * 1000L));

        } else {
            mTextViewDate.setText(mTextViewDate.getContext().getString(R.string.unconfirmed));
        }
        mTextViewID.setText(history.getTxHash());
//        if (history.getChangeInBalance().doubleValue() > 0) {
//            mTextViewOperationType.setText(R.string.received);
//
//            mImageViewIcon.setImageResource(R.drawable.ic_received);
//        } else {
//            mTextViewOperationType.setText(R.string.sent);
//
//            mImageViewIcon.setImageResource(R.drawable.ic_sent);
//        }
        mTextViewValue.setText(history.getAmount()+ mSymbol);
    }
}