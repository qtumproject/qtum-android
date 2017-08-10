package com.pixelplex.qtum.dataprovider.restAPI;


import com.pixelplex.qtum.model.gson.BlockChainInfo;

import com.pixelplex.qtum.model.gson.CallSmartContractRequest;
import com.pixelplex.qtum.model.gson.callSmartContractResponse.CallSmartContractResponse;
import com.pixelplex.qtum.model.gson.history.History;
import com.pixelplex.qtum.model.gson.history.HistoryResponse;
import com.pixelplex.qtum.model.gson.News;
import com.pixelplex.qtum.model.gson.SendRawTransactionRequest;
import com.pixelplex.qtum.model.gson.SendRawTransactionResponse;
import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.model.gson.store.IsPaidResponse;
import com.pixelplex.qtum.model.gson.store.QSearchItem;
import com.pixelplex.qtum.model.gson.store.QstoreBuyResponse;
import com.pixelplex.qtum.model.gson.store.QstoreContract;
import com.pixelplex.qtum.model.gson.store.QstoreItem;
import com.pixelplex.qtum.model.gson.store.QstoreSourceCodeResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


public class QtumService {

    private static QtumService sQtumService;
    private static final String BASE_URL = "http://163.172.68.103:5931/";
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
                .baseUrl(BASE_URL)
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

    public Observable<List<QSearchItem>> searchContracts(int offset, String tag){
        return mServiceApi.getSearchContracts(20/*must be changed*/, offset/*get all contracts*/, new String[]{tag});
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

    public Observable<IsPaidResponse> isPaidByRequestId(String contractId, String requestId){
        return mServiceApi.isPaidByRequestId(contractId, requestId);
    }

    public Observable<QstoreSourceCodeResponse> getSourceCode(String contractId, String requestId, String accessToken){
        HashMap<String, String> body = new HashMap<>();
        body.put("request_id",requestId);
        body.put("access_token",accessToken);
        return mServiceApi.getSourceCode(contractId, body);
    }

}