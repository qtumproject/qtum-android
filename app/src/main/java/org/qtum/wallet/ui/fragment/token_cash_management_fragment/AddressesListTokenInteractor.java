package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.List;

/**
 * Created by drevnitskaya on 09.10.17.
 */

public interface AddressesListTokenInteractor {
    boolean isCurrencyValid(String currency);

    boolean isAmountValid(String amountString);

    List<DeterministicKey> getKeys(int i);
}
