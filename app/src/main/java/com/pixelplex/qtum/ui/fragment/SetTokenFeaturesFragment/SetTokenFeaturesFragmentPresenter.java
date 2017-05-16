package com.pixelplex.qtum.ui.fragment.SetTokenFeaturesFragment;


interface SetTokenFeaturesFragmentPresenter {
    void onNextClick(boolean isAutomaticSellingAndBuying, boolean isFreezingOfAssets,
                     boolean isAutorefill, boolean isProofOfWork);
    void onBackClick();
}
