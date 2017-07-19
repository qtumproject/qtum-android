package com.pixelplex.qtum.ui.fragment.ContractFunctionFragment;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.dataprovider.restAPI.QtumService;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.model.gson.SendRawTransactionRequest;
import com.pixelplex.qtum.model.gson.SendRawTransactionResponse;
import com.pixelplex.qtum.model.gson.UnspentOutput;
import com.pixelplex.qtum.datastorage.KeyStorage;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.ContractBuilder;

import org.bitcoinj.script.Script;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


class ContractFunctionFragmentPresenter extends BaseFragmentPresenterImpl {

    private ContractFunctionFragmentView mContractMethodFragmentView;

    ContractFunctionFragmentPresenter(ContractFunctionFragmentView contractMethodFragmentView){
        mContractMethodFragmentView = contractMethodFragmentView;
    }

    @Override
    public ContractFunctionFragmentView getView() {
        return mContractMethodFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<ContractMethod> list = FileStorageManager.getInstance().getContractMethods(getView().getContext(),getView().getContractTemplateUiid());
        for(ContractMethod contractMethod : list){
            if(contractMethod.name.equals(getView().getMethodName())){
                getView().setUpParameterList(contractMethod.inputParams);
                return;
            }
        }
    }

    void onCallClick(List<ContractMethodParameter> contractMethodParameterList, final String contractAddress, String methodName){

        getView().setProgressDialog();
        ContractBuilder contractBuilder = new ContractBuilder();
        contractBuilder.createAbiMethodParams(methodName,contractMethodParameterList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        createTx(s, contractAddress);
                    }
                });
    }


    private void createTx(final String abiParams, final String contractAddress) {
        QtumService.newInstance().getUnspentOutputsForSeveralAddresses(KeyStorage.getInstance().getAddresses())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setAlertDialog(getView().getContext().getString(R.string.error),e.getMessage(),"Ok", BaseFragment.PopUpType.error);
                    }
                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {

                        for(Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext();){
                            UnspentOutput unspentOutput = iterator.next();
                            if(unspentOutput.getConfirmations()==0 || !unspentOutput.isOutputAvailableToPay()){
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
                        Script script = contractBuilder.createMethodScript(abiParams, contractAddress);
                        sendTx(contractBuilder.createTransactionHash(script,unspentOutputs));
                    }
                });
    }

    private void sendTx(String code) {
        QtumService.newInstance().sendRawTransaction(new SendRawTransactionRequest(code, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendRawTransactionResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setAlertDialog(getView().getContext().getResources().getString(R.string.error),e.getLocalizedMessage(),getView().getContext().getResources().getString(R.string.ok), BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(SendRawTransactionResponse sendRawTransactionResponse) {
                        getView().dismissProgressDialog();
                    }
                });

    }
}
