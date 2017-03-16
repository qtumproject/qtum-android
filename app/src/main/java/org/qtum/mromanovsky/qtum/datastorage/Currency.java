package org.qtum.mromanovsky.qtum.datastorage;


public class Currency {
    private String mName;

    public Currency(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
