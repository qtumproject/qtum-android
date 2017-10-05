package org.qtum.wallet.ui.fragment.subscribe_tokens_fragment.dark;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment.subscribe_tokens_fragment.SubscribeTokensFragment;
import org.qtum.wallet.utils.SearchBar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class SubscribeTokensFragmentDark extends SubscribeTokensFragment {

    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_currency;
    }

    @Override
    public void setTokenList(List<Token> tokenList) {

        Collections.sort(tokenList, new Comparator<Token>() {
            @Override
            public int compare(Token token, Token t1) {
                if(token.isSubscribe() && !t1.isSubscribe()){
                    return -1;
                } else if(!token.isSubscribe() && t1.isSubscribe()){
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        mTokenAdapter = new TokenAdapter(tokenList, org.qtum.wallet.R.layout.item_single_checkable);
        mCurrentList = tokenList;
        mRecyclerView.setAdapter(mTokenAdapter);
    }

}
