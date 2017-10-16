package org.qtum.wallet.ui.activity.main_activity;

import android.content.Context;

import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.NewsList;
import org.qtum.wallet.datastorage.QtumNetworkState;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;
import org.qtum.wallet.model.gson.DGPInfo;
import org.qtum.wallet.model.gson.FeePerKb;

import rx.Observable;


class MainActivityInteractorImpl implements MainActivityInteractor {

    private Context mContext;

    MainActivityInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public boolean getKeyGeneratedInstance() {
        return QtumSharedPreference.getInstance().getKeyGeneratedInstance(mContext);
    }

    @Override
    public void clearStatic(){
        KeyStorage.getInstance().clearKeyStorage();
        HistoryList.getInstance().clearHistoryList();
        NewsList.getInstance().clearNewsList();
    }

    @Override
    public Observable<DGPInfo> getDGPInfo() {
        return QtumService.newInstance().getDGPInfo();
    }

    @Override
    public boolean isDGPInfoLoaded() {
        return QtumNetworkState.newInstance().getDGPInfo()!=null;
    }

    @Override
    public boolean isFeePerkbLoaded() {
        return QtumNetworkState.newInstance().getFeePerKb()!=null;
    }

    @Override
    public void addLanguageChangeListener(LanguageChangeListener languageChangeListener) {
        QtumSharedPreference.getInstance().addLanguageListener(languageChangeListener);
    }

    @Override
    public void removeLanguageChangeListener(LanguageChangeListener languageChangeListener) {
        QtumSharedPreference.getInstance().removeLanguageListener(languageChangeListener);
    }

    @Override
    public Observable<FeePerKb> getFeePerKb() {
        return QtumService.newInstance().getEstimateFeePerKb(2);
    }

    @Override
    public void setDGPInfo(DGPInfo dgpInfo) {
        QtumNetworkState.newInstance().setDGPInfo(dgpInfo);
    }

    @Override
    public void setFeePerKb(FeePerKb feePerKb) {
        QtumNetworkState.newInstance().setFeePerKb(feePerKb);
    }
}
