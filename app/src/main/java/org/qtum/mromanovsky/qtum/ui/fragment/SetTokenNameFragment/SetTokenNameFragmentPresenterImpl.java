package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenNameFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SetTokenFeaturesFragment.SetTokenFeaturesFragment;


class SetTokenNameFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetTokenNameFragmentPresenter {

    private SetTokenNameFragmentView mSetTokenNameFragmentView;
    private SetTokenNameFragmentInteractorImpl mSetTokenNameFragmentInteractor;

    SetTokenNameFragmentPresenterImpl(SetTokenNameFragmentView setTokenNameFragmentView){
        mSetTokenNameFragmentView = setTokenNameFragmentView;
        mSetTokenNameFragmentInteractor = new SetTokenNameFragmentInteractorImpl();
    }

    @Override
    public SetTokenNameFragmentView getView() {
        return mSetTokenNameFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getView().setData(getInteractor().getTokenName(),getInteractor().getTokenSymbol());
        ((MainActivity) getView().getFragmentActivity()).hideBottomNavigationView();
        getView().showSoftInput();
    }

    @Override
    public void onNextClick(String name, String symbol) {

        getView().clearError();

        if (name.isEmpty() && symbol.isEmpty()) {
            getView().setError("Empty field", "Empty field");
            return;
        } else {
            if (symbol.isEmpty()) {
                getView().setError("", "Empty field");
                return;
            }
            if(name.isEmpty()){
                getView().setError("Empty field","");
                return;
            }
        }

        getInteractor().setTokenName(name);
        getInteractor().setTokenSymbol(symbol);
        SetTokenFeaturesFragment setTokenFeaturesFragment = SetTokenFeaturesFragment.newInstance();
        getView().openFragment(setTokenFeaturesFragment);
    }

    @Override
    public void onCancelClick() {
        getView().getFragmentActivity().onBackPressed();
        getView().hideKeyBoard();
    }

    public SetTokenNameFragmentInteractorImpl getInteractor() {
        return mSetTokenNameFragmentInteractor;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) getView().getFragmentActivity()).showBottomNavigationView();
    }
}
