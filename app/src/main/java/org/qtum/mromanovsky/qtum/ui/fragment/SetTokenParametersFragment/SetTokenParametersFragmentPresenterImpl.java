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
    public void onFinishClick(long initialSupply, long decimalUnits) {
        QtumToken.getQtumToken().setInitialSupply(initialSupply);
        QtumToken.getQtumToken().setDecimalUnits(decimalUnits);
        SetTokenConfirmFragment setTokenConfirmFragment = SetTokenConfirmFragment.newInstance();
        getView().openFragment(setTokenConfirmFragment);
    }

    @Override
    public void onBackClick() {
        getView().getFragmentActivity().onBackPressed();
    }
}
