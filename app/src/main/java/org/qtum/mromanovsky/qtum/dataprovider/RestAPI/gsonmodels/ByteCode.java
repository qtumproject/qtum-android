package org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ByteCode{

    @SerializedName("bytecode")
    @Expose
    private String bytecode;

    public String getBytecode() {
        return bytecode;
    }

    public void setBytecode(String bytecode) {
        this.bytecode = bytecode;
    }
}
