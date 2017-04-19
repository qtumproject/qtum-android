package org.qtum.mromanovsky.qtum.dataprovider.RestAPI;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.BlockChainInfo;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.ByteCode;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.TokenParams;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.ContractParamsRequest;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.History;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.HistoryResponse;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.News;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionRequest;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionResponse;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
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
//        return Observable.create(new Observable.OnSubscribe<List<UnspentOutput>>() {
//            @Override
//            public void call(Subscriber<? super List<UnspentOutput>> subscriber) {
//                Call<List<UnspentOutput>> request;
//                request = mServiceApi.getUnspentOutputsForSeveralAddresses(addresses);
//                try {
//                    Response<List<UnspentOutput>> response = request.execute();
//                    if(response.errorBody() != null){
//                        subscriber.onError(new Throwable(response.errorBody().toString()));
//                    }else {
//                        subscriber.onNext(response.body());
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
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

    public Observable<ByteCode> generateTokenBytecode(final ContractParamsRequest contractParamsRequest){
        return mServiceApi.generateTokenBytecode(contractParamsRequest);
    }

    public Observable<TokenParams> getContractsParams(String addressContact){
        return mServiceApi.getContractsParams(addressContact,"symbol,decimals,name,totalSupply");
    }
}