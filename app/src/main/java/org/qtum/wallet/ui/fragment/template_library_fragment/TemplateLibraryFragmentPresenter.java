package org.qtum.wallet.ui.fragment.template_library_fragment;


import android.content.Context;

import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.utils.DateCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TemplateLibraryFragmentPresenter extends BaseFragmentPresenterImpl {

    TemplateLibraryFragmentView mTemplateLibraryFragmentView;
    Context mContext;

    TemplateLibraryFragmentPresenter(TemplateLibraryFragmentView templateLibraryFragmentView){
        mTemplateLibraryFragmentView = templateLibraryFragmentView;
        mContext = getView().getContext();
    }

    @Override
    public TemplateLibraryFragmentView getView() {
        return mTemplateLibraryFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<ContractTemplate> contractFullTemplateList = new ArrayList<>();
        TinyDB tinyDB = new TinyDB(mContext);
        List<ContractTemplate> contractTemplateList = tinyDB.getContractTemplateList();
        if(getView().isTokenLibrary()) {
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
        getView().setUpTemplateList(contractFullTemplateList);
    }
}
