package com.pixelplex.qtum.ui.fragment.StartPageFragment;

import android.content.Context;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.CreateWalletNameFragment.CreateWalletNameFragment;
import com.pixelplex.qtum.ui.fragment.ImportWalletFragment.ImportWalletFragment;


class StartPageFragmentPresenterImpl extends BaseFragmentPresenterImpl implements StartPageFragmentPresenter {

    private StartPageFragmentView mStartPageFragmentView;


    StartPageFragmentPresenterImpl(StartPageFragmentView startPageFragmentView) {
        mStartPageFragmentView = startPageFragmentView;
    }

    @Override
    public void onCreate(Context context) {
        super.onCreate(context);
    }

    @Override
    public StartPageFragmentView getView() {
        return mStartPageFragmentView;
    }

    @Override
    public void createNewWallet() {
        BaseFragment createWalletNameFragment = CreateWalletNameFragment.newInstance(getView().getContext(), true);
        getView().openFragment(createWalletNameFragment);
    }

    @Override
    public void importWallet() {
        BaseFragment importWalletFragment = ImportWalletFragment.newInstance(getView().getContext());
        getView().openFragment(importWalletFragment);
    }
}
