package org.qtum.wallet.ui.fragment.import_wallet_fragment;

import rx.Observable;

public interface ImportWalletInteractor {
    Observable<String> importWallet(String seed);
}
