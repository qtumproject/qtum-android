package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenNameFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.QtumToken;

class SetTokenNameFragmentInteractorImpl implements SetTokenNameFragmentInteractor{

    SetTokenNameFragmentInteractorImpl(){

    }

    @Override
    public void setTokenName(String name) {
        QtumToken.getInstance().setTokenName(name);
    }

    @Override
    public void setTokenSymbol(String symbol) {
        QtumToken.getInstance().setTokenSymbol(symbol);
    }

    @Override
    public String getTokenName() {
        return  QtumToken.getInstance().getTokenName();
    }

    @Override
    public String getTokenSymbol() {
        return QtumToken.getInstance().getTokenSymbol();
    }
}
