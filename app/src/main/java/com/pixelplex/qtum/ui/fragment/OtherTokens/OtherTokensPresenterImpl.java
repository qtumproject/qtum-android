package com.pixelplex.qtum.ui.fragment.OtherTokens;

import android.content.Context;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.ContractInfo;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.TokenFragment.TokenFragment;
import com.pixelplex.qtum.utils.TinyDB;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kirillvolkov on 01.06.17.
 */

public class OtherTokensPresenterImpl extends BaseFragmentPresenterImpl implements OtherTokensPresenter, UpdateSocketInstance {

    private Context mContext;
    OtherTokensView view;
    OtherTokensInteractorImpl interactor;

    public OtherTokensPresenterImpl (OtherTokensView view) {
        this.view = view;
        mContext = getView().getContext();
        this.interactor = new OtherTokensInteractorImpl();
    }

    public void openTokenDetails(ContractInfo token) {
        TokenFragment tokenFragment = TokenFragment.newInstance(token);
        getView().openFragment(tokenFragment);
    }

    @Override
    public OtherTokensView getView() {
        return view;
    }

    private Observable<List<ContractInfo>> getTokens() {
        return Observable.fromCallable(new Callable<List<ContractInfo>>() {
            @Override
            public List<ContractInfo> call() throws Exception {
                TinyDB tinyDB = new TinyDB(mContext);
                List<ContractInfo> contracts = tinyDB.getListContractInfo();
                List<ContractInfo> tokens = new ArrayList<>();

                for (ContractInfo contract: contracts) {
                    if(contract.isToken()){
                        tokens.add(contract);
                    }
                }
                contracts.clear();
                return tokens;
            }
        });
    }

    public void setTokenList() {
        getTokens()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<ContractInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ContractInfo> tokens) {
                if(tokens != null && tokens.size() > 0) {
                    getView().setTokensData(tokens);
                }
            }
        });
    }

    @Override
    public UpdateService getSocketInstance() {
        return ((MainActivity) getView().getFragmentActivity()).getUpdateService();
    }
}
