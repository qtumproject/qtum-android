package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.QtumSettingSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;

import java.lang.ref.WeakReference;
import java.util.List;

public class MyContractsInteractorImpl implements MyContractsInteractor {
    private WeakReference<Context> mContext;

    public MyContractsInteractorImpl(Context context) {
        mContext = new WeakReference<>(context);
    }

    @Override
    public List<Contract> getContracts() {
        TinyDB tinyDB = new TinyDB(mContext.get());
        return tinyDB.getContractList();
    }

    @Override
    public List<Contract> getContractsWithoutTokens() {
        TinyDB tinyDB = new TinyDB(mContext.get());
        return tinyDB.getContractListWithoutToken();
    }

    @Override
    public List<Token> getTokens() {
        TinyDB tinyDB = new TinyDB(mContext.get());
        return tinyDB.getTokenList();
    }

    @Override
    public void setContractWithoutTokens(List<Contract> contracts) {
        TinyDB tinyDB = new TinyDB(mContext.get());
        tinyDB.putContractListWithoutToken(contracts);
    }

    @Override
    public void setTokens(List<Token> tokens) {
        TinyDB tinyDB = new TinyDB(mContext.get());
        tinyDB.putTokenList(tokens);
    }

    @Override
    public boolean isShowWizard() {
        QtumSettingSharedPreference qtumSettingSharedPreference = new QtumSettingSharedPreference();
        return qtumSettingSharedPreference.getShowContractsDeleteUnsubscribeWizard(mContext.get());
    }

    @Override
    public void setShowWizard(boolean isShow) {
        QtumSettingSharedPreference qtumSettingSharedPreference = new QtumSettingSharedPreference();
        qtumSettingSharedPreference.setShowContractsDeleteUnsubscribeWizard(mContext.get(), isShow);
    }
}
