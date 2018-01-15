package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.ExistContractResponse;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.Iterator;
import java.util.List;

import io.realm.annotations.PrimaryKey;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyContractsPresenterImpl extends BaseFragmentPresenterImpl implements MyContractsPresenter {

    private MyContractsView mMyContractsView;
    private MyContractsInteractor mMyContractsInteractor;

    public MyContractsPresenterImpl(MyContractsView myContractsView, MyContractsInteractor myContractsInteractor) {
        mMyContractsView = myContractsView;
        mMyContractsInteractor = myContractsInteractor;
    }

    ContractItemListener mContractItemListener = new ContractItemListener() {
        @Override
        public void onUnsubscribeClick(Contract contract) {
            List<Contract> contractListWithoutTokens = getInteractor().getContractsWithoutTokens();
            for (Iterator<Contract> contractIterator = contractListWithoutTokens.iterator(); contractIterator.hasNext(); ) {
                if (contract.getContractAddress().equals(contractIterator.next().getContractAddress())) {
                    contractIterator.remove();
                    getInteractor().setContractWithoutTokens(contractListWithoutTokens);
                    getView().updateRecyclerView(getInteractor().getContracts());
                    return;
                }
            }
            List<Token> tokens = getInteractor().getTokens();
            for (Iterator<Token> tokenIterator = tokens.iterator(); tokenIterator.hasNext(); ) {
                if (contract.getContractAddress().equals(tokenIterator.next().getContractAddress())) {
                    tokenIterator.remove();
                    getInteractor().setTokens(tokens);
                    getView().updateRecyclerView(getInteractor().getContracts());
                    return;
                }
            }
        }
    };

    @Override
    public void onUnsubscribeClick() {
        getView().updateRecyclerView(getInteractor().getContracts());
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        List<Contract> contractList = getInteractor().getContracts();
        if (contractList != null) {
            if (contractList.size() != 0) {
                getView().setUpRecyclerView(contractList, mContractItemListener);
                if (getInteractor().isShowWizard()) {
                    getView().showWizard();
                }
            } else {
                getView().setPlaceHolder();
            }
        } else {
            getView().setAlertDialog(R.string.error, R.string.fail_to_get_contracts, BaseFragment.PopUpType.error);
        }
    }

    @Override
    public void onContractClick(final Contract contract) {
        getView().setProgressDialog();
        getInteractor().isContractExist(contract.getContractAddress())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ExistContractResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ExistContractResponse existContractResponse) {
                        getView().dismissProgressDialog();
                        if(existContractResponse.isExist()){
                            getView().openContractFunctionFragment(contract);
                        }else{
                            getView().openDeletedContractFragment(contract.getContractAddress(), contract.getContractName());
                        }
                    }
                });
    }

    @Override
    public void onWizardClose() {
        getInteractor().setShowWizard(false);
    }

    @Override
    public void onRenameContract(Contract contract) {
        if(contract instanceof Token){
            List<Token> list = getInteractor().getTokens();
            for (Iterator<Token> tokenIterator = list.iterator(); tokenIterator.hasNext(); ) {
                if(tokenIterator.next().getContractAddress().equals(contract.getContractAddress())){
                    tokenIterator.remove();
                    list.add((Token)contract);
                    getInteractor().setTokens(list);
                    return;
                }
            }
        } else {
            List<Contract> contractListWithoutTokens = getInteractor().getContractsWithoutTokens();
            for (Iterator<Contract> contractIterator = contractListWithoutTokens.iterator(); contractIterator.hasNext(); ) {
                if (contract.getContractAddress().equals(contractIterator.next().getContractAddress())) {
                    contractIterator.remove();
                    contractListWithoutTokens.add(contract);
                    getInteractor().setContractWithoutTokens(contractListWithoutTokens);
                    return;
                }
            }
        }
    }

    @Override
    public MyContractsView getView() {
        return mMyContractsView;
    }

    public MyContractsInteractor getInteractor() {
        return mMyContractsInteractor;
    }
}
