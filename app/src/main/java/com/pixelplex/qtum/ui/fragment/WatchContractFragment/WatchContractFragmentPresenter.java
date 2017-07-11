package com.pixelplex.qtum.ui.fragment.WatchContractFragment;

import android.support.v4.app.FragmentManager;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.DateCalculator;

import java.util.List;


class WatchContractFragmentPresenter extends BaseFragmentPresenterImpl {

    private WatchContractFragmentView mWatchContractFragmentView;

    WatchContractFragmentPresenter(WatchContractFragmentView watchContractFragmentView){
        mWatchContractFragmentView = watchContractFragmentView;
    }

    @Override
    public WatchContractFragmentView getView() {
        return mWatchContractFragmentView;
    }

    void onOkClick(String name, String address, String jsonInterface, boolean isToken){
        getView().setProgressDialog();
        if(isToken){
            long uiid = FileStorageManager.getInstance().importTemplate(getView().getContext(),null,null,jsonInterface,"token","no_name",DateCalculator.getCurrentDate());
            TinyDB tinyDB = new TinyDB(getView().getContext());
            List<Token> tokenList = tinyDB.getTokenList();
            Token token = new Token(address, uiid, true, DateCalculator.getCurrentDate(), "Stub!", name);
            tokenList.add(token);
            tinyDB.putTokenList(tokenList);
        }else {
            long uiid = FileStorageManager.getInstance().importTemplate(getView().getContext(),null,null,jsonInterface,"none","no_name",DateCalculator.getCurrentDate());
            TinyDB tinyDB = new TinyDB(getView().getContext());
            List<Contract> contractList = tinyDB.getContractListWithoutToken();
            Contract contract = new Contract(address, uiid, true, DateCalculator.getCurrentDate(), "Stub!", name);
            contractList.add(contract);
            tinyDB.putContractListWithoutToken(contractList);
        }
        getView().setAlertDialog("Ok", "", "Ok", BaseFragment.PopUpType.confirm, new BaseFragment.AlertDialogCallBack() {
            @Override
            public void onOkClick() {
                FragmentManager fm = getView().getFragment().getFragmentManager();
                int count = fm.getBackStackEntryCount()-2;
                for(int i = 0; i < count; ++i) {
                    fm.popBackStack();
                }
            }
        });
    }
}
