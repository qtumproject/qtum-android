package org.qtum.wallet.ui.fragment.templates_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.set_your_token_fragment.SetYourTokenFragment;
import org.qtum.wallet.utils.DateCalculator;

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

    public void openConstructorByName(String uiid) {
        BaseFragment fragment = SetYourTokenFragment.newInstance(getView().getContext(), uiid);
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
