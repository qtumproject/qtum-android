package org.qtum.wallet.ui.fragment.wallet_fragment;

import android.content.Context;
import android.text.TextUtils;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.dataprovider.rest_api.qtum.QtumService;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.datastorage.KeyStorage;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

public class WalletInteractorImpl implements WalletInteractor {

    private Context context;

    public WalletInteractorImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<History> getHistoryList() {
        return HistoryList.getInstance().getHistoryList();
    }

    @Override
    public Observable<HistoryResponse> getHistoryList(int limit, int offest) {
        return QtumService.newInstance().getHistoryListForSeveralAddresses(getAddresses(), limit, offest);
    }

    @Override
    public List<String> getAddresses() {
        return KeyStorage.getInstance().getAddresses();
    }

    @Override
    public int getTotalHistoryItem() {
        return HistoryList.getInstance().getTotalItem();
    }

    @Override
    public void addToHistoryList(History history) {
        HistoryList.getInstance().getHistoryList().add(0, history);
    }

    @Override
    public Integer setHistory(History history) {
        for (History historyReplacing : getHistoryList()) {
            if (historyReplacing.getTxHash().equals(history.getTxHash())) {
                int position = getHistoryList().indexOf(historyReplacing);
                getHistoryList().set(position, history);
                return position;
            }
        }
        getHistoryList().add(0, history);
        return null;
    }

    @Override
    public String getAddress() {
        String s = KeyStorage.getInstance().getCurrentAddress();
        if(!TextUtils.isEmpty(s)) {
            QtumSharedPreference.getInstance().saveCurrentAddress(context, s);
        }
        return s;
    }
}