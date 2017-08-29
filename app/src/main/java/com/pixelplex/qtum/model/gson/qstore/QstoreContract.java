package com.pixelplex.qtum.model.gson.qstore;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kirillvolkov on 09.08.17.
 */

public class QstoreContract implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("tags")
    public String[] tags;

    @SerializedName("size")
    public Float sizeInBytes;

    @SerializedName("completed_on")
    public String completedOn;

    @SerializedName("with_source_code")
    public boolean withSourceCode;

    @SerializedName("publisher_address")
    public String publisherAddress;

    @SerializedName("type")
    public String type;

    @SerializedName("price")
    public Float price;

    @SerializedName("count_buy")
    public int countBuy;

    @SerializedName("count_downloads")
    public int countDownloads;

    @SerializedName("created_at")
    public String creationDate;
}
