package org.qtum.wallet.ui.fragment.subscribe_tokens_fragment.light;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment.subscribe_tokens_fragment.SubscribeTokensFragment;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SubscribeTokensFragmentLight extends SubscribeTokensFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_currency_light;
    }

    @Override
    public void setTokenList(List<Token> tokenList) {

        Collections.sort(tokenList, new Comparator<Token>() {
            @Override
            public int compare(Token token, Token t1) {
                if (token.isSubscribe() && !t1.isSubscribe()) {
                    return -1;
                } else if (!token.isSubscribe() && t1.isSubscribe()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        mTokenAdapter = new TokenAdapter(tokenList, R.layout.item_single_checkable_light);
        mCurrentList = tokenList;
        mRecyclerView.setAdapter(mTokenAdapter);
    }
}
