package com.pixelplex.qtum.ui.fragment.SubscribeTokensFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

/**
 * Created by max-v on 6/8/2017.
 */

interface SubscribeTokensFragmentView extends BaseFragmentView {
    void setTokenList(List<String> tokenList);
}
