package org.qtum.wallet.ui.fragment.overview_fragment;


import android.util.Pair;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface OverviewView extends BaseFragmentView{
    String getTxHash();
    void setUpOverview(List<CopyableOverviewItem> overview);
}
