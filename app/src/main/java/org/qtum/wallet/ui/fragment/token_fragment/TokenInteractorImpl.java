package org.qtum.wallet.ui.fragment.token_fragment;

import android.content.Context;

import org.qtum.wallet.dataprovider.rest_api.qtum.QtumService;
import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.token_history.TokenHistory;
import org.qtum.wallet.model.gson.token_history.TokenHistoryResponse;
import org.qtum.wallet.utils.ContractManagementHelper;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TokenInteractorImpl implements TokenInteractor {

    private WeakReference<Context> mContext;

    public TokenInteractorImpl(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override
    public String getCurrentAddress() {
        return KeyStorage.getInstance().getCurrentAddress();
    }

    @Override
    public String readAbiContract(String uiid) {
        return FileStorageManager.getInstance().readAbiContract(mContext.get(), uiid);
    }

    @Override
    public void setupPropertyTotalSupplyValue(Token token, Subscriber<String> stringSubscriber) {
        ContractManagementHelper.getPropertyValue(TokenFragment.totalSupply, token, mContext.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringSubscriber);
    }

    @Override
    public void setupPropertyDecimalsValue(Token token, Subscriber<String> stringSubscriber) {
        ContractManagementHelper.getPropertyValue(TokenFragment.decimals, token, mContext.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringSubscriber);
    }

    @Override
    public void setupPropertySymbolValue(Token token, Subscriber<String> stringSubscriber) {
        ContractManagementHelper.getPropertyValue(TokenFragment.symbol, token, mContext.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringSubscriber);
    }

    @Override
    public void setupPropertyNameValue(Token token, Subscriber<String> stringSubscriber) {
        ContractManagementHelper.getPropertyValue(TokenFragment.name, token, mContext.get())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringSubscriber);
    }

    @Override
    public Token setTokenDecimals(Token token, String value) {
        return new TinyDB(mContext.get()).setTokenDecimals(token, Integer.valueOf(value));
    }

    @Override
    public String handleTotalSupplyValue(Token token, String value) {
        BigDecimal bigDecimalTotalSupply = new BigDecimal(value);
        if (token.getDecimalUnits() != null) {
            BigDecimal divide = bigDecimalTotalSupply.divide(new BigDecimal(Math.pow(10, token.getDecimalUnits())), MathContext.DECIMAL128);
            value = divide.toPlainString();
        }
        return value;
    }

    @Override
    public List<TokenHistory> getHistoryList() {
        return TokenHistoryList.newInstance().getTokenHistories();
    }

    @Override
    public Observable<TokenHistoryResponse> getHistoryList(String contractAddress, int limit, int offset) {
        return QtumService.newInstance().getTokenHistoryList(contractAddress,limit,offset,getAddresses());
    }

    @Override
    public int getTotalHistoryItem() {
        return TokenHistoryList.newInstance().getTotalItems();
    }

//    @Override
//    public void addToHistoryList(TokenHistory history) {
//        TokenHistoryList.newInstance().getTokenHistories().add(0,history);
//    }

    @Override
    public List<String> getAddresses() {
        return KeyStorage.getInstance().getAddresses();
    }


}
