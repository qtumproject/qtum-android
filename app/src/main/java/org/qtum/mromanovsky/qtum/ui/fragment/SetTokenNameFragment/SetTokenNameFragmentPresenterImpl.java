package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenNameFragment;

import org.qtum.mromanovsky.qtum.datastorage.QtumToken;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SetTokenFeaturesFragment.SetTokenFeaturesFragment;


public class SetTokenNameFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetTokenNameFragmentPresenter {

    SetTokenNameFragmentView mSetTokenNameFragmentView;

    public SetTokenNameFragmentPresenterImpl(SetTokenNameFragmentView setTokenNameFragmentView){
        mSetTokenNameFragmentView = setTokenNameFragmentView;
    }

    @Override
    public SetTokenNameFragmentView getView() {
        return mSetTokenNameFragmentView;
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

        QtumToken.getQtumToken().setTokenName(name);
        QtumToken.getQtumToken().setTokenSymbol(symbol);
        SetTokenFeaturesFragment setTokenFeaturesFragment = SetTokenFeaturesFragment.newInstance();
        getView().openFragment(setTokenFeaturesFragment);
    }

    @Override
    public void onCancelClick() {
        getView().getFragmentActivity().onBackPressed();
    }
}
