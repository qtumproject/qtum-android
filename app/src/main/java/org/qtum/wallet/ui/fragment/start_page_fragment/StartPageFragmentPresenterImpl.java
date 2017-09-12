package org.qtum.wallet.ui.fragment.start_page_fragment;

import android.content.Context;


import org.qtum.wallet.datastorage.QtumSharedPreference;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.NewsList;
import org.qtum.wallet.datastorage.TinyDB;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.import_wallet_fragment.ImportWalletFragment;
import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;


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
        clearWallet();
        BaseFragment pinFragment = PinFragment.newInstance(PinFragment.CREATING, getView().getContext());
        getView().openFragment(pinFragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        boolean isKeyGenerated = QtumSharedPreference.getInstance().getKeyGeneratedInstance(getView().getContext());
        if(!isKeyGenerated){
            getView().hideLoginButton();
        }
    }

    @Override
    public void importWallet() {
        clearWallet();
        BaseFragment importWalletFragment = ImportWalletFragment.newInstance(getView().getContext());
        getView().openFragment(importWalletFragment);
    }

    private void clearWallet() {
        getView().getMainActivity().onLogout();
        getView().getMainActivity().stopUpdateService();
        QtumSharedPreference.getInstance().clear(getView().getContext());
        KeyStorage.getInstance().clearKeyStorage();
        KeyStorage.getInstance().clearKeyFile(getView().getContext());
        HistoryList.getInstance().clearHistoryList();
        NewsList.getInstance().clearNewsList();
        TinyDB db = new TinyDB(getView().getContext());
        db.clearTokenList();
        db.clearContractList();
        db.clearTemplateList();
    }
}
