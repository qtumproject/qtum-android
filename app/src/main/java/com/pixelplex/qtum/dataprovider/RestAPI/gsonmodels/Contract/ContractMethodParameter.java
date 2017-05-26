package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class ContractMethodParameter implements Serializable {

    @SerializedName("name")
    public String name;

    @SerializedName("type")
    public String type;

    public String value;

    public String displayName;

}
