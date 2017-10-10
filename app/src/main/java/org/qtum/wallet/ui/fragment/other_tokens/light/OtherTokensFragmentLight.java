package org.qtum.wallet.ui.fragment.other_tokens.light;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.other_tokens.OtherTokensFragment;
import org.qtum.wallet.ui.fragment.token_fragment.TokenFragment;

import java.util.List;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class OtherTokensFragmentLight extends OtherTokensFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.lyt_other_tokens_light;
    }

    @Override
    public void setTokensData(List<Token> tokensData) {
        tokensList.setAdapter(new TokensAdapterLight(tokensData, this, this));
    }

    @Override
    public void onTokenClick(int adapterPosition) {
        if (tokensList.getAdapter() != null) {
            BaseFragment tokenFragment = TokenFragment.newInstance(getContext(), ((TokensAdapterLight) tokensList.getAdapter()).get(adapterPosition));
            openFragment(tokenFragment);
        }
    }
}
