package org.qtum.wallet.ui.fragment.contract_management_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.FileStorageManager;
import org.qtum.wallet.model.contract.ContractMethod;

import java.util.List;


public class ContractManagementInteractorImpl implements ContractManagementInteractor{

    Context mContext;

    ContractManagementInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public List<ContractMethod> getContractListByAbi(String abi) {
        return FileStorageManager.getInstance().getContractMethodsByAbiString(mContext, abi);
    }

    @Override
    public List<ContractMethod> getContractListByUiid(String uiid) {
        return  FileStorageManager.getInstance().getContractMethods(mContext, uiid);
    }
}
