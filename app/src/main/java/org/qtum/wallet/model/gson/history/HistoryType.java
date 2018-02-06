package org.qtum.wallet.model.gson.history;


public enum HistoryType {

    Received("Received"), Sent("Sent"), Internal_Transaction("Internal Transaction");

    private String name;

    HistoryType(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
