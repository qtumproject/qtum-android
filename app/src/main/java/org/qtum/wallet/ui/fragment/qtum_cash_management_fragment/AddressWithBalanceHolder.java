package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.qtum.wallet.model.DeterministicKeyWithBalance;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddressWithBalanceHolder extends RecyclerView.ViewHolder {

    @BindView(org.qtum.wallet.R.id.address_name)
    FontTextView mTextViewAddress;

    @BindView(org.qtum.wallet.R.id.address_balance)
    FontTextView mTextViewAddressBalance;

    @BindView(org.qtum.wallet.R.id.address_symbol)
    FontTextView mTextViewSymbol;

    DeterministicKeyWithBalance mDeterministicKeyWithBalance;


    protected AddressWithBalanceHolder(View itemView, final OnAddressClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(mDeterministicKeyWithBalance);
            }
        });
        ButterKnife.bind(this, itemView);
    }

    public void bindDeterministicKeyWithBalance(final DeterministicKeyWithBalance deterministicKeyWithBalance){
        mDeterministicKeyWithBalance = deterministicKeyWithBalance;
        mTextViewAddress.setText(deterministicKeyWithBalance.getAddress());

        mTextViewAddressBalance.setText(deterministicKeyWithBalance.getBalance().toString());
        mTextViewSymbol.setText(" QTUM");
    }
}
