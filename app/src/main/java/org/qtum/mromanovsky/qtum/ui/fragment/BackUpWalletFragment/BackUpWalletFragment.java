package org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;

public class BackUpWalletFragment extends BaseFragment implements BackUpWalletFragmentView {

    public static final int LAYOUT = R.layout.fragment_back_up_wallet;
    BackUpWalletFragmentPresenterImpl mBackUpWalletFragmentPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static BackUpWalletFragment newInstance() {
        BackUpWalletFragment backUpWalletFragment = new BackUpWalletFragment();
        return backUpWalletFragment;
    }

    @Override
    protected void createPresenter() {
        mBackUpWalletFragmentPresenter = new BackUpWalletFragmentPresenterImpl(this);
    }

    @Override
    protected BackUpWalletFragmentPresenterImpl getPresenter() {
        return mBackUpWalletFragmentPresenter;
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
    }
}
