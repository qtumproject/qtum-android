package com.pixelplex.qtum.dataprovider.RestAPI;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.BlockChainInfo;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.ByteCode;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.TokenParams;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.ContractParamsRequest;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.History.HistoryResponse;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.News;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionRequest;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionResponse;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;

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

    @POST("/contracts/generate-token-bytecode")
    Observable<ByteCode> generateTokenBytecode(@Body ContractParamsRequest contractParamsRequest);

    @GET("/outputs/unspent")
    Observable<List<UnspentOutput>> getUnspentOutputsForSeveralAddresses(@Query("addresses[]") List<String> addresses);

    @GET("/history/{limit}/{offset}")
    Observable<HistoryResponse> getHistoryListForSeveralAddresses(@Path("limit") int limit, @Path("offset") int offset, @Query("addresses[]") List<String> addresses);

    @GET("/contracts/{address_contract}/params")
    Observable<TokenParams> getContractsParams(@Path("address_contract") String addressContract, @Query("keys") String keys);
}