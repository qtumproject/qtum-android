package com.pixelplex.qtum.ui.fragment.start_page_fragment;

import android.content.Context;
import android.content.Intent;

import com.pixelplex.qtum.dataprovider.services.update_service.UpdateService;
import com.pixelplex.qtum.datastorage.HistoryList;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.datastorage.NewsList;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.datastorage.TinyDB;
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
        clearWallet();
        BaseFragment pinFragment = PinFragment.newInstance(PinFragment.CREATING, getView().getContext());
        getView().openFragment(pinFragment);
    }

    @Override
    public void importWallet() {
        clearWallet();
        BaseFragment importWalletFragment = ImportWalletFragment.newInstance(getView().getContext());
        getView().openFragment(importWalletFragment);
    }

    private void clearWallet() {
        getView().getMainActivity().stopService(new Intent(getView().getMainActivity(), UpdateService.class));
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
