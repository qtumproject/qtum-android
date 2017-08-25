package com.pixelplex.qtum.ui.fragment.token_cash_management_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.DeterministicKeyWithTokenBalance;
import com.pixelplex.qtum.utils.FontTextView;

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

    public TokenAddressViewHolder(final View itemView, final OnAddressTokenClickListener listener, String currency) {
        super(itemView);
        this.currency = currency;
        itemView.setClickable(true);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });

        ButterKnife.bind(this, itemView);
    }

    public void bind(DeterministicKeyWithTokenBalance item){
        this.item = item;
        mTextViewAddress.setText(item.getAddress());

        mTextViewAddressBalance.setText((item.getBalance() != null)? String.valueOf(item.getBalance()) : "0");
        mTextViewSymbol.setText(String.format(" %s",currency));
    }
}
