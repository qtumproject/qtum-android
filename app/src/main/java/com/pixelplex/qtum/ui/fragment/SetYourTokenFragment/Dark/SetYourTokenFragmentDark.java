package com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.ConstructorAdapter;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.SetYourTokenFragment;
import com.pixelplex.qtum.utils.FontTextView;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class SetYourTokenFragmentDark extends SetYourTokenFragment {

    private final int LAYOUT = R.layout.fragment_set_your_token;

    @BindView(R.id.tv_template_name)
    FontTextView mTextViewTemplateName;

    @OnClick(R.id.cancel)
    public void onCancelClick() {
        getActivity().onBackPressed();
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        String templateName = "";
        TinyDB tinyDB = new TinyDB(getContext());
        List<ContractTemplate> contractTemplateList = tinyDB.getContractTemplateList();
        for(ContractTemplate contractTemplate : contractTemplateList){
            if(contractTemplate.getUuid().equals(templateUiid)) {
                templateName = contractTemplate.getName();
                break;
            }
        }

        mTextViewTemplateName.setText(templateName);
    }

    @Override
    public void onContractConstructorPrepared(List<ContractMethodParameter> params) {
        adapter = new ConstructorAdapterDark(params, this);
        constructorList.setAdapter(adapter);
    }
}
