package org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment;

import android.content.Context;
import android.util.Log;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletChangeEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseContextView;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment.CreateWalletNameFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment.ImportWalletFragment;

import java.io.File;


public class StartPageFragmentPresenterImpl extends BaseFragmentPresenterImpl implements StartPageFragmentPresenter {

    StartPageFragmentView mStartPageFragmentView;


    public StartPageFragmentPresenterImpl(StartPageFragmentView startPageFragmentView){
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
        getView().openFragment(createWalletNameFragment);
    }

    @Override
    public void importWallet() {
        ImportWalletFragment importWalletFragment = ImportWalletFragment.newInstance();
        getView().openFragment(importWalletFragment);
    }
}
