package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import org.qtum.wallet.model.DeterministicKeyWithBalance;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class AddressListPresenterImpl extends BaseFragmentPresenterImpl implements AddressListPresenter {

    private AddressListView mAddressListFragmentView;
    private AddressListInteractor mAddressListInteractor;
    public List<DeterministicKeyWithBalance> mKeyWithBalanceList = new ArrayList<>();
    private int balanceCountToReceive;
    public DeterministicKeyWithBalance keyWithBalanceFrom;

    public AddressListPresenterImpl(AddressListView view, AddressListInteractor interactor) {
        mAddressListFragmentView = view;
        mAddressListInteractor = interactor;
    }

    @Override
    public AddressListView getView() {
        return mAddressListFragmentView;
    }

    private Action1<AddressListInteractorImpl.OutputWrapper> actionOnNext = new Action1<AddressListInteractorImpl.OutputWrapper>() {
        @Override
        public void call(AddressListInteractorImpl.OutputWrapper outputWrapper) {

            Collections.sort(outputWrapper.getUnspentOutputs(), new Comparator<UnspentOutput>() {
                @Override
                public int compare(UnspentOutput unspentOutput, UnspentOutput t1) {
                    return unspentOutput.getAmount().doubleValue() < t1.getAmount().doubleValue() ? 1 : unspentOutput.getAmount().doubleValue() > t1.getAmount().doubleValue() ? -1 : 0;
                }
            });
            outputWrapper.getDeterministicKeyWithBalance().setUnspentOutputList(outputWrapper.getUnspentOutputs());
            BigDecimal balance = new BigDecimal("0");
            BigDecimal amount;
            for (UnspentOutput unspentOutput : outputWrapper.getUnspentOutputs()) {
                amount = new BigDecimal(String.valueOf(unspentOutput.getAmount()));
                balance = balance.add(amount);
            }
            outputWrapper.getDeterministicKeyWithBalance().setBalance(balance);
            mKeyWithBalanceList.add(outputWrapper.getDeterministicKeyWithBalance());
            balanceCountToReceive--;
            if (balanceCountToReceive == 0) {
                Collections.sort(mKeyWithBalanceList, new Comparator<DeterministicKeyWithBalance>() {
                    @Override
                    public int compare(DeterministicKeyWithBalance deterministicKeyWithBalance, DeterministicKeyWithBalance t1) {
                        return deterministicKeyWithBalance.getAddress().hashCode() < t1.getAddress().hashCode() ? 1 : deterministicKeyWithBalance.getAddress().hashCode() > t1.getAddress().hashCode() ? -1 : 0;
                    }
                });
            }
        }
    };

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        balanceCountToReceive = getInteractor().getKeyList().size();
        getView().setProgressDialog();
        Observable.concatDelayError(getInteractor().unspentQutputsObservable(actionOnNext))
                .observeOn(AndroidSchedulers.mainThread(), true)
                .subscribe(new Subscriber<AddressListInteractorImpl.OutputWrapper>() {
                    @Override
                    public void onCompleted() {
                        getView().dismissProgressDialog();
                        getView().updateAddressList(mKeyWithBalanceList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getView().dismissProgressDialog();
                    }

                    @Override
                    public void onNext(AddressListInteractorImpl.OutputWrapper outputWrapper) {
                    }
                });
    }

    public AddressListInteractor getInteractor() {
        return mAddressListInteractor;
    }

    public interface TransferListener {
        void onSuccess();

        void onError(String errorText);
    }

    @Override
    public DeterministicKeyWithBalance getKeyWithBalanceFrom() {
        return keyWithBalanceFrom;
    }

    @Override
    public void setKeyWithBalanceFrom(DeterministicKeyWithBalance keyWithBalanceFrom) {
        this.keyWithBalanceFrom = keyWithBalanceFrom;
    }

    @Override
    public List<DeterministicKeyWithBalance> getKeyWithBalanceList() {
        return mKeyWithBalanceList;
    }
}
