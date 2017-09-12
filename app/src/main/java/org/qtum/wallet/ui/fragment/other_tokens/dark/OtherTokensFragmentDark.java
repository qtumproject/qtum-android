package org.qtum.wallet.ui.fragment.other_tokens.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment.other_tokens.OtherTokensFragment;

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

    @Override
    public void onTokenClick(int adapterPosition) {
        if (tokensList.getAdapter() != null) {
            presenter.openTokenDetails(((TokensAdapterDark) tokensList.getAdapter()).get(adapterPosition));
        }
    }
}
