package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


public class ReceiveFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ReceiveFragmentPresenter{

    private ReceiveFragmentView mReceiveFragmentView;

    public ReceiveFragmentPresenterImpl(ReceiveFragmentView receiveFragmentView){
        mReceiveFragmentView = receiveFragmentView;
    }

    @Override
    public ReceiveFragmentView getView() {
        return mReceiveFragmentView;
    }
}
