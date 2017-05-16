package com.pixelplex.qtum.ui.fragment.SetTokenFeaturesFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface SetTokenFeaturesFragmentView extends BaseFragmentView{
    void setData(boolean freezingOfAssets, boolean automaticSellingAndBuying, boolean autorefill, boolean proofOfWork);
}
