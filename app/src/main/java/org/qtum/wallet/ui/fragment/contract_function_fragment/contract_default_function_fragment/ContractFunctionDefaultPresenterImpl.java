package org.qtum.wallet.ui.fragment.contract_function_fragment.contract_default_function_fragment;

import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.call_smart_contract_response.Item;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ContractFunctionDefaultPresenterImpl extends BaseFragmentPresenterImpl implements ContractFunctionDefaultPresenter {

    private ContractFunctionDefaultView mContractMethodFragmentView;
    private ContractFunctionDefaultInteractor mContractFunctionInteractor;

    private double minFee;
    private double maxFee = 1;

    private int minGasPrice;
    private int maxGasPrice = 120;

    private int minGasLimit = 100000;
    private int maxGasLimit = 5000000;

    public ContractFunctionDefaultPresenterImpl(ContractFunctionDefaultView contractMethodFragmentView, ContractFunctionDefaultInteractor contractFunctionInteractor) {
        mContractMethodFragmentView = contractMethodFragmentView;
        mContractFunctionInteractor = contractFunctionInteractor;
    }

    @Override
    public ContractFunctionDefaultView getView() {
        return mContractMethodFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<ContractMethod> list = getInteractor().getContractMethod(getView().getContractTemplateUiid());
        for (ContractMethod contractMethod : list) {
            if (contractMethod.getName().equals(getView().getMethodName())) {
                if(contractMethod.isPayable()){
                    getView().showEtSendToContract();
                }else{
                    getView().hideEtSendToContract();
                }
                getView().setUpParameterList(contractMethod.getInputParams());
                break;
            }
        }
        minFee = getInteractor().getFeePerKb().doubleValue();
        getView().updateFee(minFee, maxFee);

        minGasPrice = getInteractor().getMinGasPrice();
        getView().updateGasPrice(minGasPrice, maxGasPrice);
        getView().updateGasLimit(minGasLimit, maxGasLimit);
    }

    @Override
    public void onCallClick(List<ContractMethodParameter> contractMethodParameterList, final String contractAddress, final String fee, final int gasLimit, final int gasPrice, String methodName, final String sendToAddress) {
        getView().setProgressDialog();
        for(ContractMethodParameter contract: contractMethodParameterList){
            if(contract.getValue().isEmpty()){
                getView().setAlertDialog("Invalid parameter", "Empty value","Cancel", BaseFragment.PopUpType.error);
                return;
            }
            if(contract.getType().equals("address")){
                if(contract.getValue().length()<24){
                    getView().setAlertDialog("Invalid parameter", "Minimum address length is 24", "Cancel",BaseFragment.PopUpType.error);
                    return;
                }
            }
        }
        final Contract contract = getInteractor().getContractByAddress(contractAddress);
        getInteractor().callSmartContractObservable(methodName, contractMethodParameterList, contract)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ContractFunctionDefaultInteractorImpl.CallSmartContractRespWrapper>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(final ContractFunctionDefaultInteractorImpl.CallSmartContractRespWrapper respWrapper) {
                        Item item = respWrapper.getResponse().getItems().get(0);
                        if (!item.getExcepted().equals("None")) {
                            getView().setAlertDialog(org.qtum.wallet.R.string.error,
                                    item.getExcepted(), "Ok",
                                    BaseFragment.PopUpType.error);
                            return;
                        }
                        if (item.getGasUsed() > gasLimit) {
                            getView().setAlertDialog(org.qtum.wallet.R.string.error,
                                    item.getExcepted(), "Ok",
                                    BaseFragment.PopUpType.error);
                            return;
                        }
                        createTx(respWrapper.getAbiParams(), gasLimit, gasPrice, fee.replace(',', '.'),
                                getInteractor().getFeePerKb(), contract, sendToAddress);
                    }
                });
    }


    private void createTx(final String abiParams, final int gasLimit, final int gasPrice, final String fee, final BigDecimal feePerKb, final Contract contract, final String sendToContract) {
        getInteractor().unspentOutputsForAddressObservable(contract.getSenderAddress())
                .flatMap(new Func1<List<UnspentOutput>, Observable<SendRawTransactionResponse>>() {
                    @Override
                    public Observable<SendRawTransactionResponse> call(List<UnspentOutput> unspentOutputs) {
                        for (Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext(); ) {
                            UnspentOutput unspentOutput = iterator.next();
                            if (unspentOutput.getConfirmations() == 0 || !unspentOutput.isOutputAvailableToPay()) {
                                iterator.remove();
                            }
                        }
                        Collections.sort(unspentOutputs, new Comparator<UnspentOutput>() {
                            @Override
                            public int compare(UnspentOutput unspentOutput, UnspentOutput t1) {
                                return unspentOutput.getAmount().doubleValue() > t1.getAmount().doubleValue() ? 1 : unspentOutput.getAmount().doubleValue() < t1.getAmount().doubleValue() ? -1 : 0;
                            }
                        });
                        return sendTx(getInteractor().createTransactionHash(abiParams, unspentOutputs, gasLimit, gasPrice, feePerKb, fee, contract.getContractAddress(),sendToContract));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendRawTransactionResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getView().setAlertDialog(org.qtum.wallet.R.string.error,
                                e.getLocalizedMessage(), "Ok",
                                BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(SendRawTransactionResponse sendRawTransactionResponse) {
                        getView().dismissProgressDialog();
                    }
                });
    }

    private Observable<SendRawTransactionResponse> sendTx(String code) {
        return getInteractor().sendRawTransactionObservable(code);
    }

    public ContractFunctionDefaultInteractor getInteractor() {
        return mContractFunctionInteractor;
    }
}
