package org.qtum.wallet.model.gson.history;


public class DisplayedData {

    private String dataType;
    private String dataHex;
    private String data;

    public DisplayedData(String dataType, String dataHex, String data) {
        this.dataType = dataType;
        this.dataHex = dataHex;
        this.data = data;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataHex() {
        return dataHex;
    }

    public void setDataHex(String dataHex) {
        this.dataHex = dataHex;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
