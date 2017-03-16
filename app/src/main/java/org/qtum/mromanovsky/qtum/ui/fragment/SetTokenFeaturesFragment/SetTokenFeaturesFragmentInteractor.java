package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenFeaturesFragment;


interface SetTokenFeaturesFragmentInteractor {
    void setAutomaticSellingAndBuying(boolean automaticSellingAndBuying);
    void setAutorefill(boolean autorefill);
    void setProofOfWork(boolean proofOfWork);
    void setFreezingOfAssets(boolean freezingOfAssets);
    boolean getAutomaticSellingAndBuying();
    boolean getAutorefill();
    boolean getProofOfWork();
    boolean getFreezingOfAssets();

}
