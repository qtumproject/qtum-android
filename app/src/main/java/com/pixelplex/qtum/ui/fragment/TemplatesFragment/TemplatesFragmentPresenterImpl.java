package com.pixelplex.qtum.ui.fragment.TemplatesFragment;

import android.content.Context;

import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.SetYourTokenFragment;
import com.pixelplex.qtum.utils.DateCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class TemplatesFragmentPresenterImpl extends BaseFragmentPresenterImpl implements TemplatesFragmentPresenter {

    private final TemplatesFragmentView view;
    private final Context mContext;
    private final TemplatesFragmentInteractorImpl interactor;

    TemplatesFragmentPresenterImpl(TemplatesFragmentView view) {
        this.view = view;
        mContext = getView().getContext();
        interactor = new TemplatesFragmentInteractorImpl();
    }

    @Override
    public TemplatesFragmentView getView() {
        return view;
    }

    public void openConstructorByName(long uiid) {
        SetYourTokenFragment fragment = SetYourTokenFragment.newInstance(uiid);
        getView().openFragment(fragment);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        TinyDB tinyDB = new TinyDB(mContext);
        List<ContractTemplate> contractTemplateList = tinyDB.getContractTemplateList();

        List<ContractTemplate> contractFullTemplateList = new ArrayList<>();
        for(ContractTemplate contractTemplate : contractTemplateList){
            if(contractTemplate.isFullContractTemplate()){
                contractFullTemplateList.add(contractTemplate);
            }
        }

        Collections.sort(contractFullTemplateList, new Comparator<ContractTemplate>() {
            @Override
            public int compare(ContractTemplate contractInfo, ContractTemplate t1) {
                return DateCalculator.equals(contractInfo.getDate(),t1.getDate());
            }
        });
        getView().setUpTemplateList(contractFullTemplateList);
    }
}
