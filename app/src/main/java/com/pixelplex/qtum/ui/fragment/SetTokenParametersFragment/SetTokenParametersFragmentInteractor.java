package com.pixelplex.qtum.ui.fragment.SetTokenParametersFragment;


interface SetTokenParametersFragmentInteractor {
    void setInitialSupply(String initialSupply);
    void setDecimalUnits(String decimalUnits);
    String getInitialSupply();
    String getDecimalUnits();
}
