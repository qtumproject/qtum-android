package com.pixelplex.qtum.ui.fragment.TransactionFragment;


import com.pixelplex.qtum.model.gson.history.History;

interface TransactionFragmentInteractor {
    History getHistory(int position);
}
