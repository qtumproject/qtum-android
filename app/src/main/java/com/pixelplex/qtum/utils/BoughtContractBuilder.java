package com.pixelplex.qtum.utils;

import android.content.Context;

import com.pixelplex.qtum.dataprovider.restAPI.QtumService;
import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.datastorage.QStoreStorage;
import com.pixelplex.qtum.model.gson.store.ContractPurchase;
import com.pixelplex.qtum.model.gson.store.QstoreByteCodeResponse;
import com.pixelplex.qtum.model.gson.store.QstoreContract;
import com.pixelplex.qtum.model.gson.store.QstoreSourceCodeResponse;

import rx.Subscriber;

/**
 * Created by max-v on 8/23/2017.
 */

public class BoughtContractBuilder {

    String sourceContract;
    String byteCodeContract;
    String abiContract;
    String type;
    String name;
    String dateString;
    String uuid;

    public void build(final Context context, ContractPurchase contractPurchase, final ContractBuilderListener listener){
        final QStoreStorage.PurchaseItem purchaseItem = QStoreStorage.getInstance(context).getPurchaseByContractId(contractPurchase.contractId);
        QtumService.newInstance()
                .getSourceCode(purchaseItem.contractId,purchaseItem.requestId,purchaseItem.accessToken)
                .subscribe(new Subscriber<QstoreSourceCodeResponse>() {
                    @Override
                    public void onCompleted() {
                        QtumService.newInstance()
                                .getByteCode(purchaseItem.contractId,purchaseItem.requestId,purchaseItem.accessToken)
                                .subscribe(new Subscriber<QstoreByteCodeResponse>() {
                                    @Override
                                    public void onCompleted() {
                                        QtumService.newInstance().getAbiByContractId(purchaseItem.contractId)
                                                .subscribe(new Subscriber<Object>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        QtumService.newInstance().getContractById(purchaseItem.contractId)
                                                                .subscribe(new Subscriber<QstoreContract>() {
                                                                    @Override
                                                                    public void onCompleted() {
                                                                        FileStorageManager.getInstance().importTemplate(context,sourceContract,byteCodeContract,abiContract,type,name,dateString,uuid);
                                                                        listener.onBuildSuccess();
                                                                    }

                                                                    @Override
                                                                    public void onError(Throwable e) {

                                                                    }

                                                                    @Override
                                                                    public void onNext(QstoreContract qstoreContract) {
                                                                        type = qstoreContract.type;
                                                                        name = qstoreContract.name;
                                                                        dateString = qstoreContract.creationDate;
                                                                        uuid = purchaseItem.contractId;
                                                                    }
                                                                });
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {

                                                    }

                                                    @Override
                                                    public void onNext(Object abi) {
                                                        abiContract = abi.toString();
                                                    }
                                                });
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(QstoreByteCodeResponse qstoreByteCodeResponse) {
                                        byteCodeContract = qstoreByteCodeResponse.bytecode;
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QstoreSourceCodeResponse qstoreSourceCodeResponse) {
                        sourceContract = qstoreSourceCodeResponse.sourceCode;
                    }
                });
    }

    public interface ContractBuilderListener{
        void onBuildSuccess();
    }

}
