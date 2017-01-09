package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import butterknife.BindView;

public class ReceiveFragment extends BaseFragment implements ReceiveFragmentView{

    ReceiveFragmentPresenterImpl mReceiveFragmentPresenter;

    public static final int  LAYOUT = R.layout.fragment_receive;
    public static final String TAG = "SendFragment";

    @BindView(R.id.toolbar) Toolbar mToolbar;

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

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ((MainActivity) getActivity()).hideBottomNavigationView();
    }
}
