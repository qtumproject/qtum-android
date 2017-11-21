package org.qtum.wallet.model.gson.qstore;

import com.google.gson.annotations.SerializedName;

import org.qtum.wallet.R;

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

    public int getIcon() {
        switch (type) {
            case "QRC20 Token":
                return R.drawable.ic_supertoken;
            case "Smart Contract":
                return R.drawable.ic_smart_contract;
            case "Crowdsale":
                return R.drawable.ic_crowdsale;
            default:
                return R.drawable.ic_smart_contract;
        }
    }
}
