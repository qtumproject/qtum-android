package org.qtum.wallet.datastorage;


import org.qtum.wallet.model.gson.DGPInfo;
import org.qtum.wallet.model.gson.FeePerKb;


public class QtumNetworkState {

    private static QtumNetworkState sQtumNetworkState;
    private FeePerKb mFeePerKb;
    private DGPInfo mDGPInfo;

    public static QtumNetworkState newInstance() {
        if(sQtumNetworkState==null)
            sQtumNetworkState = new QtumNetworkState();
        return sQtumNetworkState;
    }

    private QtumNetworkState(){

    }

    public FeePerKb getFeePerKb() {
        return mFeePerKb;
    }

    public void setFeePerKb(FeePerKb feePerKb) {
        mFeePerKb = feePerKb;
    }

    public DGPInfo getDGPInfo() {
        return mDGPInfo;
    }

    public void setDGPInfo(DGPInfo DGPInfo) {
        mDGPInfo = DGPInfo;
    }
}
