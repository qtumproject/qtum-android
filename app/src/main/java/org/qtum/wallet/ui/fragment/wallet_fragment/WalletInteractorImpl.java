package org.qtum.wallet.ui.fragment.wallet_fragment;

import android.content.Context;
import android.text.TextUtils;

import org.qtum.wallet.dataprovider.rest_api.qtum.QtumService;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history.TransactionReceipt;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;

public class WalletInteractorImpl implements WalletInteractor {

    private Context context;
    private Realm realm;

    public WalletInteractorImpl(Context context, Realm realm) {
        this.context = context;
        this.realm = realm;
    }

//    @Override
//    public List<History> getHistoryList() {
//        return HistoryList.getInstance().getHistoryList();
//    }

    @Override
    public Observable<HistoryResponse> getHistoryList(int limit, int offest) {
        return QtumService.newInstance().getHistoryListForSeveralAddresses(getAddresses(), limit, offest);
    }

    @Override
    public List<String> getAddresses() {
        return KeyStorage.getInstance().getAddresses();
    }

//    @Override
//    public int getTotalHistoryItem() {
//        return HistoryList.getInstance().getTotalItem();
//    }
//
//    @Override
//    public void addToHistoryList(History history) {
//        HistoryList.getInstance().getHistoryList().add(0, history);
//    }
//
//    @Override
//    public Integer setHistory(History history) {
//        for (History historyReplacing : getHistoryList()) {
//            if (historyReplacing.getTxHash().equals(history.getTxHash())) {
//                int position = getHistoryList().indexOf(historyReplacing);
//                getHistoryList().set(position, history);
//                return position;
//            }
//        }
//        getHistoryList().add(0, history);
//        return null;
//    }

    @Override
    public String getAddress() {
        String s = KeyStorage.getInstance().getCurrentAddress();
        if (!TextUtils.isEmpty(s)) {
            QtumSharedPreference.getInstance().saveCurrentAddress(context, s);
        }
        return s;
    }

    @Override
    public Observable<List<TransactionReceipt>> getTransactionReceipt(String txHash) {
        return QtumService.newInstance().getTransactionReceipt(txHash);
    }

    @Override
    public RealmResults<History> getHistoriesFromRealm() {
        return realm.where(History.class).findAll().sort("blockTime", Sort.DESCENDING);
    }

    @Override
    public void updateHistoryInRealm(final List<History> histories) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(histories);
            }
        });

    }

    @Override
    public void updateHistoryInRealm(final History histories) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(histories);
            }
        });

    }

    @Override
    public void updateReceiptInRealm(final TransactionReceipt transactionReceipt) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(transactionReceipt);
            }
        });

    }

    @Override
    public TransactionReceipt getReceiptByRxhHashFromRealm(String txHash) {
        return realm.where(TransactionReceipt.class).equalTo("transactionHash", txHash).findFirst();
    }

    @Override
    public void setUpHistoryReceipt(final String txHash, final boolean isContract) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                History history = realm.where(History.class).equalTo("txHash", txHash).findFirst();
                history.setReceiptUpdated(true);
                history.setContractType(isContract);
                realm.insertOrUpdate(history);
            }
        });


    }
}