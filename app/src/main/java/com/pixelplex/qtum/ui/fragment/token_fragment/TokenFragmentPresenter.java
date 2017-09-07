package com.pixelplex.qtum.ui.fragment.token_fragment;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.token_cash_management_fragment.AdressesListFragmentToken;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.receive_fragment.ReceiveFragment;
import com.pixelplex.qtum.utils.ContractManagementHelper;

import java.util.List;


public class TokenFragmentPresenter extends BaseFragmentPresenterImpl {

    private TokenFragmentView view;
    private Context mContext;

    private Token token;
    private String abi;

    public Token getToken() {
        return token;
    }

    public String getAbi(){
        abi = (TextUtils.isEmpty(abi))? FileStorageManager.getInstance().readAbiContract(getView().getContext(),token.getUiid()) : abi;
        return abi;
    }

    public void setToken(Token token) {
        this.token = token;
        getView().setBalance(this.token.getLastBalance());
        getView().setTokenAddress(this.token.getContractAddress());
        getView().setSenderAddress(this.token.getSenderAddress());
    }

    public TokenFragmentPresenter(TokenFragmentView view){
        this.view = view;
        this.mContext = getView().getContext();
    }

    public void onChooseAddressClick() {
        if(!TextUtils.isEmpty(getView().getCurrency())) {
            BaseFragment addressListFragment = AdressesListFragmentToken.newInstance(mContext, token, getView().getCurrency());
            getView().openFragment(addressListFragment);
        }
    }

    @Override
    public TokenFragmentView getView() {
        return view;
    }

    public void onRefresh() {
        Toast.makeText(mContext,"Refreshing...", Toast.LENGTH_SHORT).show();
    }

    private void saveTokenDecimalUnits(int decimalUnits){
        TinyDB tinyDB = new TinyDB(getView().getContext());
        List<Token> tokenList = tinyDB.getTokenList();
        for (Token t : tokenList){
            if(token.getUiid().equals(t.getUiid())){
                t.setDecimalUnits(decimalUnits);
                this.token = t;
            }
        }
        tinyDB.putTokenList(tokenList);
    }


    public void onReceiveClick(){
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

        if(token.getDecimalUnits() == null) {
            ContractManagementHelper.getPropertyValue(TokenFragment.decimals, token, mContext, new ContractManagementHelper.GetPropertyValueCallBack() {
                @Override
                public void onSuccess(String value) {
                    getView().onContractPropertyUpdated(TokenFragment.decimals, value);
                    if(value != null) {
                        saveTokenDecimalUnits(Integer.valueOf(value));
                        getView().setBalance(token.getTokenBalanceWithDecimalUnits());
                    }
                }
            });
        } else {
            getView().onContractPropertyUpdated(TokenFragment.decimals, String.valueOf(token.getDecimalUnits()));
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