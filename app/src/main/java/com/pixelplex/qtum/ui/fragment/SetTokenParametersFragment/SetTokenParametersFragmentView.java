package com.pixelplex.qtum.ui.fragment.SetTokenParametersFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface SetTokenParametersFragmentView extends BaseFragmentView{
    void setError(String supplyError, String unitsError);
    void clearError();
    void setData(String initialSupply, String decimalUnits);
}
