package org.qtum.wallet.ui.fragment.send_fragment;

import org.qtum.wallet.model.Currency;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;

public interface SendPresenter {
    void onResponse(String publicAddress, double amount, String tokenAddress);

    void onResponseError();

    void send(String from, String address, String amount, Currency currency, String fee);

    MainActivity.OnServiceConnectionChangeListener getServiceConnectionChangeListener();

    void updateNetworkSate(boolean networkConnectedFlag);
}
