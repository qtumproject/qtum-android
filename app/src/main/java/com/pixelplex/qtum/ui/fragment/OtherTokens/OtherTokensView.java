package com.pixelplex.qtum.ui.fragment.OtherTokens;

import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

import java.util.List;

/**
 * Created by kirillvolkov on 01.06.17.
 */

public interface OtherTokensView extends BaseFragmentView {
    void setTokensData(List<Contract> tokensData);
}
