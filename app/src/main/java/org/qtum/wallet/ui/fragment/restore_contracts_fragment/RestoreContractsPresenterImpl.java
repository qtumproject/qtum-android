package org.qtum.wallet.ui.fragment.restore_contracts_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.ContractCreationStatus;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.backup.Backup;
import org.qtum.wallet.model.backup.ContractJSON;
import org.qtum.wallet.model.backup.TemplateJSON;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.utils.DateCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

public class RestoreContractsPresenterImpl extends BaseFragmentPresenterImpl implements RestoreContractsPresenter {

    private RestoreContractsView mRestoreContractsView;
    private RestoreContractsInteractor mRestoreContractsInteractor;
    private boolean restoreTemplates, restoreContracts, restoreTokens;
    private Backup mBackup;

    public RestoreContractsPresenterImpl(RestoreContractsView view, RestoreContractsInteractor interactor) {
        mRestoreContractsView = view;
        mRestoreContractsInteractor = interactor;
    }

    @Override
    public RestoreContractsView getView() {
        return mRestoreContractsView;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().hideBottomNavView(false);

    }

    @Override
    public void onDeleteFileClick() {
        getView().deleteFile();
        if (mBackup != null) {
            mBackup = null;
        }
    }

    @Override
    public void onRestoreClick(final boolean restoreTemplates, final boolean restoreContracts, final boolean restoreTokens) {
        this.restoreTemplates = restoreTemplates;
        this.restoreContracts = restoreContracts;
        this.restoreTokens = restoreTokens;
        if (restoreTemplates || restoreContracts || restoreTokens) {
            if (getView().getRestoreFile() != null) {
                try {
                    mBackup = getInteractor().getBackupFromFile(getView().getRestoreFile());
                    int templatesCountInt = 0;
                    int contractsCountInt = 0;
                    int tokensCountInt = 0;

                    if (restoreTemplates) {
                        templatesCountInt = mBackup.getTemplates().size();
                    }
                    if (restoreContracts) {
                        for (ContractJSON contractJSON : mBackup.getContracts()) {
                            if (!contractJSON.getType().equals("token")) {
                                contractsCountInt++;
                            }
                        }
                    }
                    if (restoreTokens) {
                        for (ContractJSON contractJSON : mBackup.getContracts()) {
                            if (contractJSON.getType().equals("token")) {
                                tokensCountInt++;
                            }
                        }
                    }
                    String templatesCount = String.valueOf(templatesCountInt);
                    String contractsCount = String.valueOf(contractsCountInt);
                    String tokensCount = String.valueOf(tokensCountInt);

                    getView().showRestoreDialogFragment(mBackup.getDateCreate(), mBackup.getFileVersion(), templatesCount, contractsCount, tokensCount);
                } catch (Exception e) {
                    e.printStackTrace();
                    getView().setAlertDialog(R.string.something_went_wrong, "", "OK", BaseFragment.PopUpType.error, getView().getAlertCallback());
                }
            }
        }
    }

    @Override
    public Observable<Boolean> createBackupData() {
        return rx.Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                if (!validateBackup()) {
                    return false;
                }

                List<ContractTemplate> templates = getInteractor().getContractTemplates();

                if (!restoreContracts && !restoreTokens) {
                    for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                        getInteractor().importTemplate(templateJSON, templates);
                    }

                } else if (!restoreTemplates && !restoreTokens) {
                    List<Contract> contractList = new ArrayList<>();
                    for (ContractJSON contractJSON : mBackup.getContracts()) {
                        if (!contractJSON.getType().equals("token")) {
                            for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                    ContractTemplate contractTemplate = getInteractor().importTemplate(templateJSON, templates);
                                    //TODO change Created status
                                    Contract contract = new Contract(contractJSON.getContractAddress(), contractTemplate.getUuid(), ContractCreationStatus.Created, DateCalculator.getLongDate(contractJSON.getPublishDate()), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    contractList.add(contract);
                                }
                            }
                        }
                        getInteractor().putListWithoutToken(contractList);
                    }

                } else if (!restoreTemplates && !restoreContracts) {
                    List<Token> tokenList = new ArrayList<>();
                    for (ContractJSON contractJSON : mBackup.getContracts()) {
                        if (contractJSON.getType().equals("token")) {
                            for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                    ContractTemplate contractTemplate = getInteractor().importTemplate(templateJSON, templates);
                                    //TODO change Created status
                                    Token token = new Token(contractJSON.getContractAddress(), contractTemplate.getUuid(), ContractCreationStatus.Created, DateCalculator.getLongDate(contractJSON.getPublishDate()),
                                            contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    token.setSubscribe(contractJSON.getIsActive());
                                    getView().subscribeTokenBalanceChange(token.getContractAddress());
                                    tokenList.add(token);
                                }
                            }
                        }
                        getInteractor().putTokenList(tokenList);
                    }

                } else if (!restoreTemplates) {
                    List<Token> tokenList = new ArrayList<>();
                    List<Contract> contractList = new ArrayList<>();
                    for (ContractJSON contractJSON : mBackup.getContracts()) {
                        if (contractJSON.getType().equals("token")) {
                            for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                    ContractTemplate contractTemplate = getInteractor().importTemplate(templateJSON, templates);
                                    //TODO
                                    Token token = new Token(contractJSON.getContractAddress(), contractTemplate.getUuid(), ContractCreationStatus.Created, DateCalculator.getLongDate(contractJSON.getPublishDate()), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    getView().subscribeTokenBalanceChange(token.getContractAddress());
                                    token.setSubscribe(contractJSON.getIsActive());
                                    tokenList.add(token);
                                }
                            }
                        } else {
                            for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                    ContractTemplate contractTemplate = getInteractor().importTemplate(templateJSON, templates);
                                    //TODO
                                    Contract contract = new Contract(contractJSON.getContractAddress(), contractTemplate.getUuid(), ContractCreationStatus.Created, DateCalculator.getLongDate(contractJSON.getPublishDate()),
                                            contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    contractList.add(contract);
                                }
                            }
                        }
                    }

                    getInteractor().putTokenList(tokenList);
                    getInteractor().putListWithoutToken(contractList);

                } else if (restoreContracts && restoreTokens) {
                    List<Token> tokenList = new ArrayList<>();
                    List<Contract> contractList = new ArrayList<>();
                    for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                        ContractTemplate contractTemplate = getInteractor().importTemplate(templateJSON, templates);
                        for (ContractJSON contractJSON : mBackup.getContracts()) {
                            if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                if (!contractJSON.getType().equals("token")) {
                                    //TODO
                                    Contract contract = new Contract(contractJSON.getContractAddress(), contractTemplate.getUuid(), ContractCreationStatus.Created, DateCalculator.getLongDate(contractJSON.getPublishDate()), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    contractList.add(contract);
                                } else if (contractJSON.getType().equals("token")) {
                                    //TODO
                                    Token token = new Token(contractJSON.getContractAddress(), contractTemplate.getUuid(), ContractCreationStatus.Created, DateCalculator.getLongDate(contractJSON.getPublishDate()), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    token.setSubscribe(contractJSON.getIsActive());
                                    getView().subscribeTokenBalanceChange(token.getContractAddress());
                                    tokenList.add(token);
                                }
                            }
                        }
                    }

                    getInteractor().putTokenList(tokenList);
                    getInteractor().putListWithoutToken(contractList);

                } else if (restoreContracts) {
                    List<Contract> contractList = new ArrayList<>();
                    for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                        ContractTemplate contractTemplate = getInteractor().importTemplate(templateJSON, templates);
                        for (ContractJSON contractJSON : mBackup.getContracts()) {
                            if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                if (!contractJSON.getType().equals("token")) {
                                    //TODO
                                    Contract contract = new Contract(contractJSON.getContractAddress(), contractTemplate.getUuid(), ContractCreationStatus.Created, DateCalculator.getLongDate(contractJSON.getPublishDate()), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    contractList.add(contract);
                                }
                            }
                        }
                    }
                    getInteractor().putListWithoutToken(contractList);
                } else {
                    List<Token> tokenList = new ArrayList<>();
                    for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                        ContractTemplate contractTemplate = getInteractor().importTemplate(templateJSON, templates);
                        for (ContractJSON contractJSON : mBackup.getContracts()) {
                            if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                if (contractJSON.getType().equals("token")) {
                                    //TODO
                                    Token token = new Token(contractJSON.getContractAddress(), contractTemplate.getUuid(), ContractCreationStatus.Created, DateCalculator.getLongDate(contractJSON.getPublishDate()), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    token.setSubscribe(contractJSON.getIsActive());
                                    getView().subscribeTokenBalanceChange(token.getContractAddress());
                                    tokenList.add(token);
                                }
                            }
                        }
                    }
                    getInteractor().putTokenList(tokenList);
                }
                return true;
            }
        });
    }

    private boolean validateBackup() {
        if (mBackup == null)
            return false;
        if (mBackup.getTemplates() != null) {
            for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                if (!getInteractor().getTemplateValidity(templateJSON)) {
                    return false;
                }
            }
        }
        if (mBackup.getContracts() != null) {
            for (ContractJSON contractJSON : mBackup.getContracts()) {
                if (!getInteractor().getContractValidity(contractJSON)) {
                    return false;
                }
            }
        }
        if (mBackup.getContracts() != null && mBackup.getTemplates() != null) {
            List<ContractJSON> contractJSONList = new ArrayList<>();
            for (ContractJSON contractJSON : mBackup.getContracts()) {
                if (validateContractCreationAddress(contractJSON, mBackup.getTemplates())) {
                    contractJSONList.add(contractJSON);
                }
            }
            mBackup.setContracts(contractJSONList);
        }
        return true;
    }

    private boolean validateContractCreationAddress(ContractJSON contractJSON, List<TemplateJSON> templates) {
        return getInteractor().validateContractCreationAddress(contractJSON, templates);
    }

    interface RestoreDialogCallBack {
        void onRestoreClick();
    }

    public RestoreContractsInteractor getInteractor() {
        return mRestoreContractsInteractor;
    }

    public void setBackup(Backup mBackup) {
        this.mBackup = mBackup;
    }
}
