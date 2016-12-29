package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

public class ReceiveFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ReceiveFragmentView{

    private ReceiveFragmentView mReceiveFragmentView;

    public ReceiveFragmentPresenterImpl(ReceiveFragmentView receiveFragmentView){
        mReceiveFragmentView = receiveFragmentView;
    }
}
