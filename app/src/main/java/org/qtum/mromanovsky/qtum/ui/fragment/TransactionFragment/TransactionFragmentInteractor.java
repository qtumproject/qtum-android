package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.utils.Transaction;

public interface TransactionFragmentInteractor {
    Transaction getTransaction(int position);
}
