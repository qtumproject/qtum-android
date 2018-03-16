package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.model.DeterministicKeyWithTokenBalance;
import org.qtum.wallet.utils.ClipboardUtils;

import org.qtum.wallet.utils.FontTextView;

import java.math.BigDecimal;
import java.math.MathContext;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TokenAddressViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.address_name)
    FontTextView mTextViewAddress;

    @BindView(R.id.address_balance)
    FontTextView mTextViewAddressBalance;

    @BindView(R.id.address_symbol)
    FontTextView mTextViewSymbol;

    DeterministicKeyWithTokenBalance item;

    OnAddressTokenClickListener listener;

    @OnLongClick(R.id.address_name)
    public boolean onAddressLongClick() {
        ClipboardUtils.copyToClipBoard(mTextViewAddress.getContext(), mTextViewAddress.getText().toString(), new ClipboardUtils.CopyCallback() {
            @Override
            public void onCopyToClipBoard() {
                Toast.makeText(mTextViewAddress.getContext(), mTextViewAddress.getContext().getString(R.string.copied), Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    @OnClick(R.id.address_name)
    public void onAddressClick() {
        onItemClick();
    }

    String currency;
    int decimalUnits;

    public TokenAddressViewHolder(final View itemView, final OnAddressTokenClickListener listener, String currency, int decimalUnits) {
        super(itemView);
        this.listener = listener;
        this.currency = currency;
        itemView.setClickable(true);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
        this.decimalUnits = decimalUnits;
        ButterKnife.bind(this, itemView);
    }

    public void bind(DeterministicKeyWithTokenBalance item) {
        this.item = item;
        mTextViewAddress.setText(item.getAddress());
        String balance;
        try {
            balance = (item.getBalance() != null && !item.getBalance().toString().equals("0")) ? String.valueOf(item.getBalance().divide(new BigDecimal(Math.pow(10, decimalUnits)), MathContext.DECIMAL128)) : "0";
        } catch (Exception e){
            balance = "0";
        }
        if (mTextViewAddressBalance.getLayoutParams().width == WRAP_CONTENT) {
            mTextViewAddressBalance.setLongNumberText(balance, itemView.getContext().getResources().getDisplayMetrics().widthPixels / 2);
        } else {
            mTextViewAddressBalance.setLongNumberText(balance);
        }
        mTextViewSymbol.setText(String.format(" %s", currency));
    }

    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 1000;

    private void onItemClick(){
        long now = System.currentTimeMillis();
        if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
            return;
        }
        mLastClickTime = now;
        listener.onItemClick(item);
    }
}
