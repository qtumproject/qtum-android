package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import android.os.Handler;

import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.model.DeterministicKeyWithTokenBalance;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public interface AddressesListTokenView extends BaseFragmentView {
    void updateAddressList(List<DeterministicKeyWithTokenBalance> deterministicKeyWithBalance, String currency);

    UpdateService getSocketInstance();

    Handler getHandler();

    void hideTransferDialog();

    void goToSendFragment(String address, String address1, String amountString, String contractAddress);
}
