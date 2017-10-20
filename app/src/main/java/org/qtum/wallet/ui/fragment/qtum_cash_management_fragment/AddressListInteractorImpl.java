package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import android.content.Context;

import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.datastorage.KeyStorage;

import org.bitcoinj.crypto.DeterministicKey;
import org.qtum.wallet.model.DeterministicKeyWithBalance;
import org.qtum.wallet.model.gson.UnspentOutput;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class AddressListInteractorImpl implements AddressListInteractor {

    private Context mContext;

    AddressListInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public List<DeterministicKey> getKeyList() {
        return KeyStorage.getInstance().getKeyList(10);
    }

    public List<Observable<OutputWrapper>> unspentQutputsObservable(Action1<OutputWrapper> actionOnNext) {
        List<Observable<OutputWrapper>> observables = new ArrayList<>();
        for (final DeterministicKey deterministicKey : getKeyList()) {
            final DeterministicKeyWithBalance deterministicKeyWithBalance = new DeterministicKeyWithBalance(deterministicKey);
            Observable<OutputWrapper> observable =
                    QtumService.newInstance().getUnspentOutputs(deterministicKeyWithBalance.getAddress())
                            .subscribeOn(Schedulers.io())
                            .map(new Func1<List<UnspentOutput>, OutputWrapper>() {
                                @Override
                                public OutputWrapper call(List<UnspentOutput> unspentOutputs) {
                                    return new OutputWrapper(deterministicKeyWithBalance, unspentOutputs);
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(actionOnNext);
            observables.add(observable);
        }

        return observables;

    }

    public static class OutputWrapper {
        private DeterministicKeyWithBalance deterministicKeyWithBalance;
        private List<UnspentOutput> unspentOutputs;

        public OutputWrapper() {

        }

        public OutputWrapper(DeterministicKeyWithBalance deterministicKeyWithBalance, List<UnspentOutput> unspentOutputs) {
            this.deterministicKeyWithBalance = deterministicKeyWithBalance;
            this.unspentOutputs = unspentOutputs;
        }

        public DeterministicKeyWithBalance getDeterministicKeyWithBalance() {
            return deterministicKeyWithBalance;
        }

        public List<UnspentOutput> getUnspentOutputs() {
            return unspentOutputs;
        }
    }

}
