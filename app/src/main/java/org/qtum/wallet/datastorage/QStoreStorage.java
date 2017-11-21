package org.qtum.wallet.datastorage;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.qtum.wallet.model.gson.qstore.PurchaseItem;
import org.qtum.wallet.model.gson.qstore.QstoreBuyResponse;

import java.util.ArrayList;
import java.util.List;

public class QStoreStorage {

    private static QStoreStorage instance;

    private final static String PURCHASE_LIST = "PURCHASE_LIST";

    private TinyDB tDb;

    private List<PurchaseItem> purchaseItems;

    public static QStoreStorage getInstance(Context context) {
        if (instance == null) {
            instance = new QStoreStorage(context);
        }
        return instance;
    }

    private QStoreStorage(Context context) {
        tDb = new TinyDB(context);
        purchaseItems = getPurchaseItems();
    }

    private List<PurchaseItem> getPurchaseItems() {
        Gson gson = new Gson();
        ArrayList<String> listString = tDb.getListString(PURCHASE_LIST);
        List<PurchaseItem> objects = new ArrayList<>();

        for (String jObjString : listString) {
            PurchaseItem value = gson.fromJson(jObjString, PurchaseItem.class);
            if (value != null) {
                objects.add(value);
            }
        }
        return objects;
    }

    public boolean isContractPurchased(String contractId) {

        if (TextUtils.isEmpty(contractId)) {
            throw new NullPointerException("Contract id is NULL");
        }

        for (PurchaseItem item : purchaseItems) {
            if (contractId.equals(item.getContractId())) {
                return true;
            }
        }

        return false;
    }

    public List<PurchaseItem> getNonPayedContracts() {

        List<PurchaseItem> nonPayedContracts = new ArrayList<>();

        for (PurchaseItem item : purchaseItems) {
            if (!item.payStatus.equals(PurchaseItem.PAID_STATUS)) {
                nonPayedContracts.add(item);
            }
        }

        return nonPayedContracts;
    }

    public PurchaseItem getPurchaseByContractId(String contractId) {

        if (TextUtils.isEmpty(contractId)) {
            return null;
        }

        for (PurchaseItem item : purchaseItems) {
            if (contractId.equals(item.getContractId())) {
                return item;
            }
        }

        return null;
    }

    public void addPurchasedItem(String contractId, QstoreBuyResponse buyResponse) {
        purchaseItems.add(new PurchaseItem(contractId, buyResponse));
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<>();
        for (PurchaseItem obj : purchaseItems) {
            objStrings.add(gson.toJson(obj));
        }
        tDb.putListString(PURCHASE_LIST, objStrings);
    }

    public void setPurchaseItemBuyStatus(String contractId, String buyStatus) {

        for (PurchaseItem item : purchaseItems) {
            if (item.getContractId().equals(contractId)) {
                item.payStatus = buyStatus;
            }
        }

        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<>();
        for (PurchaseItem obj : purchaseItems) {
            objStrings.add(gson.toJson(obj));
        }
        tDb.putListString(PURCHASE_LIST, objStrings);
    }
}
