package org.qtum.wallet.model.gson.history;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

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

    @Ignore
    private List<DisplayedData> topicDisplayedList;

    @Ignore
    private List<DisplayedData> dataDisplayedList;

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

    public List<DisplayedData> getDisplayedTopics(){
        if(topicDisplayedList==null) {
            topicDisplayedList = new ArrayList<>();
            for (String topic : getTopics()) {
                topicDisplayedList.add(new DisplayedData("Hex", topic, topic));
            }
        }
        return topicDisplayedList;
    }

    public List<DisplayedData> getDisplayedData(){
        if(dataDisplayedList==null) {
            dataDisplayedList = new ArrayList<>();
            int dataCount = getData().length() / 64;
            for (int i = 0; i < dataCount; i++) {
                String data = getData().substring(i, (i + 1) * 64);
                dataDisplayedList.add(new DisplayedData("Hex", data, data));
            }
        }
        return dataDisplayedList;
    }

}
