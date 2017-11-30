package org.qtum.wallet.ui.fragment.watch_token_fragment;

import android.support.annotation.MainThread;

import org.qtum.wallet.R;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.gson.ContractParams;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;

import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WatchTokenPresenterImpl extends BaseFragmentPresenterImpl implements WatchTokenPresenter {

    private WatchTokenView mWatchContractView;
    private WatchTokenInteractor mWatchContractInteractor;
    private String QRC20TokenStandardAbi;
    private Subscription mSubscription;

    public WatchTokenPresenterImpl(WatchTokenView view, WatchTokenInteractor interactor) {
        mWatchContractView = view;
        mWatchContractInteractor = interactor;
        QRC20TokenStandardAbi = getInteractor().getQRC20TokenStandardAbi();
    }

    @Override
    public WatchTokenView getView() {
        return mWatchContractView;
    }

    @Override
    public void onOkClick(String name, String address) {
        getView().setProgressDialog();
        if (!getInteractor().isValidContractAddress(address)) {
            getView().setAlertDialog(R.string.invalid_token_address, R.string.ok, BaseFragment.PopUpType.error);
            return;
        }
        List<Contract> allContractList = getInteractor().getContracts();
        for (Contract contract : allContractList) {
            if (contract.getContractAddress().equals(address)) {
                getView().setAlertDialog(R.string.token_with_same_address_already_exists, R.string.ok, BaseFragment.PopUpType.error);
                return;
            }
        }

        if (getInteractor().isABIInterfaceValid(QRC20TokenStandardAbi)) {
            String contractAddress = getInteractor().handleContractWithToken(name, address, QRC20TokenStandardAbi);
            getView().subscribeTokenBalanceChanges(contractAddress);
        } else {
            getView().setAlertDialog(R.string.abi_doesnt_match_qrc20_standard, R.string.ok, BaseFragment.PopUpType.error);
            return;
        }

        getView().setAlertDialog(R.string.token_was_added_to_your_wallet, "", R.string.ok, BaseFragment.PopUpType.confirm, getView().getAlertCallback());
    }

    private WatchTokenInteractor getInteractor() {
        return mWatchContractInteractor;
    }

    @Override
    public void onContractAddressEntered(String contractAddress) {
        mSubscription = getInteractor().getContractParams(contractAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ContractParams>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ContractParams contractParams) {
                        getView().setContractName(contractParams.getName());
                    }
                });
    }
}