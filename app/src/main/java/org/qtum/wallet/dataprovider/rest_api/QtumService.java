package org.qtum.wallet.dataprovider.rest_api;


import org.qtum.wallet.model.gson.BlockChainInfo;

import org.qtum.wallet.model.gson.CallSmartContractRequest;
import org.qtum.wallet.model.gson.FeePerKb;
import org.qtum.wallet.model.gson.QstoreContractType;
import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.News;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.qstore.ContractPurchase;
import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.model.gson.qstore.QstoreBuyResponse;
import org.qtum.wallet.model.gson.qstore.QstoreByteCodeResponse;
import org.qtum.wallet.model.gson.qstore.QstoreContract;
import org.qtum.wallet.model.gson.qstore.QstoreItem;
import org.qtum.wallet.model.gson.qstore.QstoreSourceCodeResponse;
import org.qtum.wallet.utils.CurrentNetParams;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


public class QtumService {

    private static QtumService sQtumService;
    private QtumRestService mServiceApi;

    public static QtumService newInstance() {
        if (sQtumService == null) {
            sQtumService = new QtumService();
        }
        return sQtumService;
    }

    private QtumService() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CurrentNetParams.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        mServiceApi = retrofit.create(QtumRestService.class);
    }

    public Observable<List<UnspentOutput>> getUnspentOutputs(final String address) {
        return mServiceApi.getOutputsUnspent(address);
    }

    public Observable<List<UnspentOutput>> getUnspentOutputsForSeveralAddresses(final List<String> addresses) {
        return mServiceApi.getUnspentOutputsForSeveralAddresses(addresses);
    }

    public Observable<HistoryResponse> getHistoryListForSeveralAddresses(final List<String> addresses, final int limit, final int offset) {
        return mServiceApi.getHistoryListForSeveralAddresses(limit, offset, addresses);
    }

    public Observable<List<History>> getHistoryList(final String address, final int limit, final int offset) {
        return mServiceApi.getHistoryList(address, limit, offset);
    }

    public Observable<List<News>> getNews(final String lang) {
        return mServiceApi.getNews(lang);
    }

    public Observable<BlockChainInfo> getBlockChainInfo() {
        return mServiceApi.getBlockChainInfo();
    }

    public Observable<FeePerKb> getEstimateFeePerKb(int nBlock){
        return mServiceApi.getEstimateFeePerKb(nBlock);
    }

    public Observable<SendRawTransactionResponse> sendRawTransaction(final SendRawTransactionRequest sendRawTransactionRequest) {
        return mServiceApi.sendRawTransaction(sendRawTransactionRequest);
    }

    public Observable<CallSmartContractResponse> callSmartContract(String contractAddress, final CallSmartContractRequest callSmartContractRequest){
        return mServiceApi.callSmartContract(contractAddress,callSmartContractRequest);
    }

    public Observable<History> getTransaction(final String txHash) {
        return mServiceApi.getTransaction(txHash);
    }

    public Observable<List<QstoreItem>> getTrendingNow(){
        return mServiceApi.getTrendingNow();
    }

    public Observable<List<QstoreItem>> getWatsNew(){
        return mServiceApi.getWatsNew();
    }

    public Observable<List<QSearchItem>> searchContracts(int offset,String type, String data, boolean byTag){
        if(byTag) {
            return mServiceApi.getSearchContracts(20/*must be changed*/, offset/*get all contracts*/,type, new String[]{data});
        } else {
            return mServiceApi.getSearchContracts(20/*must be changed*/, offset/*get all contracts*/,type, data);
        }
    }

    public Observable<QstoreContract> getContractById(String id){
        return mServiceApi.getContract(id);
    }

    public Observable<Object> getAbiByContractId(String contractId){
        return mServiceApi.getAbiByContractId(contractId);
    }

    public Observable<QstoreBuyResponse> buyRequest(String contractId){
        return mServiceApi.buyRequest(contractId);
    }

    public Observable<ContractPurchase> isPaidByRequestId(String contractId, String requestId){
        return mServiceApi.isPaidByRequestId(contractId, requestId);
    }

    public Observable<QstoreSourceCodeResponse> getSourceCode(String contractId, String requestId, String accessToken){
        HashMap<String, String> body = new HashMap<>();
        body.put("request_id",requestId);
        body.put("access_token",accessToken);
        return mServiceApi.getSourceCode(contractId, body);
    }

    public Observable<QstoreByteCodeResponse> getByteCode(String contractId, String requestId, String accessToken){
        HashMap<String, String> body = new HashMap<>();
        body.put("request_id",requestId);
        body.put("access_token",accessToken);
        return mServiceApi.getByteCode(contractId, body);
    }

    public Observable<List<QstoreContractType>> getContractTypes(){
        return mServiceApi.getContractTypes();
    }

}