package com.pixelplex.qtum.ui.fragment.subscribe_tokens_fragment.light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.subscribe_tokens_fragment.SubscribeTokensFragment;
import com.pixelplex.qtum.utils.SearchBarLight;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by kirillvolkov on 25.07.17.
 */

public class SubscribeTokensFragmentLight extends SubscribeTokensFragment {

    @BindView(R.id.search_bar)
    SearchBarLight searchBar;

    @Override
    protected int getLayout() {
        return R.layout.fragment_currency_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        searchBar.setListener(this);
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

        mTokenAdapter = new TokenAdapter(tokenList, R.layout.item_single_checkable_light);
        mCurrentList = tokenList;
        mRecyclerView.setAdapter(mTokenAdapter);
    }
}
