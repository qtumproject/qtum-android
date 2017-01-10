package org.qtum.mromanovsky.qtum.ui.fragment.SendFragment;


import android.view.WindowManager;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

public class SendFragment extends BaseFragment implements SendFragmentView {

    public static final int LAYOUT = R.layout.fragment_send;
    public static final String TAG = "SendFragment";

    SendFragmentPresenterImpl mSendFragmentPresenter;

    public static SendFragment newInstance() {
        SendFragment sendFragment = new SendFragment();
        return sendFragment;
    }

    @Override
    protected void createPresenter() {
        mSendFragmentPresenter = new SendFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mSendFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
}
