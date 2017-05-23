package com.pixelplex.qtum.ui.fragment.TransactionFragment;


import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

interface TransactionFragmentView extends BaseFragmentView {
    void setUpTransactionData(String value, String receivedTime, List<String> from, List<String> to, boolean confirmed);
}
