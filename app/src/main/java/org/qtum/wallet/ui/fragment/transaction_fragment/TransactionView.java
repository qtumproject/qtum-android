package org.qtum.wallet.ui.fragment.transaction_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

interface TransactionView extends BaseFragmentView {
    void setUpTransactionData(String value, String receivedTime, List<String> from, List<String> to, boolean confirmed);
}
