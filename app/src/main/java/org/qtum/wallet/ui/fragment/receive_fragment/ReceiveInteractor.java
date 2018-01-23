package org.qtum.wallet.ui.fragment.receive_fragment;

import java.math.BigDecimal;

public interface ReceiveInteractor {
    String getCurrentReceiveAddress();

    String formatReceiveAddress(String addr);

    String formatAmount(String amount);

    String formatTokenAddress(String addr);

}
