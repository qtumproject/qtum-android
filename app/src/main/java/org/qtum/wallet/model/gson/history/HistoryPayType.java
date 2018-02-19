package org.qtum.wallet.model.gson.history;


public enum HistoryPayType {

    Received("Received"), Sent("Sent"), Internal_Transaction("Internal Transaction");

    private String name;

    HistoryPayType(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
