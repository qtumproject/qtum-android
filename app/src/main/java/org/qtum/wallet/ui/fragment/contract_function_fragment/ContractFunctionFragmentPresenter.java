package org.qtum.wallet.ui.fragment.contract_function_fragment;

import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.gson.CallSmartContractRequest;
import org.qtum.wallet.model.gson.FeePerKb;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.utils.ContractBuilder;

import org.bitcoinj.script.Script;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.qtum.wallet.R.string.token;


class ContractFunctionFragmentPresenter extends BaseFragmentPresenterImpl {

    private ContractFunctionFragmentView mContractMethodFragmentView;

    FeePerKb mFeePerKb;

    private double minFee;
    private double maxFee = 0.2;

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
                break;
            }
        }
        getFeePerKbObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FeePerKb>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FeePerKb feePerKb) {
                        minFee = feePerKb.getFeePerKb().doubleValue();
                        getView().updateFee(minFee,maxFee);
                        mFeePerKb = feePerKb;
                    }
                });
    }

    public Observable<FeePerKb> getFeePerKbObservable(){
        if(mFeePerKb!=null){
            return Observable.just(mFeePerKb);
        } else {
            return QtumService.newInstance().getEstimateFeePerKb(2);
        }
    }

    void onCallClick(List<ContractMethodParameter> contractMethodParameterList, final String contractAddress, final String fee, String methodName){

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
                    public void onNext(final String s) {
                        QtumService.newInstance().callSmartContract(contractAddress,new CallSmartContractRequest(new String[]{s}))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<CallSmartContractResponse>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(final CallSmartContractResponse callSmartContractResponse) {
                                        if(!callSmartContractResponse.getItems().get(0).getExcepted().equals("None")){
                                            getView().setAlertDialog(getView().getContext().getString(org.qtum.wallet.R.string.error), callSmartContractResponse.getItems().get(0).getExcepted(), "Ok", BaseFragment.PopUpType.error);
                                            return;
                                        }
                                        getFeePerKbObservable()
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Subscriber<FeePerKb>() {
                                                               @Override
                                                               public void onCompleted() {

                                                               }

                                                               @Override
                                                               public void onError(Throwable e) {

                                                               }

                                                               @Override
                                                               public void onNext(FeePerKb feePerKb) {
                                                                   createTx(s, /*TODO callSmartContractResponse.getItems().get(0).getGasUsed()*/ 2000000, fee, feePerKb.getFeePerKb(),contractAddress);
                                                                   mFeePerKb = feePerKb;
                                                               }
                                                           });

                                    }
                                });
                    }
                });
    }


    private void createTx(final String abiParams, final int gasLimit, final String fee,final BigDecimal feePerKb ,final String contractAddress) {
        QtumService.newInstance().getUnspentOutputsForSeveralAddresses(KeyStorage.getInstance().getAddresses())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setAlertDialog(getView().getContext().getString(org.qtum.wallet.R.string.error),e.getMessage(),"Ok", BaseFragment.PopUpType.error);
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
                        Script script = contractBuilder.createMethodScript(abiParams, gasLimit,contractAddress);
                        //TODO
                        sendTx(contractBuilder.createTransactionHash(script,unspentOutputs,gasLimit,feePerKb,fee,getView().getContext()));
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
                        getView().setAlertDialog(getView().getContext().getResources().getString(org.qtum.wallet.R.string.error),e.getLocalizedMessage(),getView().getContext().getResources().getString(org.qtum.wallet.R.string.ok), BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(SendRawTransactionResponse sendRawTransactionResponse) {
                        getView().dismissProgressDialog();
                    }
                });

    }
}
