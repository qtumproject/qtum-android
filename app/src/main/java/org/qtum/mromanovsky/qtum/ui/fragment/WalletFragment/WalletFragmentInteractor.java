package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import org.qtum.mromanovsky.qtum.model.Transaction;

import java.util.List;

public interface WalletFragmentInteractor {
    List<Transaction> getTransactionList();
}
