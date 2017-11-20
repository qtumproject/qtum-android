package org.qtum.wallet.model;


public class Version {

    private String versionName;
    private int versionCode;

    public Version(String versionName, int versionCode){
        this.versionName = versionName;
        this.versionCode = versionCode;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Version) obj).versionCode == this.versionCode && ((Version) obj).versionName.equals(this.versionName);
    }
}