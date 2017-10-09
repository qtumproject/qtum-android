package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.model.DeterministicKeyWithTokenBalance;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.token_balance.Balance;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.fragment.qtum_cash_management_fragment.AddressListPresenterImpl;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.bitcoinj.crypto.DeterministicKey;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kirillvolkov on 03.08.17.
 */

public class AddressesListTokenPresenterImpl extends BaseFragmentPresenterImpl implements Runnable, AddressesListTokenPresenter {

    private AddressesListTokenView view;
    private AddressesListTokenInteractor interactor;
    public List<DeterministicKeyWithTokenBalance> items = new ArrayList<>();
    private Token token;
    private String currency;
    private List<DeterministicKey> addrs;
    private DeterministicKeyWithTokenBalance keyWithTokenBalanceFrom;

    private TokenBalance tokenBalance;

    public AddressesListTokenPresenterImpl(AddressesListTokenView view, AddressesListTokenInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public AddressesListTokenView getView() {
        return view;
    }

    private List<DeterministicKey> getAddresses() {
        if (addrs == null) {
            addrs = getInteractor().getKeys(10);
        }
        return addrs;
    }

    @Override
    public void processTokenBalances(TokenBalance balance) {
        for (DeterministicKey item : getAddresses()) {
            DeterministicKeyWithTokenBalance deterministicKeyWithTokenBalance = new DeterministicKeyWithTokenBalance(item);
            items.add(deterministicKeyWithTokenBalance);
            processTokenBalace(deterministicKeyWithTokenBalance, balance);
        }
    }

    private void processTokenBalace(DeterministicKeyWithTokenBalance deterministicKeyWithTokenBalance, TokenBalance balance) {
        for (Balance bal : balance.getBalances()) {
            if (deterministicKeyWithTokenBalance.getAddress().equals(bal.getAddress())) {
                deterministicKeyWithTokenBalance.addBalance(bal.getBalance());
            }
        }
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String getCurrency() {
        return currency;
    }


    @Override
    public void run() {
        if (items != null && getInteractor().isCurrencyValid(currency)) {
            getView().updateAddressList(items, currency);
        }
    }

    @Override
    public void transfer(DeterministicKeyWithTokenBalance keyWithBalanceTo, final DeterministicKeyWithTokenBalance keyWithTokenBalanceFrom, String amountString, final AddressListPresenterImpl.TransferListener transferListener) {
        if (!getInteractor().isAmountValid(amountString)) {
            getView().setAlertDialog(R.string.error,
                    R.string.enter_valid_amount_value,
                    R.string.ok,
                    BaseFragment.PopUpType.error);
            return;
        }

        if (Float.valueOf(amountString) <= 0) {
            getView().setAlertDialog(R.string.error,
                    R.string.transaction_amount_cant_be_zero,
                    R.string.ok,
                    BaseFragment.PopUpType.error);
            return;
        }

        getView().hideTransferDialog();

        if (tokenBalance == null || tokenBalance.getBalanceForAddress(keyWithTokenBalanceFrom.getAddress()) == null
                || tokenBalance.getBalanceForAddress(keyWithTokenBalanceFrom.getAddress()).getBalance().floatValue() < Float.valueOf(amountString)) {
            getView().dismissProgressDialog();
            getView().setAlertDialog(R.string.error, R.string.you_have_insufficient_funds_for_this_transaction, "Ok", BaseFragment.PopUpType.error);
            return;
        }

        getView().goToSendFragment(keyWithTokenBalanceFrom.getAddress(), keyWithBalanceTo.getAddress(), amountString, token.getContractAddress());
    }

    public int getDecimalUnits() {
        return token.getDecimalUnits();
    }

    @Override
    public String getContractAddress() {
        return token.getContractAddress();
    }

    @Override
    public void setTokenBalance(TokenBalance tokenBalance) {
        this.tokenBalance = tokenBalance;
    }

    public AddressesListTokenInteractor getInteractor() {
        return interactor;
    }

    @Override
    public List<DeterministicKeyWithTokenBalance> getKeysWithTokenBalance() {
        return items;
    }

    @Override
    public void setKeyWithTokenBalanceFrom(DeterministicKeyWithTokenBalance keyWithTokenBalanceFrom) {
        this.keyWithTokenBalanceFrom = keyWithTokenBalanceFrom;
    }

    @Override
    public DeterministicKeyWithTokenBalance getKeyWithTokenBalanceFrom() {
        return keyWithTokenBalanceFrom;
    }
}
