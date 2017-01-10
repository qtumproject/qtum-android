package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.datastorage.Transactions;
import org.qtum.mromanovsky.qtum.model.Transaction;

public class TransactionFragmentInteractorImpl implements TransactionFragmentInteractor {
    @Override
    public Transaction getTransaction(int position) {
        return Transactions.getInstance().getTransactionList().get(position);
    }
}
