package com.pixelplex.qtum.ui.fragment.token_cash_management_fragment;

import android.os.Handler;

import com.pixelplex.qtum.dataprovider.services.update_service.UpdateService;
import com.pixelplex.qtum.model.DeterministicKeyWithTokenBalance;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public interface AdressesListFragmentTokenView extends BaseFragmentView {
    void updateAddressList(List<DeterministicKeyWithTokenBalance> deterministicKeyWithBalance, String currency);
    UpdateService getSocketInstance();
    Handler getHandler();
    void hideTransferDialog();
}
