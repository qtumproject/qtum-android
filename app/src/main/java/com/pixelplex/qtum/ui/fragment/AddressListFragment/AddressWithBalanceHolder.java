package com.pixelplex.qtum.ui.fragment.AddressListFragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.restAPI.QtumService;
import com.pixelplex.qtum.model.DeterministicKeyWithBalance;
import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.utils.FontTextView;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AddressWithBalanceHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.address_name)
    FontTextView mTextViewAddress;

    @BindView(R.id.address_balance)
    FontTextView mTextViewAddressBalance;

    @BindView(R.id.address_symbol)
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
        mDeterministicKeyWithBalance = deterministicKeyWithBalance;;
        mTextViewAddress.setText(deterministicKeyWithBalance.getAddress());

        mTextViewAddressBalance.setText(deterministicKeyWithBalance.getBalance().toString());
        mTextViewSymbol.setText(" QTUM");
    }
}
