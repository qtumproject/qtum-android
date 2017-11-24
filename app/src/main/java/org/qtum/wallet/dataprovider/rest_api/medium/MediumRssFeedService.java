package org.qtum.wallet.dataprovider.rest_api.medium;

import org.qtum.wallet.model.news.RssFeed;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface MediumRssFeedService {
    @GET("/feed/{channel}")
    Observable<RssFeed> getRssFeed(@Path("channel") String chanel);
}
