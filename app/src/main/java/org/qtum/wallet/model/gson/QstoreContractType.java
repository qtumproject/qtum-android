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
    private float cost = 0;
    private int icon;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public float getCost() {
        return cost;
    }

    public int getIcon() {
        return icon;
    }

    public void getIconByType(){
        switch (type){
            case "QRC20 Token":
                icon = R.drawable.ic_supertoken;
                break;
            case "Smart Contract":
                icon = R.drawable.ic_smart_contract;
                break;
            case "Crowdsale":
                icon = R.drawable.ic_crowdsale;
                break;
            default:
                break;
        }
    }
}
