package com.pixelplex.qtum.ui.fragment.WatchContractFragment;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Token;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.util.Date;
import java.util.List;

/**
 * Created by max-v on 6/13/2017.
 */

public class WatchContractFragmentPresenter extends BaseFragmentPresenterImpl {

    private WatchContractFragmentView mWatchContractFragmentView;

    WatchContractFragmentPresenter(WatchContractFragmentView watchContractFragmentView){
        mWatchContractFragmentView = watchContractFragmentView;
    }

    @Override
    public WatchContractFragmentView getView() {
        return mWatchContractFragmentView;
    }

    public void onOkClick(String name, String address, String jsonInterface, boolean isToken){
        getView().setProgressDialog();
        if(isToken){
            FileStorageManager.getInstance().writeAbiContract(getView().getContext(), address, jsonInterface);
            TinyDB tinyDB = new TinyDB(getView().getContext());
            List<Token> tokenList = tinyDB.getTokenList();
            long date = new Date().getTime() / 1000;
            Token token = new Token(address, address, true, date, "asdasd", name);
            tokenList.add(token);
            tinyDB.putTokenList(tokenList);
        }else {
            FileStorageManager.getInstance().writeAbiContract(getView().getContext(), address, jsonInterface);
            TinyDB tinyDB = new TinyDB(getView().getContext());
            List<Contract> contractList = tinyDB.getContractListWithoutToken();
            long date = new Date().getTime() / 1000;
            Contract contract = new Contract(address, address, true, date, "asdasd", name);
            contractList.add(contract);
            tinyDB.putContractListWithoutToken(contractList);
        }
        getView().setAlertDialog("Ok","Ok", BaseFragment.PopUpType.confirm);
    }
}
