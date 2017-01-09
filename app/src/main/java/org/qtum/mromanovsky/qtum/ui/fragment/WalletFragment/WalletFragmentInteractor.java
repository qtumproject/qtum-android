package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.utils.Transaction;

import java.util.List;

public interface WalletFragmentInteractor {
    List<Transaction> getTransactionList();
}
