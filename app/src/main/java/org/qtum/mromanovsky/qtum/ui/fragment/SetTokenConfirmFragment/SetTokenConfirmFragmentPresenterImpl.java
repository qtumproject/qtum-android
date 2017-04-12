package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenConfirmFragment;

import android.util.Log;
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
        getInteractor().sendToken(new SetTokenConfirmFragmentInteractorImpl.SendTokenCallBack() {
            @Override
            public void onSuccess() {
                Log.d("yes","yeah");
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
