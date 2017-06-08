package com.pixelplex.qtum.ui.fragment.SubscribeTokensFragment;

import android.content.Context;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.ContractInfo;
import com.pixelplex.qtum.utils.TinyDB;

import java.util.List;

/**
 * Created by max-v on 6/8/2017.
 */

public class SubscribeTokensFragmentInteractor {

    Context mContext;

    public SubscribeTokensFragmentInteractor(Context context){
        mContext = context;
    }


    public List<ContractInfo> getTokenList() {
        List<ContractInfo> contractInfoList = (new TinyDB(mContext)).getListContractInfo();
        return contractInfoList;
    }
}
