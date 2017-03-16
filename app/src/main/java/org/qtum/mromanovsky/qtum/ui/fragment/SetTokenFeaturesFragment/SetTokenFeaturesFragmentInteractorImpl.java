package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenFeaturesFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.QtumToken;

class SetTokenFeaturesFragmentInteractorImpl implements SetTokenFeaturesFragmentInteractor{

    SetTokenFeaturesFragmentInteractorImpl(){

    }

    @Override
    public void setAutomaticSellingAndBuying(boolean automaticSellingAndBuying) {
        QtumToken.getInstance().setAutomaticSellingAndBuying(automaticSellingAndBuying);
    }

    @Override
    public void setAutorefill(boolean autorefill) {
        QtumToken.getInstance().setAutorefill(autorefill);
    }

    @Override
    public void setProofOfWork(boolean proofOfWork) {
        QtumToken.getInstance().setProofOfWork(proofOfWork);
    }

    @Override
    public void setFreezingOfAssets(boolean freezingOfAssets) {
        QtumToken.getInstance().setFreezingOfAssets(freezingOfAssets);
    }

    @Override
    public boolean getAutomaticSellingAndBuying() {
        return QtumToken.getInstance().isAutomaticSellingAndBuying();
    }

    @Override
    public boolean getAutorefill() {
        return QtumToken.getInstance().isAutorefill();
    }

    @Override
    public boolean getProofOfWork() {
        return QtumToken.getInstance().isProofOfWork();
    }

    @Override
    public boolean getFreezingOfAssets() {
        return QtumToken.getInstance().isFreezingOfAssets();
    }
}
