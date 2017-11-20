package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import org.qtum.wallet.model.AddressWithBalance;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddressListPresenterImpl extends BaseFragmentPresenterImpl implements AddressListPresenter {

    private AddressListView mAddressListFragmentView;
    private AddressListInteractor mAddressListInteractor;
    private List<AddressWithBalance> mAddressWithBalanceList = new ArrayList<>();
    private AddressWithBalance keyWithBalanceFrom;

    public AddressListPresenterImpl(AddressListView view, AddressListInteractor interactor) {
        mAddressListFragmentView = view;
        mAddressListInteractor = interactor;
    }

    @Override
    public AddressListView getView() {
        return mAddressListFragmentView;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().setProgressDialog();
        List<String> addresses = getInteractor().getAddresses();
        initAddressesWithBalanceList(addresses);
        getInteractor().getUnspentOutputs(addresses)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {
                        for (UnspentOutput unspentOutput : unspentOutputs) {
                            for (AddressWithBalance addressWithBalance : mAddressWithBalanceList) {
                                if (unspentOutput.getAddress().equals(addressWithBalance.getAddress())) {
                                    addressWithBalance.setUnspentOutput(unspentOutput);
                                    break;
                                }
                            }
                        }
                        getView().updateAddressList(mAddressWithBalanceList);
                        getView().dismissProgressDialog();
                    }
                });

    }

    private void initAddressesWithBalanceList(List<String> addresses) {
        for (String address : addresses) {
            mAddressWithBalanceList.add(new AddressWithBalance(address));
        }
    }

    public AddressListInteractor getInteractor() {
        return mAddressListInteractor;
    }

    @Override
    public AddressWithBalance getKeyWithBalanceFrom() {
        return keyWithBalanceFrom;
    }

    @Override
    public void setKeyWithBalanceFrom(AddressWithBalance keyWithBalanceFrom) {
        this.keyWithBalanceFrom = keyWithBalanceFrom;
    }

    @Override
    public List<AddressWithBalance> getAddressWithBalanceList() {
        return mAddressWithBalanceList;
    }
}
