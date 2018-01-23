package org.qtum.wallet.ui.fragment.receive_fragment;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import rx.Subscription;

public interface ReceiveView extends BaseFragmentView {
    void setQrCode(Bitmap bitmap);

    void showSpinner();

    void hideSpinner();

    void setUpAddress(String s);

    void updateBalance(String balance, String unconfirmedBalance, String symbol);


    void showToast();

    void openFragmentForResult(Fragment fragment);

    Bitmap getQrCode();

    Subscription imageEncodeObservable(final String param);

    boolean isAmountValid(String amount);

    boolean isTokenAddressValid(String addr);

    boolean isUnconfirmedBalanceValid(String unconfirmedBalance);
}
