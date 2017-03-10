package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenParametersFragment;


public interface SetTokenParametersFragmentPresenter {
    void onFinishClick(long initialSupply, long decimalUnits);
    void onBackClick();
}
