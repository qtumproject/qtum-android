package com.pixelplex.qtum.ui.fragment.OtherTokens.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.OtherTokens.OtherTokensFragment;
import com.pixelplex.qtum.ui.fragment.OtherTokens.TokensAdapter;

import java.util.List;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class OtherTokensFragmentDark extends OtherTokensFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_other_tokens;
    }

    @Override
    public void setTokensData(List<Token> tokensData) {
        tokensList.setAdapter(new TokensAdapterDark(tokensData,presenter, this));
    }
}
