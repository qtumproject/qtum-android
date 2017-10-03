package org.qtum.wallet.ui.fragment.send_fragment;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;

import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;
import org.qtum.wallet.ui.fragment.pin_fragment.PinDialogFragment;

import java.math.BigDecimal;


public interface SendView extends BaseFragmentView {
    void openInnerFragmentForResult(Fragment fragment);

    void qrCodeRecognitionToolBar();

    void sendToolBar();

    void updateData(String publicAddress, double amount, String tokenAddress);

    void errorRecognition();

    void updateBalance(String balance, String unconfirmedBalance);

    void setUpCurrencyField(Currency currency);

    void setUpCurrencyField(@StringRes int defaultCurrId);

    Fragment getFragment();

    void hideCurrencyField();

    UpdateService getSocketService();

    void updateFee(double minFee, double maxFee);

    void setAdressAndAmount(String address, String anount);

    UpdateService getUpdateService();

    void handleBalanceUpdating(String balanceString, BigDecimal unconfirmedBalance);

    void removePermissionResultListener();

    boolean isTokenEmpty(String tokenAddress);

    String getStringValue(@StringRes int resId);

    boolean isValidAmount(String amount);

    void showPinDialog(PinDialogFragment.PinCallBack callback);

}
