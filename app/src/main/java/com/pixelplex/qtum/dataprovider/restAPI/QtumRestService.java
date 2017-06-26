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

}