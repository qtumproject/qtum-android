package org.qtum.wallet.ui.fragment.transaction_fragment;


import org.qtum.wallet.model.gson.history.History;

interface TransactionFragmentInteractor {
    History getHistory(int position);
}
