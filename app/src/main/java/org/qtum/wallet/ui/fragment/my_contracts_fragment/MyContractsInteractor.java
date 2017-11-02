package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;

import java.util.List;


public interface MyContractsInteractor {
    List<Contract> getContracts();
    List<Contract> getContractsWithoutTokens();
    List<Token> getTokens();
    void setContractWithoutTokens(List<Contract> contracts);
    void setTokens(List<Token> tokens);
    boolean isShowWizard();
    void setShowWizard(boolean isShow);
}
