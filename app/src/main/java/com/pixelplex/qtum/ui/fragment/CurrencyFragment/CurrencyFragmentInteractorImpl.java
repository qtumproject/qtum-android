package com.pixelplex.qtum.ui.fragment.CurrencyFragment;


import android.content.Context;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.datastorage.TinyDB;

import java.util.List;

class CurrencyFragmentInteractorImpl implements CurrencyFragmentInteractor{

    Context mContext;

    public CurrencyFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public List<Contract> getTokenList() {
        List<Contract> contractList = (new TinyDB(mContext)).getContractList();
        return contractList;
    }
}
