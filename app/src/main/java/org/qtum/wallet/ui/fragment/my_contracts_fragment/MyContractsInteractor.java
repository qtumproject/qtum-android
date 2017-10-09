package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import org.qtum.wallet.model.contract.Contract;

import java.util.List;

/**
 * Created by drevnitskaya on 09.10.17.
 */

public interface MyContractsInteractor {
    List<Contract> getContracts();

}
