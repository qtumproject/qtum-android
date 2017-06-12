package com.pixelplex.qtum.ui.fragment.SubscribeTokensFragment;

import android.content.Context;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.datastorage.TinyDB;

import java.util.List;

/**
 * Created by max-v on 6/8/2017.
 */

public class SubscribeTokensFragmentInteractor {

    Context mContext;

    public SubscribeTokensFragmentInteractor(Context context){
        mContext = context;
    }


    public List<Contract> getTokenList() {
        List<Contract> contractList = (new TinyDB(mContext)).getContractList();
        return contractList;
    }
}
