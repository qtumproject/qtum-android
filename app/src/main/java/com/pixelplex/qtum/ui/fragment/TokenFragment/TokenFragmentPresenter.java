package com.pixelplex.qtum.ui.fragment.TokenFragment;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.dataprovider.restAPI.QtumService;
import com.pixelplex.qtum.model.gson.CallSmartContractRequest;
import com.pixelplex.qtum.model.gson.callSmartContractResponse.CallSmartContractResponse;
import com.pixelplex.qtum.model.contract.ContractMethod;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;
import com.pixelplex.qtum.utils.ContractManagementHelper;
import com.pixelplex.qtum.utils.sha3.sha.Keccak;
import com.pixelplex.qtum.utils.sha3.sha.Parameters;

import org.spongycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


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

    @Override
    public TokenFragmentView getView() {
        return view;
    }

    public void onRefresh() {
        Toast.makeText(mContext,"Refreshing...", Toast.LENGTH_SHORT).show();
    }



    public void onReceiveClick(){
        BaseFragment receiveFragment = ReceiveFragment.newInstance(getView().getContext(), token.getContractAddress());
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
        ContractManagementHelper.getPropertyValue(TokenFragment.decimals, token, mContext, new ContractManagementHelper.GetPropertyValueCallBack() {
            @Override
            public void onSuccess(String value) {
                getView().onContractPropertyUpdated(TokenFragment.decimals, value);
            }
        });
        ContractManagementHelper.getPropertyValue(TokenFragment.symbol, token, mContext, new ContractManagementHelper.GetPropertyValueCallBack() {
            @Override
            public void onSuccess(String value) {
                getView().onContractPropertyUpdated(TokenFragment.symbol, value);
            }
        });
    }
}

