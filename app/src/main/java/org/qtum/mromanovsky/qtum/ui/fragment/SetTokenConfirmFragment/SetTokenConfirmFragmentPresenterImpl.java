package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenConfirmFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


public class SetTokenConfirmFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetTokenConfirmFragmentPresenter {

    SetTokenConfirmFragmentView mSetTokenConfirmFragmentView;

    public SetTokenConfirmFragmentPresenterImpl(SetTokenConfirmFragmentView setTokenConfirmFragmentView){
        mSetTokenConfirmFragmentView = setTokenConfirmFragmentView;
    }

    @Override
    public SetTokenConfirmFragmentView getView() {
        return mSetTokenConfirmFragmentView;
    }

    @Override
    public void onConfirmClick() {
        
    }

    @Override
    public void onBackClick() {
        getView().getFragmentActivity().onBackPressed();
    }
}
