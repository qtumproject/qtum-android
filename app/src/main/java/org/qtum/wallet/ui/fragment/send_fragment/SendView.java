package org.qtum.wallet.ui.fragment.send_fragment;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.math.BigDecimal;

public interface SendView extends BaseFragmentView {
    void openInnerFragmentForResult(Fragment fragment);

    void qrCodeRecognitionToolBar();

    void sendToolBar();

    void updateData(String publicAddress, double amount);

    void errorRecognition();

    void updateBalance(String balance, String unconfirmedBalance);

    void setUpCurrencyField(Currency currency);

    void setUpCurrencyField(@StringRes int defaultCurrId);

    Fragment getFragment();

    void hideCurrencyField();

    UpdateService getSocketService();

    void updateFee(double minFee, double maxFee);

    void setAdressAndAmount(String address, String anount);

    void handleBalanceUpdating(String balanceString, BigDecimal unconfirmedBalance);

    void removePermissionResultListener();

    boolean isTokenEmpty(String tokenAddress);

    String getStringValue(@StringRes int resId);

    boolean isValidAmount();

    void showPinDialog();

    String getAddressInput();

    String getAmountInput();

    String getFeeInput();

    String getFromAddress();

    Currency getCurrency();

    SendInteractorImpl.SendTxCallBack getSendTransactionCallback();

    boolean isValidAvailableAddress(String availableAddress);

    TokenBalance getTokenBalance(String contractAddress);

    void updateGasPrice(int minGasPrice, int maxGasPrice);

    void updateGasLimit(int minGasLimit, int maxGasLimit);

    int getGasPriceInput();

    int getGasLimitInput();
}
