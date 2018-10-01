package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.model.AddressWithTokenBalance;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.token_balance.Balance;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.bitcoinj.crypto.DeterministicKey;

import java.util.ArrayList;
import java.util.List;

public class AddressesListTokenPresenterImpl extends BaseFragmentPresenterImpl implements Runnable, AddressesListTokenPresenter {

    private AddressesListTokenView view;
    private AddressesListTokenInteractor interactor;
    public List<AddressWithTokenBalance> items = new ArrayList<>();
    private Token token;
    private String currency;
    private List<String> addrs;
    private AddressWithTokenBalance keyWithTokenBalanceFrom;

    private TokenBalance tokenBalance;

    public AddressesListTokenPresenterImpl(AddressesListTokenView view, AddressesListTokenInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public AddressesListTokenView getView() {
        return view;
    }

    private List<String> getAddresses() {
        if (addrs == null) {
            addrs = getInteractor().getAddresses();
        }
        return addrs;
    }

    @Override
    public void processTokenBalances(TokenBalance balance) {
        for (String item : getAddresses()) {
            AddressWithTokenBalance deterministicKeyWithTokenBalance = new AddressWithTokenBalance(item);
            items.add(deterministicKeyWithTokenBalance);
            processTokenBalance(deterministicKeyWithTokenBalance, balance);
        }
    }

    private void processTokenBalance(AddressWithTokenBalance deterministicKeyWithTokenBalance, TokenBalance balance) {
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
    public void transfer(AddressWithTokenBalance keyWithBalanceTo,
                         final AddressWithTokenBalance keyWithTokenBalanceFrom, String amountString) {
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

        if (tokenBalance == null || !getInteractor().isValidForAddress(tokenBalance, keyWithTokenBalanceFrom)
                || !getInteractor().isValidBalance(tokenBalance, keyWithTokenBalanceFrom, amountString, token.getDecimalUnits())) {
            getView().dismissProgressDialog();
            getView().setAlertDialog(R.string.error, R.string.you_have_insufficient_funds_for_this_transaction, "Ok", BaseFragment.PopUpType.error);
            return;
        }
        getView().goToSendFragment(keyWithTokenBalanceFrom, keyWithBalanceTo, amountString, token.getContractAddress());
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
    public List<AddressWithTokenBalance> getKeysWithTokenBalance() {
        return items;
    }

    @Override
    public void setKeyWithTokenBalanceFrom(AddressWithTokenBalance keyWithTokenBalanceFrom) {
        this.keyWithTokenBalanceFrom = keyWithTokenBalanceFrom;
    }

    @Override
    public AddressWithTokenBalance getKeyWithTokenBalanceFrom() {
        return keyWithTokenBalanceFrom;
    }

    public void setKeysWithTokenBalance(List<AddressWithTokenBalance> list) {
        items = list;
    }
}
