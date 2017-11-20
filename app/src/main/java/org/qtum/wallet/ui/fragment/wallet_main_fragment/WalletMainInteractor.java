package org.qtum.wallet.ui.fragment.wallet_main_fragment;

import org.qtum.wallet.model.contract.Token;

import java.util.List;

import rx.Observable;

public interface WalletMainInteractor {
    Observable<List<Token>> getTokensObservable();
}
