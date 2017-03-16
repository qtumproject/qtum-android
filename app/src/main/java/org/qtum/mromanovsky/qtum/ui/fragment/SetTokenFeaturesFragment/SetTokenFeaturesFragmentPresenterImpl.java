package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenFeaturesFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SetTokenParametersFragment.SetTokenParametersFragment;


class SetTokenFeaturesFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetTokenFeaturesFragmentPresenter {

    private SetTokenFeaturesFragmentView mSetTokenFeaturesFragmentView;
    private SetTokenFeaturesFragmentInteractorImpl mSetTokenFeaturesFragmentInteractor;

    SetTokenFeaturesFragmentPresenterImpl(SetTokenFeaturesFragmentView setTokenFeaturesFragmentView){
        mSetTokenFeaturesFragmentView = setTokenFeaturesFragmentView;
        mSetTokenFeaturesFragmentInteractor = new SetTokenFeaturesFragmentInteractorImpl();
    }

    @Override
    public SetTokenFeaturesFragmentView getView() {
        return mSetTokenFeaturesFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        getView().setData(getInteractor().getFreezingOfAssets(), getInteractor().getAutomaticSellingAndBuying(),
                getInteractor().getAutorefill(),getInteractor().getProofOfWork());
    }

    @Override
    public void onNextClick(boolean automaticSellingAndBuying, boolean freezingOfAssets,
                            boolean autorefill, boolean proofOfWork) {
        getInteractor().setAutomaticSellingAndBuying(automaticSellingAndBuying);
        getInteractor().setFreezingOfAssets(freezingOfAssets);
        getInteractor().setAutorefill(autorefill);
        getInteractor().setProofOfWork(proofOfWork);

        SetTokenParametersFragment setTokenParametersFragment = SetTokenParametersFragment.newInstance();
        getView().openFragment(setTokenParametersFragment);
    }

    @Override
    public void onBackClick() {
        getView().getFragmentActivity().onBackPressed();
    }

    public SetTokenFeaturesFragmentInteractorImpl getInteractor() {
        return mSetTokenFeaturesFragmentInteractor;
    }
}
