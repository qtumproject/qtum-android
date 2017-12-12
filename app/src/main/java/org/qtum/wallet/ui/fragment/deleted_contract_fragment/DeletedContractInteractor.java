package org.qtum.wallet.ui.fragment.deleted_contract_fragment;


import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;

import java.util.List;

public interface DeletedContractInteractor {

    List<Contract> getContractsWithoutTokens();

    List<Token> getTokens();

    void setContractWithoutTokens(List<Contract> contracts);

    void setTokens(List<Token> tokens);

}
