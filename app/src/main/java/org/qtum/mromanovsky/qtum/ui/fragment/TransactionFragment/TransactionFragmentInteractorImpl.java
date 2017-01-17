package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.datastorage.TransactionQTUMList;
import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

public class TransactionFragmentInteractorImpl implements TransactionFragmentInteractor {
    @Override
    public TransactionQTUM getTransaction(int position) {
        return TransactionQTUMList.getInstance().getTransactionQTUMList().get(position);
    }
}
