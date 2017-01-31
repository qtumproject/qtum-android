package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;

import android.os.Handler;
import android.util.Log;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.btc.Transaction;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.QrCodeRecognitionFragment.QrCodeRecognitionFragment;


public class SendBaseFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SendBaseFragmentPresenter {

    private SendBaseFragmentView mSendBaseFragmentView;
    private SendBaseFragmentInteractorImpl mSendBaseFragmentInteractor;

    public SendBaseFragmentPresenterImpl(SendBaseFragmentView sendBaseFragmentView){
        mSendBaseFragmentView = sendBaseFragmentView;
        mSendBaseFragmentInteractor = new SendBaseFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public SendBaseFragmentView getView() {
        return mSendBaseFragmentView;
    }

    @Override
    public void onClickQrCode() {
        QrCodeRecognitionFragment qrCodeRecognitionFragment = QrCodeRecognitionFragment.newInstance();
        getView().openInnerFragmentForResult(qrCodeRecognitionFragment);

    }

    public SendBaseFragmentInteractorImpl getInteractor() {
        return mSendBaseFragmentInteractor;
    }

    @Override
    public void isQrCodeRecognition(boolean isQrCodeRecognition) {

        if(isQrCodeRecognition){
            QrCodeRecognitionFragment qrCodeRecognitionFragment = QrCodeRecognitionFragment.newInstance();
            getView().openInnerFragmentForResult(qrCodeRecognitionFragment);

        }
    }

    @Override
    public void onResponse(String publicAddress, double amount) {
        getView().updateData(publicAddress,amount);
    }
    @Override
    public void onResponseError() {
        getView().errorRecognition();
    }

    @Override
    public void send(String[] sendInfo) {
        if (sendInfo[2].length() < 4) {
            getView().confirmError(getView().getContext().getString(R.string.pin_is_not_long_enough));
            return;
        } else {
            int intPassword = Integer.parseInt(sendInfo[2]);
            if (intPassword != getInteractor().getPassword()) {
                getView().confirmError(getView().getContext().getString(R.string.incorrect_pin));
                return;
            }
        }
        getView().clearError();
        getView().setDialogProgressBar("Sending");
        getInteractor().sendTx(sendInfo[0], sendInfo[1], new SendBaseFragmentInteractorImpl.SendTxCallBack() {
            @Override
            public void onSuccess() {
                getView().dismissDialogProgressBar();
            }

            @Override
            public void onError() {
                getView().dismissDialogProgressBar();
                getView().setDialogProgressBar("Error");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        getView().dismissDialogProgressBar();
                    }
                }, 3000);
            }
        });
    }



}
