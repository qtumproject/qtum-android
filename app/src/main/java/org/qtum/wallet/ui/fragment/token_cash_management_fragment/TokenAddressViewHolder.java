package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.model.DeterministicKeyWithTokenBalance;
import org.qtum.wallet.utils.FontTextView;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public class TokenAddressViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.address_name)
    FontTextView mTextViewAddress;

    @BindView(R.id.address_balance)
    FontTextView mTextViewAddressBalance;

    @BindView(R.id.address_symbol)
    FontTextView mTextViewSymbol;

    DeterministicKeyWithTokenBalance item;

    String currency;
    int decimalUnits;

    public TokenAddressViewHolder(final View itemView, final OnAddressTokenClickListener listener, String currency, int decimalUnits) {
        super(itemView);
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

        mTextViewAddressBalance.setText((item.getBalance() != null) ? String.valueOf(item.getBalance().divide(new BigDecimal(Math.pow(10, decimalUnits)))) : "0");
        mTextViewSymbol.setText(String.format(" %s", currency));
    }
}
