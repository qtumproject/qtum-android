package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.utils.Transaction;
import org.qtum.mromanovsky.qtum.utils.Transactions;

public class TransactionFragmentInteractorImpl implements TransactionFragmentInteractor{
    @Override
    public Transaction getTransaction(int position) {
        return Transactions.getInstance().getTransactionList().get(position);
    }
}
