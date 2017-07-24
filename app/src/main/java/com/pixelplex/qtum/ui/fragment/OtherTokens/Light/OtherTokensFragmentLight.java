package com.pixelplex.qtum.ui.fragment.OtherTokens.Light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.OtherTokens.Dark.TokensAdapterDark;
import com.pixelplex.qtum.ui.fragment.OtherTokens.OtherTokensFragment;

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
}
