package org.qtum.wallet.ui.fragment.token_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.utils.ContractManagementHelper;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.MathContext;

public class TokenInteractorImpl implements TokenInteractor {
    private WeakReference<Context> mContext;

    public TokenInteractorImpl(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override
    public String getCurrentAddress() {
        return KeyStorage.getInstance().getCurrentAddress();
    }

    @Override
    public String readAbiContract(String uiid) {
        return FileStorageManager.getInstance().readAbiContract(mContext.get(), uiid);
    }

    @Override
    public void setupPropertyTotalSupplyValue(Token token, ContractManagementHelper.GetPropertyValueCallBack totalSupplyValueCallback) {
        ContractManagementHelper.getPropertyValue(TokenFragment.totalSupply, token, mContext.get(), totalSupplyValueCallback);
    }

    @Override
    public void setupPropertyDecimalsValue(Token token, ContractManagementHelper.GetPropertyValueCallBack decimalsValueCallback) {
        ContractManagementHelper.getPropertyValue(TokenFragment.decimals, token, mContext.get(), decimalsValueCallback);
    }

    @Override
    public void setupPropertySymbolValue(Token token, ContractManagementHelper.GetPropertyValueCallBack symbolValueCallback) {
        ContractManagementHelper.getPropertyValue(TokenFragment.symbol, token, mContext.get(), symbolValueCallback);
    }

    @Override
    public void setupPropertyNameValue(Token token, ContractManagementHelper.GetPropertyValueCallBack nameValueCallback) {
        ContractManagementHelper.getPropertyValue(TokenFragment.name, token, mContext.get(), nameValueCallback);
    }

    @Override
    public Token setTokenDecimals(Token token, String value) {
        return new TinyDB(mContext.get()).setTokenDecimals(token, Integer.valueOf(value));
    }

    @Override
    public String handleTotalSupplyValue(Token token, String value) {
        BigDecimal bigDecimalTotalSupply = new BigDecimal(value);
        if (token.getDecimalUnits() != null) {
            BigDecimal divide = bigDecimalTotalSupply.divide(new BigDecimal(Math.pow(10, token.getDecimalUnits())), MathContext.DECIMAL128);
            value = divide.toPlainString();
        }
        return value;
    }
}
