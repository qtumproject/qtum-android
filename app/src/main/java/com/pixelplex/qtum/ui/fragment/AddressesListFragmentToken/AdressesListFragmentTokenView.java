package com.pixelplex.qtum.ui.fragment.AddressesListFragmentToken;

import android.os.Handler;

import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.model.DeterministicKeyWithTokenBalance;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;
import com.pixelplex.qtum.ui.fragment.OtherTokens.UpdateSocketInstance;

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
