package org.qtum.wallet.ui.fragment.restore_contracts_fragment;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.backup.Backup;
import org.qtum.wallet.model.backup.ContractJSON;
import org.qtum.wallet.model.backup.TemplateJSON;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;

import java.io.File;
import java.util.List;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public interface RestoreContractsInteractor {
    Backup getBackupFromFile(File restoreFile) throws Exception;

    List<ContractTemplate> getContractTemplates();


    ContractTemplate importTemplate(TemplateJSON templateJSON, List<ContractTemplate> templates);

    void putListWithoutToken(List<Contract> contractList);

    void putTokenList(List<Token> tokenList);

    boolean validateContractCreationAddress(ContractJSON contractJSON, List<TemplateJSON> templates);

    boolean getTemplateValidity(TemplateJSON templateJSON);

    boolean getContractValidity(ContractJSON contractJSON);

}
