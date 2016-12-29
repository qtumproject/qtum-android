package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

public class ReceiveFragment extends BaseFragment implements ReceiveFragmentView{

    ReceiveFragmentPresenterImpl mReceiveFragmentPresenter;

    public static final int  LAYOUT = R.layout.fragment_receive;
    public static final String TAG = "SendFragment";

    public static ReceiveFragment newInstance(){
        ReceiveFragment receiveFragment = new ReceiveFragment();
        return  receiveFragment;
    }

    @Override
    protected void createPresenter() {
        mReceiveFragmentPresenter = new ReceiveFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mReceiveFragmentPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(LAYOUT,container,false);
    }
}
