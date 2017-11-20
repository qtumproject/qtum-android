package org.qtum.wallet.ui.fragment.templates_fragment;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TemplatesPresenterImpl extends BaseFragmentPresenterImpl implements TemplatesPresenter {

    private final TemplatesView view;
    private final TemplatesInteractor interactor;

    public TemplatesPresenterImpl(TemplatesView view, TemplatesInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<ContractTemplate> contractTemplateList = getInteractor().getContractTemplates();

        List<ContractTemplate> contractFullTemplateList = new ArrayList<>();
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
        getView().setUpTemplateList(contractFullTemplateList);
    }

    @Override
    public TemplatesView getView() {
        return view;
    }

    public TemplatesInteractor getInteractor() {
        return interactor;
    }
}
