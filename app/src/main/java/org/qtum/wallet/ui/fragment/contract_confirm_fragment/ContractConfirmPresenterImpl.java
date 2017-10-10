package org.qtum.wallet.ui.fragment.contract_confirm_fragment;

import org.bitcoinj.script.Script;
import org.qtum.wallet.R;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumNetworkState;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.utils.ContractBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ContractConfirmPresenterImpl extends BaseFragmentPresenterImpl implements ContractConfirmPresenter {

    private ContractConfirmView view;
    private ContractConfirmInteractor interactor;

    private String mContractTemplateUiid;

    private double minFee;
    private double maxFee = 0.2;

    private int minGasPrice;
    private int maxGasPrice = 120;

    private int minGasLimit = 100000;
    private int maxGasLimit = 5000000;

    private List<ContractMethodParameter> mContractMethodParameterList;

    @Override
    public void initializeViews() {
        super.initializeViews();
        minFee = getInteractor().getMinFee();
        getView().updateFee(minFee, maxFee);
        minGasPrice = getInteractor().getMinGasPrice();
        getView().updateGasPrice(minGasPrice, maxGasPrice);
        getView().updateGasLimit(minGasLimit, maxGasLimit);
    }

    @Override
    public void setContractMethodParameterList(List<ContractMethodParameter> contractMethodParameterList) {
        this.mContractMethodParameterList = contractMethodParameterList;
    }

    @Override
    public List<ContractMethodParameter> getContractMethodParameterList() {
        return mContractMethodParameterList;
    }

    public ContractConfirmPresenterImpl(ContractConfirmView view, ContractConfirmInteractor contractConfirmInteractor) {
        this.view = view;
        interactor = contractConfirmInteractor;
    }

    @Override
    public void onConfirmContract(final String uiid, final int gasLimit, final int gasPrice, final String fee) {
        getView().setProgressDialog();
        mContractTemplateUiid = uiid;
        getInteractor().createAbiConstructParams(mContractMethodParameterList, uiid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setAlertDialog(R.string.error, e.getMessage(), "Ok", BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(String s) {
                        createTx(s, gasLimit, gasPrice, fee);
                    }
                });
    }


    private void createTx(final String abiParams, final int gasLimit, final int gasPrice, final String fee) {
        getInteractor().getUnspentOutputsForSeveralAddresses(KeyStorage.getInstance().getAddresses())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setAlertDialog(R.string.error, e.getMessage(), "Ok", BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {

                        for (Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext(); ) {
                            UnspentOutput unspentOutput = iterator.next();
                            if (!unspentOutput.isOutputAvailableToPay()) {
                                iterator.remove();
                            }
                        }
                        Collections.sort(unspentOutputs, new Comparator<UnspentOutput>() {
                            @Override
                            public int compare(UnspentOutput unspentOutput, UnspentOutput t1) {
                                return unspentOutput.getAmount().doubleValue() < t1.getAmount().doubleValue() ? 1 : unspentOutput.getAmount().doubleValue() > t1.getAmount().doubleValue() ? -1 : 0;
                            }
                        });
                        ContractBuilder contractBuilder = new ContractBuilder();
                        Script script = contractBuilder.createConstructScript(abiParams, gasLimit, gasPrice);
//TODO
                        String hash = getInteractor().createTransactionHash(script, unspentOutputs, gasLimit, gasPrice, fee);
                        sendTx(hash, "Stub!");
                    }
                });
    }

    private void sendTx(final String code, final String senderAddress) {
        getInteractor().sendRawTransaction(new SendRawTransactionRequest(code, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendRawTransactionResponse>() {
                    @Override
                    public void onCompleted() {
                        getView().dismissProgressDialog();
                        getView().setAlertDialog(R.string.contract_created_successfully, "", "OK", BaseFragment.PopUpType.confirm, new BaseFragment.AlertDialogCallBack() {
                            @Override
                            public void onButtonClick() {
                                getView().closeFragments();
                            }

                            @Override
                            public void onButton2Click() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().dismissProgressDialog();
                        getView().setAlertDialog(R.string.error, e.getMessage(), "OK", BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(SendRawTransactionResponse sendRawTransactionResponse) {
                        getInteractor().saveContract(sendRawTransactionResponse.getTxid(),mContractTemplateUiid,getView().getContractName(),senderAddress);
                    }
                });
    }

    @Override
    public ContractConfirmView getView() {
        return view;
    }

    private ContractConfirmInteractor getInteractor() {
        return interactor;
    }
}
