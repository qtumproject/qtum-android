package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import org.qtum.wallet.model.DeterministicKeyWithBalance;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

import java.util.List;

/**
 * Created by drevnitskaya on 05.10.17.
 */

public interface AddressListPresenter extends BaseFragmentPresenter {

    DeterministicKeyWithBalance getKeyWithBalanceFrom();

    void setKeyWithBalanceFrom(DeterministicKeyWithBalance keyWithBalanceFrom);

    List<DeterministicKeyWithBalance> getKeyWithBalanceList();
}
