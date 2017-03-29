package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface TransactionFragmentView extends BaseFragmentView {
    void setUpTransactionData(double value, String receivedTime, List<String> from, List<String> to);
}
