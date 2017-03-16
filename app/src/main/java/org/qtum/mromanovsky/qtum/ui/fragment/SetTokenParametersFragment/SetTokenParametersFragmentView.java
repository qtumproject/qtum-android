package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenParametersFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface SetTokenParametersFragmentView extends BaseFragmentView{
    void setError(String supplyError, String unitsError);
    void clearError();
    void setData(String initialSupply, String decimalUnits);
}
