package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenParametersFragment;

import org.qtum.mromanovsky.qtum.datastorage.QtumToken;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SetTokenConfirmFragment.SetTokenConfirmFragment;


class SetTokenParametersFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetTokenParametersFragmentPresenter {

    private SetTokenParametersFragmentView mSetTokenParametersFragmentView;
    private SetTokenParametersFragmentInteractorImpl mSetTokenParametersFragmentInteractor;

    SetTokenParametersFragmentPresenterImpl(SetTokenParametersFragmentView setTokenParametersFragmentView){
        mSetTokenParametersFragmentView = setTokenParametersFragmentView;
        mSetTokenParametersFragmentInteractor = new SetTokenParametersFragmentInteractorImpl();
    }

    @Override
    public SetTokenParametersFragmentView getView() {
        return mSetTokenParametersFragmentView;
    }

    @Override
    public void onFinishClick(String initialSupply, String decimalUnits) {
        getView().clearError();

        if (initialSupply.isEmpty() && decimalUnits.isEmpty()) {
            getView().setError("Empty field", "Empty field");
            return;
        } else {
            if (initialSupply.isEmpty()) {
                getView().setError("Empty field", "");
                return;
            }
            if(decimalUnits.isEmpty()){
                getView().setError("","Empty field");
                return;
            }
        }

        getInteractor().setInitialSupply(initialSupply);
        getInteractor().setDecimalUnits(decimalUnits);
        SetTokenConfirmFragment setTokenConfirmFragment = SetTokenConfirmFragment.newInstance();
        getView().openFragment(setTokenConfirmFragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getView().setData(getInteractor().getInitialSupply(), getInteractor().getDecimalUnits());
    }

    @Override
    public void onBackClick() {
        getView().getFragmentActivity().onBackPressed();
        getView().hideKeyBoard();
    }

    public SetTokenParametersFragmentInteractorImpl getInteractor() {
        return mSetTokenParametersFragmentInteractor;
    }
}
