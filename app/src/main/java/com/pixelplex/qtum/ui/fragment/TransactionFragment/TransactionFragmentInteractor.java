package com.pixelplex.qtum.ui.fragment.TransactionFragment;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.History;

interface TransactionFragmentInteractor {
    History getHistory(int position);
}
