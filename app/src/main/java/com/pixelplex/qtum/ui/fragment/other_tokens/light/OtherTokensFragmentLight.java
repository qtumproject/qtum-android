package com.pixelplex.qtum.ui.fragment.other_tokens.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.other_tokens.OtherTokensFragment;

import java.util.List;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class OtherTokensFragmentLight extends OtherTokensFragment {

    @Override
    protected int getLayout() {
        return R.layout.lyt_other_tokens_light;
    }

    @Override
    public void setTokensData(List<Token> tokensData) {
        tokensList.setAdapter(new TokensAdapterLight(tokensData,presenter, this));
    }

    @Override
    public void onTokenClick(int adapterPosition) {
        if (tokensList.getAdapter() != null) {
            presenter.openTokenDetails(((TokensAdapterLight) tokensList.getAdapter()).get(adapterPosition));
        }
    }
}
