package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenConfirmFragment;

import android.widget.Toast;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


class SetTokenConfirmFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetTokenConfirmFragmentPresenter {

    private SetTokenConfirmFragmentView mSetTokenConfirmFragmentView;
    private SetTokenConfirmFragmentInteractorImpl mSetTokenConfirmFragmentInteractor;

    SetTokenConfirmFragmentPresenterImpl(SetTokenConfirmFragmentView setTokenConfirmFragmentView){
        mSetTokenConfirmFragmentView = setTokenConfirmFragmentView;
        mSetTokenConfirmFragmentInteractor = new SetTokenConfirmFragmentInteractorImpl();
    }

    @Override
    public SetTokenConfirmFragmentView getView() {
        return mSetTokenConfirmFragmentView;
    }

    @Override
    public void onConfirmClick() {
        getInteractor().generateTokenBytecode(new SetTokenConfirmFragmentInteractorImpl.GenerateTokenBytecodeCallBack() {
            @Override
            public void onSuccess(String byteCode) {
                Toast.makeText(getView().getContext(),byteCode,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackClick() {
        getView().getFragmentActivity().onBackPressed();
    }

    public SetTokenConfirmFragmentInteractorImpl getInteractor() {
        return mSetTokenConfirmFragmentInteractor;
    }
}
