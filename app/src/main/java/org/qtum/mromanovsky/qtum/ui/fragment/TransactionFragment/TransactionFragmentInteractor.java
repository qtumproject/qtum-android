package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.model.TransactionQTUM;

public interface TransactionFragmentInteractor {
    TransactionQTUM getTransaction(int position);
}
