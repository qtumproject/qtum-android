package com.pixelplex.qtum.ui.fragment.transaction_fragment;


import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

interface TransactionFragmentView extends BaseFragmentView {
    void setUpTransactionData(String value, String receivedTime, List<String> from, List<String> to, boolean confirmed);
}
