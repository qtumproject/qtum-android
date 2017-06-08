package com.pixelplex.qtum.ui.fragment.SubscribeTokensFragment;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.ContractInfo;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by max-v on 6/8/2017.
 */

public class SubscribeTokensFragmentPresenter extends BaseFragmentPresenterImpl {

    private SubscribeTokensFragmentView mSubscribeTokensFragmentView;
    private SubscribeTokensFragmentInteractor mSubscribeTokensFragmentInteractor;

    SubscribeTokensFragmentPresenter(SubscribeTokensFragmentView subscribeTokensFragmentView){
        mSubscribeTokensFragmentView = subscribeTokensFragmentView;
        mSubscribeTokensFragmentInteractor = new SubscribeTokensFragmentInteractor(getView().getContext());
    }

    @Override
    public SubscribeTokensFragmentView getView() {
        return mSubscribeTokensFragmentView;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        List<String> contractTokenList = new ArrayList<>();
        for(ContractInfo contractInfo : getInteractor().getTokenList()){
            if(contractInfo.isToken()){
                contractTokenList.add(contractInfo.getContractName());
            }
        }
        getView().setTokenList(contractTokenList);
    }

    public SubscribeTokensFragmentInteractor getInteractor() {
        return mSubscribeTokensFragmentInteractor;
    }
}
