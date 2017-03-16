package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenFeaturesFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface SetTokenFeaturesFragmentView extends BaseFragmentView{
    void setData(boolean freezingOfAssets, boolean automaticSellingAndBuying, boolean autorefill, boolean proofOfWork);
}
