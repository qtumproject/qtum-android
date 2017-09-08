package com.pixelplex.qtum.ui.fragment.watch_contract_fragment;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.services.update_service.UpdateService;
import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.template_library_fragment.TemplateLibraryFragment;
import com.pixelplex.qtum.utils.ContractBuilder;
import com.pixelplex.qtum.utils.DateCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class WatchContractFragmentPresenter extends BaseFragmentPresenterImpl {

    private WatchContractFragmentView mWatchContractFragmentView;
    Context mContext;
    UpdateService mUpdateService;

    WatchContractFragmentPresenter(WatchContractFragmentView watchContractFragmentView){
        mWatchContractFragmentView = watchContractFragmentView;
        mContext = getView().getContext();
    }

    @Override
    public WatchContractFragmentView getView() {
        return mWatchContractFragmentView;
    }

    void onOkClick(String name, String address, String ABIInterface, boolean isToken){
        getView().setProgressDialog();

        if(!validateContractAddress(address)){
            getView().setAlertDialog(mContext.getString(R.string.invalid_token_address),mContext.getString(R.string.ok), BaseFragment.PopUpType.error);
            return;
        }
        TinyDB tinyDB = new TinyDB(getView().getContext());
        List<Contract> allContractList = tinyDB.getContractList();
        for(Contract contract : allContractList){
            if(contract.getContractAddress().equals(address)){
                getView().setAlertDialog(mContext.getString(R.string.token_with_same_address_already_exists),mContext.getString(R.string.ok), BaseFragment.PopUpType.error);
                return;
            }
        }
        if(isToken){
            if(ContractBuilder.checkForValidityQRC20(ABIInterface)) {
                ContractTemplate contractTemplate = FileStorageManager.getInstance().importTemplate(getView().getContext(), null, null, ABIInterface, "token", "no_name", DateCalculator.getCurrentDate(), UUID.randomUUID().toString());
                List<Token> tokenList = tinyDB.getTokenList();
                Token token = new Token(address, contractTemplate.getUuid(), true, DateCalculator.getCurrentDate(), "Stub!", name);
                tokenList.add(token);
                mUpdateService.subscribeTokenBalanceChange(token.getContractAddress());
                tinyDB.putTokenList(tokenList);
            } else {
                getView().setAlertDialog(mContext.getString(R.string.abi_doesnt_match_qrc20_standard),mContext.getString(R.string.ok), BaseFragment.PopUpType.error);
                return;
            }
        }else {
            ContractTemplate contractTemplate = FileStorageManager.getInstance().importTemplate(getView().getContext(), null, null, ABIInterface, "contract", "no_name", DateCalculator.getCurrentDate(), UUID.randomUUID().toString());
            List<Contract> contractList = tinyDB.getContractListWithoutToken();
            Contract contract = new Contract(address, contractTemplate.getUuid(), true, DateCalculator.getCurrentDate(), "Stub!", name);
            contractList.add(contract);
            tinyDB.putContractListWithoutToken(contractList);
        }
        getView().setAlertDialog(mContext.getString(R.string.token_was_added_to_your_wallet),"", mContext.getString(R.string.ok), BaseFragment.PopUpType.confirm, new BaseFragment.AlertDialogCallBack() {
            @Override
            public void onOkClick() {
                FragmentManager fm = getView().getFragment().getFragmentManager();
                int count = fm.getBackStackEntryCount()-2;
                for(int i = 0; i < count; ++i) {
                    fm.popBackStack();
                }
            }
        });
    }

    public void onChooseFromLibraryClick(boolean isToken){
        BaseFragment templateLibraryFragment = TemplateLibraryFragment.newInstance(getView().getContext(),isToken);
        getView().openFragmentForResult(templateLibraryFragment);
    }

    private boolean validateContractAddress(String address){
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{40,}$");
        Matcher m = p.matcher(address);
        return m.matches();
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        mUpdateService = getView().getMainActivity().getUpdateService();
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        List<ContractTemplate> contractFullTemplateList = new ArrayList<>();
        TinyDB tinyDB = new TinyDB(mContext);
        List<ContractTemplate> contractTemplateList = tinyDB.getContractTemplateList();
        if(getView().isToken()) {
            for (ContractTemplate contractTemplate : contractTemplateList) {
                if (contractTemplate.isFullContractTemplate()&&contractTemplate.getContractType().equals("token")) {
                    contractFullTemplateList.add(contractTemplate);
                }
            }

            Collections.sort(contractFullTemplateList, new Comparator<ContractTemplate>() {
                @Override
                public int compare(ContractTemplate contractInfo, ContractTemplate t1) {
                    return DateCalculator.equals(contractInfo.getDate(), t1.getDate());
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
                    return DateCalculator.equals(contractInfo.getDate(), t1.getDate());
                }
            });
        }
        getView().setUpTemplatesList(contractFullTemplateList, new OnTemplateClickListener() {
            @Override
            public void updateSelection(int adapterPosition) {
                getView().notifyAdapter(adapterPosition);
            }

            @Override
            public void onTemplateClick(ContractTemplate contractTemplate) {
                String abi = FileStorageManager.getInstance().readAbiContract(mContext, contractTemplate.getUuid());
                getView().setABIInterface(contractTemplate.getName(), abi);
            }
        });
    }
}
