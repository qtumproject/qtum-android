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
import com.pixelplex.qtum.model.gson.store.ContractPurchase;
import com.pixelplex.qtum.model.gson.store.QSearchItem;
import com.pixelplex.qtum.model.gson.store.QstoreBuyResponse;
import com.pixelplex.qtum.model.gson.store.QstoreByteCodeResponse;
import com.pixelplex.qtum.model.gson.store.QstoreContract;
import com.pixelplex.qtum.model.gson.store.QstoreItem;
import com.pixelplex.qtum.model.gson.store.QstoreSourceCodeResponse;

import java.util.HashMap;
import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


interface QtumRestService {

    @GET("/outputs/unspent/{address}")
    Observable<List<UnspentOutput>> getOutputsUnspent(@Path("address") String address);

    @GET("/history/{address}/{limit}/{offset}")
    Observable<List<History>> getHistoryList(@Path("address") String address, @Path("limit") int limit, @Path("offset") int offset);

    @GET("/news/{lang}")
    Observable<List<News>> getNews(@Path("lang") String lang);

    @GET("/blockchain/info")
    Observable<BlockChainInfo> getBlockChainInfo();

    @POST("/send-raw-transaction")
    Observable<SendRawTransactionResponse> sendRawTransaction(@Body SendRawTransactionRequest sendRawTransactionRequest);

    @POST("/contracts/{addressContract}/call")
    Observable<CallSmartContractResponse> callSmartContract(@Path("addressContract") String addressContract, @Body CallSmartContractRequest callSmartContractRequest);

    @GET("/outputs/unspent")
    Observable<List<UnspentOutput>> getUnspentOutputsForSeveralAddresses(@Query("addresses[]") List<String> addresses);

    @GET("/history/{limit}/{offset}")
    Observable<HistoryResponse> getHistoryListForSeveralAddresses(@Path("limit") int limit, @Path("offset") int offset, @Query("addresses[]") List<String> addresses);

    @GET("/transactions/{tx_hash}")
    Observable<History> getTransaction(@Path("tx_hash") String txHash);

    @GET("/contracts/trending-now")
    Observable<List<QstoreItem>> getTrendingNow();

    @GET("/contracts/last-added")
    Observable<List<QstoreItem>> getWatsNew();

    @GET("/contracts/{count}/{offset}")
    Observable<List<QSearchItem>> getSearchContracts(@Path("count") int count, @Path("offset") int offset, /*@Query("type_name") String type,*/ @Query("tags[]") String[] tags);

     @GET("/contracts/{contract_id}")
    Observable<QstoreContract> getContract(@Path("contract_id") String contractId);

    @POST("/contracts/{contract_id}/source-code")
    Observable<QstoreSourceCodeResponse> getSourceCode(@Path("contract_id") String contractId, @Body HashMap<String, String> body);

    @POST("/contracts/{contract_id}/bytecode")
    Observable<QstoreByteCodeResponse> getByteCode(@Path("contract_id") String contractId, @Body HashMap<String, String> body);

    @GET("/contracts/{contract_id}/abi")
    Observable<Object> getAbiByContractId(@Path("contract_id") String contractId);

    @POST("/contracts/{contract_id}/buy-request")
    Observable<QstoreBuyResponse> buyRequest(@Path("contract_id") String contractId);

    @GET("/contracts/{contract_id}/is-paid/by-request-id")
    Observable<ContractPurchase> isPaidByRequestId(@Path("contract_id") String contractId, @Query("request_id") String requestId);


}