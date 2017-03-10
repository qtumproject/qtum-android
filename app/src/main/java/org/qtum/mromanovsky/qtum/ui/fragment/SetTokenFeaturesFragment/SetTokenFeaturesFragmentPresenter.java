package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenFeaturesFragment;


public interface SetTokenFeaturesFragmentPresenter {
    void onNextClick(boolean isAutomaticSellingAndBuying, boolean isFreezingOfAssets,
                     boolean isAutorefill, boolean isProofOfWork);
    void onBackClick();
}
