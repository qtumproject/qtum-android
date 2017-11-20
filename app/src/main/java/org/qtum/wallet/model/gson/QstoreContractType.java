package org.qtum.wallet.model.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.qtum.wallet.R;

public class QstoreContractType {
    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("count")
    @Expose
    private int count = 0;

    private int icon;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public int getIcon() {
        switch (type) {
            case "QRC20 Token":
                return icon = R.drawable.ic_supertoken;
            case "Smart Contract":
                return icon = R.drawable.ic_smart_contract;
            case "Crowdsale":
                return icon = R.drawable.ic_crowdsale;
            default:
                return icon = R.drawable.ic_smart_contract;
        }
    }
}
