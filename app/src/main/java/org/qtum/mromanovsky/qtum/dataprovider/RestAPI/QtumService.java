package org.qtum.mromanovsky.qtum.dataprovider.RestAPI;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.BlockChainInfo;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.News;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionRequest;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;


public class QtumService {

    private static QtumService sQtumService;
    public static final String BASE_URL = "http://139.162.178.174/";
    QtumRestService mServiceApi;

    public static QtumService newInstance(){
        if(sQtumService == null){
            sQtumService = new QtumService();
        }
        return sQtumService;
    }

    private QtumService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mServiceApi = retrofit.create(QtumRestService.class);
    }

    public Observable<List<UnspentOutput>> getUnspentOutputs(final String address) {
        return Observable.create(new Observable.OnSubscribe<List<UnspentOutput>>() {
            @Override
            public void call(Subscriber<? super List<UnspentOutput>> subscriber) {
                Call<List<UnspentOutput>> request;
                request = mServiceApi.getOutputsUnspent(address);
                try {
                    Response<List<UnspentOutput>> response = request.execute();
                    if(response.errorBody() != null){
                        subscriber.onError(new Throwable(response.errorBody().toString()));
                    }else {
                        subscriber.onNext(response.body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Observable<List<UnspentOutput>> getUnspentOutputsForSeveralAddresses(final List<String> addresses) {
        return Observable.create(new Observable.OnSubscribe<List<UnspentOutput>>() {
            @Override
            public void call(Subscriber<? super List<UnspentOutput>> subscriber) {
                Call<List<UnspentOutput>> request;
                request = mServiceApi.getUnspentOutputsForSeveralAddresses(addresses);
                try {
                    Response<List<UnspentOutput>> response = request.execute();
                    if(response.errorBody() != null){
                        subscriber.onError(new Throwable(response.errorBody().toString()));
                    }else {
                        subscriber.onNext(response.body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Observable<List<History>> getHistoryListForSeveralAddresses(final List<String> addresses, final int limit, final int offset) {
        return Observable.create(new Observable.OnSubscribe<List<History>>() {
            @Override
            public void call(Subscriber<? super List<History>> subscriber) {
                Call<List<History>> request;
                request = mServiceApi.getHistoryListForSeveralAddresses(limit,offset,addresses);
                try {
                    Response<List<History>> response = request.execute();
                    if(response.errorBody() != null){
                        subscriber.onError(new Throwable(response.errorBody().toString()));
                    }else {
                        subscriber.onNext(response.body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Observable<List<History>> getHistoryList(final String address, final int limit, final int offset) {
        return Observable.create(new Observable.OnSubscribe<List<History>>() {
            @Override
            public void call(Subscriber<? super List<History>> subscriber) {
                Call<List<History>> request;
                request = mServiceApi.getHistoryList(address,limit,offset);
                try {
                    Response<List<History>> response = request.execute();
                    if(response.errorBody() != null){
                        subscriber.onError(new Throwable(response.errorBody().toString()));
                    }else {
                        subscriber.onNext(response.body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Observable<List<News>> getNews(final String lang) {
        return Observable.create(new Observable.OnSubscribe<List<News>>() {
            @Override
            public void call(Subscriber<? super List<News>> subscriber) {
                Call<List<News>> request;
                request = mServiceApi.getNews(lang);
                try {
                    Response<List<News>> response = request.execute();
                    subscriber.onNext(response.body());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Observable<BlockChainInfo> getBlockChainInfo() {
        return Observable.create(new Observable.OnSubscribe<BlockChainInfo>() {
            @Override
            public void call(Subscriber<? super BlockChainInfo> subscriber) {
                Call<BlockChainInfo> request;
                request = mServiceApi.getBlockChainInfo();
                try {
                    Response<BlockChainInfo> response = request.execute();
                    subscriber.onNext(response.body());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Observable<Void> sendRawTransaction(final SendRawTransactionRequest sendRawTransactionRequest) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                Call<Void> request;
                request = mServiceApi.sendRawTransaction(sendRawTransactionRequest);
                try {
                    Response<Void> response = request.execute();
                    if(response.errorBody() != null){
                        subscriber.onError(new Throwable(response.errorBody().toString()));
                    }else {
                        subscriber.onNext(response.body());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
