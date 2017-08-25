package com.pixelplex.qtum.ui.fragment.watch_contract_fragment.dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.watch_contract_fragment.OnTemplateClickListener;
import com.pixelplex.qtum.ui.fragment.watch_contract_fragment.TemplatesAdapter;
import com.pixelplex.qtum.ui.fragment.watch_contract_fragment.WatchContractFragment;
import com.pixelplex.qtum.utils.FontManager;

import java.util.List;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class WatchContractFragmentDark extends WatchContractFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_watch_contract;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        mTilContractName.setTypeface(FontManager.getInstance().getFont(getResources().getString(R.string.simplonMonoRegular)));
        mTilContractAddress.setTypeface(FontManager.getInstance().getFont(getResources().getString(R.string.simplonMonoRegular)));

        mEditTextContractName.setTypeface(FontManager.getInstance().getFont(getResources().getString(R.string.simplonMonoMedium)));
        mEditTextContractAddress.setTypeface(FontManager.getInstance().getFont(getResources().getString(R.string.simplonMonoMedium)));
    }

    @Override
    public void setUpTemplatesList(List<ContractTemplate> contractTemplateList, OnTemplateClickListener listener) {
        mRecyclerViewTemplates.setAdapter(new TemplatesAdapter(contractTemplateList, listener, R.layout.item_template_chips, R.color.accent_red_color));

    }

}
