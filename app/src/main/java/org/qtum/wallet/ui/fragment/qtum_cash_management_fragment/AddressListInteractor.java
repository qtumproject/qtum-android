package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by drevnitskaya on 05.10.17.
 */

public interface AddressListInteractor {

    List<DeterministicKey> getKeyList();

    List<Observable<AddressListInteractorImpl.OutputWrapper>> unspentQutputsObservable(Action1<AddressListInteractorImpl.OutputWrapper> actionOnNext);
}
