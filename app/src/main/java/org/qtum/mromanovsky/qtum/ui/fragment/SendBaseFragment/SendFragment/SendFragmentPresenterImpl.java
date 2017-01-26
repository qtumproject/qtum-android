package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.SendFragment;

import org.qtum.mromanovsky.qtum.btc.Transaction;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment.QrCodeRecognitionFragment.QrCodeRecognitionFragment;


public class SendFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SendFragmentPresenter {

    private SendFragmentView mSendFragmentView;
    private SendFragmentInteractorImpl mSendFragmentInteractor;

    public SendFragmentPresenterImpl(SendFragmentView sendFragmentView) {
        mSendFragmentView = sendFragmentView;
        mSendFragmentInteractor = new SendFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public SendFragmentView getView() {
        return mSendFragmentView;
    }


    public SendFragmentInteractorImpl getInteractor() {
        return mSendFragmentInteractor;
    }

    @Override
    public void send(String[] sendInfo) {
        getInteractor().createTx(sendInfo[0], sendInfo[1], new SendFragmentInteractorImpl.CreateTxCallBack() {
            @Override
            public void onSuccess(Transaction transaction) {
                getInteractor().sendTx(transaction, new SendFragmentInteractorImpl.SendTxCallBack() {
                    @Override
                    public void onSuccess() {
                        //TODO transaction sent
                    }
                });
            }
        });
    }
}
