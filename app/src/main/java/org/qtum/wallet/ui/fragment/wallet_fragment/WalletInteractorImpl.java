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

import javax.annotation.Nullable;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;

public class WalletInteractorImpl implements WalletInteractor {

    private Context context;
    private Realm realm;
    private RealmResults<History> mHistories;
    private HistoryInDbChangeListener<History> mHistoryInDbChangeListener;

    public WalletInteractorImpl(Context context, Realm realm) {
        this.context = context;
        this.realm = realm;
        mHistories = realm.where(History.class).findAll().sort("blockTime", Sort.DESCENDING);
        mHistories.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<History>>() {
            @Override
            public void onChange(RealmResults<History> histories, @Nullable OrderedCollectionChangeSet changeSet) {
                if(mHistoryInDbChangeListener!=null){
                    mHistoryInDbChangeListener.onHistoryChange(histories, changeSet);
                }
            }
        });
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

    @Override
    public void addHistoryInDbChangeListener(HistoryInDbChangeListener listener) {
        mHistoryInDbChangeListener = listener;
    }

    @Override
    public int getHistoryDbCount() {
        return mHistories.size();
    }

    @Override
    public List<History> getHistorySubList(int startIndex, int length) {
        return mHistories.subList(startIndex, length);
    }
}