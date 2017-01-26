package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.QrCodeRecognitionFragment.QrCodeRecognitionFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendFragment.SendFragment;


public class SendBaseFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SendBaseFragmentPresenter {

    private SendBaseFragmentView mSendBaseFragmentView;
    private SendFragment sendFragment = SendFragment.newInstance();

    public SendBaseFragmentPresenterImpl(SendBaseFragmentView sendBaseFragmentView){
        mSendBaseFragmentView = sendBaseFragmentView;
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

    @Override
    public void isQrCodeRecognition(boolean isQrCodeRecognition) {

        if(isQrCodeRecognition){
            QrCodeRecognitionFragment qrCodeRecognitionFragment = QrCodeRecognitionFragment.newInstance();
            getView().openInnerFragment(sendFragment);
            getView().openInnerFragmentForResult(qrCodeRecognitionFragment);

        } else {
            getView().openInnerFragment(sendFragment);

        }
    }

    @Override
    public void onResponse(String publicAddress, double amount) {
        sendFragment.onResponse(publicAddress,amount);
    }


}
