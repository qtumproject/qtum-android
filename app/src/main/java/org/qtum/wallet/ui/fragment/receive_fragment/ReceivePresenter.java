package org.qtum.wallet.ui.fragment.receive_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

import java.math.BigDecimal;

public interface ReceivePresenter extends BaseFragmentPresenter {
    void changeAmount(String s);

    void setTokenAddress(String address);

    void changeAddress();

    void setModuleWidth(int qrCodeWidth);

    void calcModuleWidth(boolean isDataPixel, int x, int y);

    boolean getWithCrossQr();

    int getTopOffsetHeight();

    int getModuleWidth();

    CharSequence getCurrentReceiveAddress();

    void onBalanceChanged(BigDecimal unconfirmedBalance, BigDecimal balance);
}
