package org.qtum.wallet.ui.fragment.watch_token_fragment;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.contract.Contract;

import java.util.List;

public interface WatchTokenInteractor {
    List<ContractTemplate> getContractTemplates();

    int compareDates(String date, String date1);

    String readAbiContract(String uuid);

    boolean isValidContractAddress(String address);

    List<Contract> getContracts();

    String handleContractWithToken(String name, String address, String ABIInterface);

    boolean isABIInterfaceValid(String abiInterface);

    String getQRC20TokenStandardAbi();
}
