package com.pixelplex.qtum.ui.fragment.start_page_fragment;

import android.content.Context;

import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.create_wallet_name_fragment.CreateWalletNameFragment;
import com.pixelplex.qtum.ui.fragment.import_wallet_fragment.ImportWalletFragment;


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
