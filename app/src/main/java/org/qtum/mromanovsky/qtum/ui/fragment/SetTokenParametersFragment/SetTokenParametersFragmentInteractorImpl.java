package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenParametersFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.QtumToken;

class SetTokenParametersFragmentInteractorImpl implements SetTokenParametersFragmentInteractor{

    SetTokenParametersFragmentInteractorImpl(){

    }


    @Override
    public void setInitialSupply(String initialSupply) {
        QtumToken.getInstance().setInitialSupply(Long.parseLong(initialSupply));
    }

    @Override
    public void setDecimalUnits(String decimalUnits) {
        QtumToken.getInstance().setDecimalUnits(Long.parseLong(decimalUnits));
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
