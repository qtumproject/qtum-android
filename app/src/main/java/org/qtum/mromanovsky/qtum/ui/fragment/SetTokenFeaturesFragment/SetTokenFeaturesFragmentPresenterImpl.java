package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenFeaturesFragment;

import org.qtum.mromanovsky.qtum.datastorage.QtumToken;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SetTokenParametersFragment.SetTokenParametersFragment;


public class SetTokenFeaturesFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetTokenFeaturesFragmentPresenter {

    SetTokenFeaturesFragmentView mSetTokenFeaturesFragmentView;

    public SetTokenFeaturesFragmentPresenterImpl(SetTokenFeaturesFragmentView setTokenFeaturesFragmentView){
        mSetTokenFeaturesFragmentView = setTokenFeaturesFragmentView;
    }

    @Override
    public SetTokenFeaturesFragmentView getView() {
        return mSetTokenFeaturesFragmentView;
    }

    @Override
    public void onNextClick(boolean isAutomaticSellingAndBuying, boolean isFreezingOfAssets,
                            boolean isAutorefill, boolean isProofOfWork) {
        QtumToken.getQtumToken().setAutomaticSellingAndBuying(isAutomaticSellingAndBuying);
        QtumToken.getQtumToken().setFreezingOfAssets(isFreezingOfAssets);
        QtumToken.getQtumToken().setAutorefill(isAutorefill);
        QtumToken.getQtumToken().setProofOfWork(isProofOfWork);
        SetTokenParametersFragment setTokenParametersFragment = SetTokenParametersFragment.newInstance();
        getView().openFragment(setTokenParametersFragment);
    }

    @Override
    public void onBackClick() {
        getView().getFragmentActivity().onBackPressed();
    }
}
