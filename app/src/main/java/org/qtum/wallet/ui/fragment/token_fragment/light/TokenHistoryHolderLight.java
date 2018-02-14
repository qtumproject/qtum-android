package org.qtum.wallet.ui.fragment.token_fragment.light;

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
import org.qtum.wallet.model.gson.token_history.TokenHistory;
import org.qtum.wallet.ui.fragment.token_fragment.TokenHistoryClickListener;
import org.qtum.wallet.ui.fragment.wallet_fragment.TransactionClickListener;
import org.qtum.wallet.utils.ClipboardUtils;
import org.qtum.wallet.utils.DateCalculator;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import rx.Subscriber;
import rx.Subscription;

public class TokenHistoryHolderLight extends RecyclerView.ViewHolder {

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
    @BindView(R.id.progress_indicator)
    View progressIndicator;
    @BindView(R.id.tv_getting_info)
    TextView mTextViewGettingInfo;
    Subscription mSubscription;
    String mSymbol;
    TokenHistory mTokenHistory;

    int decimalUnits;

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

    TokenHistoryHolderLight(View itemView, final TokenHistoryClickListener listener, int decimalUnits) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onTokenHistoryClick(mTokenHistory.getTxHash());
            }
        });
        ButterKnife.bind(this, itemView);
        this.decimalUnits = decimalUnits;
    }

    void bindTransactionData(final TokenHistory history, String symbol) {
        mTokenHistory = history;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSymbol = symbol;
        mImageViewIcon.setVisibility(View.VISIBLE);
        mTextViewDate.setVisibility(View.VISIBLE);
        progressIndicator.setVisibility(View.GONE);
        mTextViewGettingInfo.setVisibility(View.GONE);
        mLinearLayoutTransaction.setBackgroundResource(android.R.color.transparent);
        if (history.isReceiptUpdated()) {
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
                mImageViewIcon.setImageResource(R.drawable.ic_confirmation_loader);
                mTextViewDate.setText(R.string.confirmation);
                mLinearLayoutTransaction.setBackgroundResource(R.color.bottom_nav_bar_color_light);
            }
            switch (history.getHistoryType()) {
                case Sent:
                    mImageViewIcon.setImageResource(R.drawable.ic_sended_light);
                    break;
                case Received:
                    mImageViewIcon.setImageResource(R.drawable.ic_received_light);
                    break;
                case Internal_Transaction:
                    mImageViewIcon.setImageResource(R.drawable.ic_sent_to_myself_light);
                    break;
            }
        }else {
            mImageViewIcon.setVisibility(View.GONE);
            mTextViewDate.setVisibility(View.GONE);
            progressIndicator.setVisibility(View.VISIBLE);
            mTextViewGettingInfo.setVisibility(View.VISIBLE);
        }
        mTextViewID.setText(history.getTxHash());

        String resultamount = history.getAmount();

        if(decimalUnits > 0) {
           resultamount = new BigDecimal(history.getAmount()).divide(new BigDecimal("10").pow(decimalUnits)).toString();
        }

        mTextViewValue.setText(getSpannedBalance(resultamount) + mSymbol);
    }

    private SpannableString getSpannedBalance(String balance) {
        SpannableString span = new SpannableString(balance);
        if (balance.length() > 4) {
            span.setSpan(new RelativeSizeSpan(.6f), balance.length() - 4, balance.length(), 0);
        }
        return span;
    }
}
