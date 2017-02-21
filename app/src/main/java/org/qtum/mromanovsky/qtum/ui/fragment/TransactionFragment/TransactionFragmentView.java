package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

interface TransactionFragmentView extends BaseFragmentView {
    void setUpTransactionData(double value, String receivedTime, String from, String to);
}
