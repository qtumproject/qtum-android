package com.pixelplex.qtum.ui.fragment.start_page_fragment;

import android.content.Context;

import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.import_wallet_fragment.ImportWalletFragment;
import com.pixelplex.qtum.ui.fragment.pin_fragment.PinFragment;


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
        BaseFragment pinFragment = PinFragment.newInstance(PinFragment.CREATING, getView().getContext());
        getView().openFragment(pinFragment);
    }

    @Override
    public void importWallet() {
        BaseFragment importWalletFragment = ImportWalletFragment.newInstance(getView().getContext());
        getView().openFragment(importWalletFragment);
    }
}
