package com.pixelplex.qtum.model.gson.store;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kirillvolkov on 09.08.17.
 */

public class QstoreItem {

    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("type")
    public String type;

    @SerializedName("price")
    public Float price;

    @SerializedName("count_buy")
    public int countBuy;

    @SerializedName("count_downloads")
    public int countDownloads;

    @SerializedName("created_at")
    public String createDate;

}
