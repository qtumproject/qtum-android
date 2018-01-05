package org.qtum.wallet.model.gson.history;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Log extends RealmObject{

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("topics")
    @Expose
    private RealmList<String> topics = null;
    @SerializedName("data")
    @Expose
    private String data;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(RealmList<String> topics) {
        this.topics = topics;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
