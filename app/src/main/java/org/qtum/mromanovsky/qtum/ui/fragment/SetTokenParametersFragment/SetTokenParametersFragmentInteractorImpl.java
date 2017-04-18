package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenParametersFragment;


import org.qtum.mromanovsky.qtum.datastorage.QtumToken;

class SetTokenParametersFragmentInteractorImpl implements SetTokenParametersFragmentInteractor{

    SetTokenParametersFragmentInteractorImpl(){

    }

    @Override
    public void setInitialSupply(String initialSupply) {
        QtumToken.getInstance().setInitialSupply(Integer.parseInt(initialSupply));
    }

    @Override
    public void setDecimalUnits(String decimalUnits) {
        QtumToken.getInstance().setDecimalUnits(Integer.parseInt(decimalUnits));
    }

    @Override
    public String getInitialSupply() {
        return String.valueOf(QtumToken.getInstance().getInitialSupply());
    }

    @Override
    public String getDecimalUnits() {
        return String.valueOf(QtumToken.getInstance().getDecimalUnits());
    }
}
