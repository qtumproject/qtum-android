package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenConfirmFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


class SetTokenConfirmFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetTokenConfirmFragmentPresenter {

    private SetTokenConfirmFragmentView mSetTokenConfirmFragmentView;

    SetTokenConfirmFragmentPresenterImpl(SetTokenConfirmFragmentView setTokenConfirmFragmentView){
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
