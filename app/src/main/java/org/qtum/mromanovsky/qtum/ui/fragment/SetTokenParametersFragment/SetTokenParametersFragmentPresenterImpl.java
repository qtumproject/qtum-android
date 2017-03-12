package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenParametersFragment;

import org.qtum.mromanovsky.qtum.datastorage.QtumToken;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SetTokenConfirmFragment.SetTokenConfirmFragment;


public class SetTokenParametersFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetTokenParametersFragmentPresenter {

    SetTokenParametersFragmentView mSetTokenParametersFragmentView;

    public SetTokenParametersFragmentPresenterImpl(SetTokenParametersFragmentView setTokenParametersFragmentView){
        mSetTokenParametersFragmentView = setTokenParametersFragmentView;
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

        QtumToken.getQtumToken().setInitialSupply(Long.parseLong(initialSupply));
        QtumToken.getQtumToken().setDecimalUnits(Long.parseLong(decimalUnits));
        SetTokenConfirmFragment setTokenConfirmFragment = SetTokenConfirmFragment.newInstance();
        getView().openFragment(setTokenConfirmFragment);
    }

    @Override
    public void onBackClick() {
        getView().getFragmentActivity().onBackPressed();
    }
}
