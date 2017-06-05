package com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class ContractMethod{

    @SerializedName("constant")
    public boolean constant;

    @SerializedName("inputs")
    public List<ContractMethodParameter> inputParams;

    @SerializedName("name")
    public String name;

    @SerializedName("outputs")
    public List<ContractMethodParameter> outputParams;

    @SerializedName("payable")
    public boolean payable;

    @SerializedName("type")
    public String type;

}
