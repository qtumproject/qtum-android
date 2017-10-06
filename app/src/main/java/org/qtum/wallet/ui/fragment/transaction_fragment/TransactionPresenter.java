package org.qtum.wallet.ui.fragment.transaction_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

interface TransactionPresenter extends BaseFragmentPresenter {
    void openTransactionView(int position);
}
