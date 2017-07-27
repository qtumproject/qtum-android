package com.pixelplex.qtum.ui.fragment.AddressListFragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.restAPI.QtumService;
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

    @BindView(R.id.spinner)
    ProgressBar mSpinner;

    List<UnspentOutput> mUnspentOutputList;


    protected AddressWithBalanceHolder(View itemView, final OnAddressClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddressClick();
            }
        });
        ButterKnife.bind(this, itemView);
    }

    public void bindAddress(String address){
        mTextViewAddress.setText(address);
        mSpinner.setVisibility(View.VISIBLE);
        mTextViewAddressBalance.setVisibility(View.GONE);
        mTextViewSymbol.setVisibility(View.GONE);

        QtumService.newInstance().getUnspentOutputs(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {
                        for(Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext();){
                            UnspentOutput unspentOutput = iterator.next();
                            if(!unspentOutput.isOutputAvailableToPay()){
                                iterator.remove();
                            }
                        }
                        Collections.sort(unspentOutputs, new Comparator<UnspentOutput>() {
                            @Override
                            public int compare(UnspentOutput unspentOutput, UnspentOutput t1) {
                                return unspentOutput.getAmount().doubleValue() < t1.getAmount().doubleValue() ? 1 : unspentOutput.getAmount().doubleValue() > t1.getAmount().doubleValue() ? -1 : 0;
                            }
                        });

                        mUnspentOutputList = unspentOutputs;

                        BigDecimal balance = new BigDecimal("0");
                        BigDecimal amount;
                        for(UnspentOutput unspentOutput : unspentOutputs){
                            amount = new BigDecimal(String.valueOf(unspentOutput.getAmount()));
                            balance = balance.add(amount);
                        }
                        mTextViewSymbol.setVisibility(View.VISIBLE);
                        mTextViewAddressBalance.setVisibility(View.VISIBLE);
                        mSpinner.setVisibility(View.GONE);
                        mTextViewAddressBalance.setText(balance.toString());
                        mTextViewSymbol.setText(" QTUM");

                    }
                });
    }
}
