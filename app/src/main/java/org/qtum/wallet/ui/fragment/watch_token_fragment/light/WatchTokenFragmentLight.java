package org.qtum.wallet.ui.fragment.watch_token_fragment.light;

import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.fragment.watch_contract_fragment.OnTemplateClickListener;
import org.qtum.wallet.ui.fragment.watch_contract_fragment.TemplatesAdapter;
import org.qtum.wallet.ui.fragment.watch_contract_fragment.WatchContractFragment;
import org.qtum.wallet.ui.fragment.watch_token_fragment.WatchTokenFragment;
import org.qtum.wallet.utils.FontManager;

import java.util.List;

public class WatchTokenFragmentLight extends WatchTokenFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_watch_token_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mEditTextContractName.setTypeface(FontManager.getInstance().getFont(getResources().getString(org.qtum.wallet.R.string.proximaNovaSemibold)));
        mEditTextContractAddress.setTypeface(FontManager.getInstance().getFont(getResources().getString(org.qtum.wallet.R.string.proximaNovaSemibold)));
    }

}
