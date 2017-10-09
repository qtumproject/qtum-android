package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


public interface MyContractsView extends BaseFragmentView {
    void updateRecyclerView(List<Contract> contractList);

    void setPlaceHolder();
}
