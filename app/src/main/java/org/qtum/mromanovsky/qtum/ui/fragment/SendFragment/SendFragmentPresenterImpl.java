package org.qtum.mromanovsky.qtum.ui.fragment.SendFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

public class SendFragmentPresenterImpl extends BaseFragmentPresenterImpl implements SendFragmentPresenter{

    SendFragmentView mSendFragmentView;

    public SendFragmentPresenterImpl(SendFragmentView sendFragmentView){
        mSendFragmentView = sendFragmentView;
    }
}
