package org.qtum.mromanovsky.qtum.dataprovider.RestAPI;

import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.OutputUnspent;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by max-v on 2/1/2017.
 */

public class QtumService {

    public static final String BASE_URL = "http://139.162.119.184/";
    QtumRestService serviceApi;

    public QtumService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceApi = retrofit.create(QtumRestService.class);

    }

    public Observable<List<OutputUnspent>> getUnspentOutputInfo(final String address) {
        return Observable.create(new Observable.OnSubscribe<List<OutputUnspent>>() {
            @Override
            public void call(Subscriber<? super List<OutputUnspent>> subscriber) {

                Call<List<OutputUnspent>> request;
                request = serviceApi.getOutputsUnspent("1HQSVAgFkMwwQ8xuhgQPQ8jFxKBk9kHWD5");
                try {
                    Response<List<OutputUnspent>> response = request.execute();
                    subscriber.onNext(response.body());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
