package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;

public interface TransactionFragmentInteractor {
    History getHistory(int position);
}
