package org.qtum.wallet.model.gson.qstore;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QSearchItem extends QstoreItem {
    @SerializedName("tags")
    public List<String> tags;
}
