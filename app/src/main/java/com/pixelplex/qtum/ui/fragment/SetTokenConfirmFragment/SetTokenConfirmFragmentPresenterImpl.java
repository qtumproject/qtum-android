package com.pixelplex.qtum.ui.fragment.SetTokenConfirmFragment;

import android.content.Context;
import android.os.Handler;

import com.pixelplex.qtum.dataprovider.NetworkStateReceiver;
import com.pixelplex.qtum.dataprovider.RestAPI.NetworkStateListener;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


class SetTokenConfirmFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SetTokenConfirmFragmentPresenter {

    private SetTokenConfirmFragmentView mSetTokenConfirmFragmentView;
    private SetTokenConfirmFragmentInteractorImpl mSetTokenConfirmFragmentInteractor;
    private NetworkStateReceiver mNetworkStateReceiver;

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
        getView().setProgressDialog("Sending");
        getInteractor().sendToken(new SetTokenConfirmFragmentInteractorImpl.SendTokenCallBack() {
            @Override
            public void onSuccess() {
                getView().dismissProgressDialog();
                getView().setAlertDialog("Sent");
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        getView().dismissAlertDialog();
                    }
                }, 2000);
                getInteractor().clearToken();
            }

            @Override
            public void onError(String error) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(error);
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        getView().dismissAlertDialog();
                    }
                }, 2000);
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

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        mNetworkStateReceiver  = ((MainActivity) getView().getFragmentActivity()).getNetworkReceiver();
        mNetworkStateReceiver.addNetworkStateListener(new NetworkStateListener() {
            @Override
            public void onNetworkConnected() {
                getView().enableSendButton();
            }

            @Override
            public void onNetworkDisconnected() {
                getView().disableSendButton();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNetworkStateReceiver.removeNetworkStateListener();
        //TODO: unsubscribe rx
    }
}