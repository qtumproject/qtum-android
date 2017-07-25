package com.pixelplex.qtum.ui.fragment.TemplateLibraryFragment;


import android.content.Context;

import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinFragment;
import com.pixelplex.qtum.utils.DateCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TemplateLibraryFragmentPresenter extends BaseFragmentPresenterImpl{

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
