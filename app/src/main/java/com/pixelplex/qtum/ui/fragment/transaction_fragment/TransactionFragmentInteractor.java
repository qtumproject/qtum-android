package com.pixelplex.qtum.ui.fragment.transaction_fragment;


import com.pixelplex.qtum.model.gson.history.History;

interface TransactionFragmentInteractor {
    History getHistory(int position);
}
