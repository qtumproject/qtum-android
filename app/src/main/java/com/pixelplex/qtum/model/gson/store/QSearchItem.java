package com.pixelplex.qtum.model.gson.store;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kirillvolkov on 09.08.17.
 */

public class QSearchItem extends QstoreItem {

    @SerializedName("tags")
    public List<String> tags;

}
