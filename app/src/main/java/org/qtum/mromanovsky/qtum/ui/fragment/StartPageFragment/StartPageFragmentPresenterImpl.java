package org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment.CreateWalletNameFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment.ImportWalletFragment;

import java.util.List;


public class StartPageFragmentPresenterImpl extends BaseFragmentPresenterImpl implements StartPageFragmentPresenter {

    StartPageFragmentView mStartPageFragmentView;


    public StartPageFragmentPresenterImpl(StartPageFragmentView startPageFragmentView) {
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
        CreateWalletNameFragment createWalletNameFragment = CreateWalletNameFragment.newInstance();
        getView().openFragmentAndAddToBackStack(createWalletNameFragment);
    }

    @Override
    public void importWallet() {
        ImportWalletFragment importWalletFragment = ImportWalletFragment.newInstance();
        getView().openFragmentAndAddToBackStack(importWalletFragment);
    }
}
