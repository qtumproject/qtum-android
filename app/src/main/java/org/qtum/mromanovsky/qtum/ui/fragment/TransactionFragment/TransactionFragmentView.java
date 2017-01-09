package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

public interface TransactionFragmentView extends BaseFragmentView{
    void setUpTransactionData(Double value, String receivedTime, String from, String to);
}
