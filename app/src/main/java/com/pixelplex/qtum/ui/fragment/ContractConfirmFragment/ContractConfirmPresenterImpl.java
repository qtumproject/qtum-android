package com.pixelplex.qtum.ui.fragment.ContractConfirmFragment;

import android.content.Context;

import com.pixelplex.qtum.dataprovider.RestAPI.QtumService;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Token;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionRequest;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.SendRawTransactionResponse;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.UnspentOutput;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.ContractBuilder;
import com.pixelplex.qtum.datastorage.TinyDB;

import org.bitcoinj.script.Script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class ContractConfirmPresenterImpl extends BaseFragmentPresenterImpl implements ContractConfirmPresenter {

    ContractConfirmView view;
    ContractConfirmInteractorImpl interactor;
    Context mContext;


    private String mContractTemplateName = "";


    private List<ContractMethodParameter> mContractMethodParameterList;

    public void setContractMethodParameterList(List<ContractMethodParameter> contractMethodParameterList) {
        this.mContractMethodParameterList = contractMethodParameterList;
    }

    public List<ContractMethodParameter> getContractMethodParameterList() {
        return mContractMethodParameterList;
    }

    public ContractConfirmPresenterImpl(ContractConfirmView view) {
        this.view = view;
        mContext = getView().getContext();
        interactor = new ContractConfirmInteractorImpl();
    }


    public void confirmContract(final String contractTemplateName) {
        getView().setProgressDialog();
        mContractTemplateName = contractTemplateName;
        ContractBuilder contractBuilder = new ContractBuilder();
        contractBuilder.createAbiConstructParams(mContractMethodParameterList, contractTemplateName,mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().dismissProgressDialog();
                        getView().setAlertDialog("Error", e.getMessage(),"Ok", BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(String s) {
                        createTx(s);
                    }
                });
    }


    public void createTx(final String abiParams) {
        QtumService.newInstance().getUnspentOutputsForSeveralAddresses(KeyStorage.getInstance().getAddresses())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setAlertDialog("Error",e.getMessage(),"Ok", BaseFragment.PopUpType.error);
                    }
                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {

                        for(Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext();){
                            UnspentOutput unspentOutput = iterator.next();
                            if(unspentOutput.getConfirmations()==0){
                                iterator.remove();
                            }
                        }
                        Collections.sort(unspentOutputs, new Comparator<UnspentOutput>() {
                            @Override
                            public int compare(UnspentOutput unspentOutput, UnspentOutput t1) {
                                return unspentOutput.getAmount().doubleValue() > t1.getAmount().doubleValue() ? 1 : unspentOutput.getAmount().doubleValue() < t1.getAmount().doubleValue() ? -1 : 0;
                            }
                        });
                        ContractBuilder contractBuilder = new ContractBuilder();
                        Script script = contractBuilder.createConstructScript(abiParams);
                        sendTx(contractBuilder.createTransactionHash(script,unspentOutputs),"asdasd");
                    }
                });
    }

    public void sendTx(final String code, final String senderAddress) {
        QtumService.newInstance().sendRawTransaction(new SendRawTransactionRequest(code, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendRawTransactionResponse>() {
                    @Override
                    public void onCompleted() {
                        getView().dismissProgressDialog();
                        getView().setAlertDialog("Contract created successfully","OK", BaseFragment.PopUpType.confirm);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().dismissProgressDialog();
                        getView().setAlertDialog("Error",e.getMessage(),"OK", BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(SendRawTransactionResponse sendRawTransactionResponse) {
                        String s = sendRawTransactionResponse.getResult();
                        getView().getApplication().setContractAwait(true);
                        String name = "";
                        for(ContractMethodParameter contractMethodParameter : mContractMethodParameterList){
                            if(contractMethodParameter.getName().equals("_name")){
                                name = contractMethodParameter.getValue();
                            }
                        }
                        Token token = new Token(null, mContractTemplateName, false, null, senderAddress, name);

                        TinyDB tinyDB = new TinyDB(mContext);
                        List<Token> tokenList = tinyDB.getTokenList();
                        tokenList.add(token);
                        tinyDB.putTokenList(tokenList);
                    }
                });
    }

    @Override
    public ContractConfirmView getView() {
        return view;
    }
}
