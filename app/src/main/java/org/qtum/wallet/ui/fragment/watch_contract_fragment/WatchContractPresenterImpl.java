package org.qtum.wallet.ui.fragment.watch_contract_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WatchContractPresenterImpl extends BaseFragmentPresenterImpl implements WatchContractPresenter {

    private WatchContractView mWatchContractView;
    private WatchContractInteractor mWatchContractInteractor;

    public WatchContractPresenterImpl(WatchContractView view, WatchContractInteractor interactor) {
        mWatchContractView = view;
        mWatchContractInteractor = interactor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        List<ContractTemplate> contractFullTemplateList = new ArrayList<>();
        List<ContractTemplate> contractTemplateList = getInteractor().getContractTemplates();
        if (getView().isToken()) {
            for (ContractTemplate contractTemplate : contractTemplateList) {
                if (contractTemplate.isFullContractTemplate() && contractTemplate.getContractType().equals("token")) {
                    contractFullTemplateList.add(contractTemplate);
                }
            }

            Collections.sort(contractFullTemplateList, new Comparator<ContractTemplate>() {
                @Override
                public int compare(ContractTemplate contractInfo, ContractTemplate t1) {
                    return getInteractor().compareDates(contractInfo.getDate(), t1.getDate());
                }
            });
        } else {
            for (ContractTemplate contractTemplate : contractTemplateList) {
                if (contractTemplate.isFullContractTemplate()) {
                    contractFullTemplateList.add(contractTemplate);
                }
            }

            Collections.sort(contractFullTemplateList, new Comparator<ContractTemplate>() {
                @Override
                public int compare(ContractTemplate contractInfo, ContractTemplate t1) {
                    return getInteractor().compareDates(contractInfo.getDate(), t1.getDate());
                }
            });
        }
        getView().setUpTemplatesList(contractFullTemplateList, getView().getTemplateClickListener());
    }

    @Override
    public WatchContractView getView() {
        return mWatchContractView;
    }

    @Override
    public void onOkClick(String name, String address, String ABIInterface, boolean isToken) {
        getView().setProgressDialog();

        if (!getInteractor().isValidContractAddress(address)) {
            getView().setAlertDialog(R.string.invalid_token_address, R.string.ok, BaseFragment.PopUpType.error);
            return;
        }

        List<Contract> allContractList = getInteractor().getContracts();
        for (Contract contract : allContractList) {
            if (contract.getContractAddress().equals(address)) {
                getView().setAlertDialog(R.string.token_with_same_address_already_exists, R.string.ok, BaseFragment.PopUpType.error);
                return;
            }
        }
        if (isToken) {
            if (getInteractor().isABIInterfaceValid(ABIInterface)) {
                String contractAddress = getInteractor().handleContractWithToken(name, address, ABIInterface);
                getView().subscribeTokenBalanceChanges(contractAddress);
            } else {
                getView().setAlertDialog(R.string.abi_doesnt_match_qrc20_standard, R.string.ok, BaseFragment.PopUpType.error);
                return;
            }
        } else {
            getInteractor().handleContractWithoutToken(name, address, ABIInterface);
        }
        getView().setAlertDialog(R.string.token_was_added_to_your_wallet, "", R.string.ok, BaseFragment.PopUpType.confirm, getView().getAlertCallback());
    }

    private WatchContractInteractor getInteractor() {
        return mWatchContractInteractor;
    }

    @Override
    public void onTemplateClick(ContractTemplate contractTemplate) {
        String abi = getInteractor().readAbiContract(contractTemplate.getUuid());
        getView().setABIInterface(contractTemplate.getName(), abi);
    }
}
