package org.qtum.wallet.ui.fragment.token_fragment;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment.token_cash_management_fragment.AdressesListFragmentToken;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.receive_fragment.ReceiveFragment;
import org.qtum.wallet.utils.ContractManagementHelper;


public class TokenFragmentPresenter extends BaseFragmentPresenterImpl {

    private TokenFragmentView view;
    private Context mContext;

    private Token token;
    private String abi;

    public Token getToken() {
        return token;
    }

    public String getAbi() {
        abi = (TextUtils.isEmpty(abi)) ? FileStorageManager.getInstance().readAbiContract(getView().getContext(), token.getUiid()) : abi;
        return abi;
    }

    public void setToken(Token token) {
        this.token = token;
        getView().setBalance(this.token.getLastBalance().toPlainString());
        getView().setTokenAddress(this.token.getContractAddress());
        getView().setSenderAddress(this.token.getSenderAddress());
    }

    public TokenFragmentPresenter(TokenFragmentView view) {
        this.view = view;
        this.mContext = getView().getContext();
    }

    public void onChooseAddressClick() {
        if (!TextUtils.isEmpty(getView().getCurrency())) {
            BaseFragment addressListFragment = AdressesListFragmentToken.newInstance(mContext, token, getView().getCurrency());
            getView().openFragment(addressListFragment);
        }
    }

    @Override
    public TokenFragmentView getView() {
        return view;
    }

    public void onRefresh() {
        Toast.makeText(mContext, "Refreshing...", Toast.LENGTH_SHORT).show();
    }

    public void onReceiveClick() {
        BaseFragment receiveFragment = ReceiveFragment.newInstance(getView().getContext(), token.getContractAddress(), getView().getTokenBalance());
        getView().openFragment(receiveFragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        ContractManagementHelper.getPropertyValue(TokenFragment.totalSupply, token, mContext, new ContractManagementHelper.GetPropertyValueCallBack() {
            @Override
            public void onSuccess(String value) {
                getView().onContractPropertyUpdated(TokenFragment.totalSupply, value);
            }
        });

        if (token.getDecimalUnits() == null) {
            ContractManagementHelper.getPropertyValue(TokenFragment.decimals, token, mContext, new ContractManagementHelper.GetPropertyValueCallBack() {
                @Override
                public void onSuccess(String value) {
                    getView().onContractPropertyUpdated(TokenFragment.decimals, value);
                    if (value != null) {
                        token = new TinyDB(getView().getContext()).setTokenDecimals(token, Integer.valueOf(value));
                        getView().setBalance(token.getTokenBalanceWithDecimalUnits().toPlainString());
                    }
                }
            });
        } else {
            getView().onContractPropertyUpdated(TokenFragment.decimals, String.valueOf(token.getDecimalUnits()));
            getView().setBalance(token.getTokenBalanceWithDecimalUnits().toPlainString());
        }

        ContractManagementHelper.getPropertyValue(TokenFragment.symbol, token, mContext, new ContractManagementHelper.GetPropertyValueCallBack() {
            @Override
            public void onSuccess(String value) {
                getView().onContractPropertyUpdated(TokenFragment.symbol, value);
            }
        });
        ContractManagementHelper.getPropertyValue(TokenFragment.name, token, mContext, new ContractManagementHelper.GetPropertyValueCallBack() {
            @Override
            public void onSuccess(String value) {
                getView().onContractPropertyUpdated(TokenFragment.name, value);
            }
        });
    }

}