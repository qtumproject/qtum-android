package org.qtum.wallet.ui.fragment.overview_fragment;


import android.util.Pair;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface OverviewView extends BaseFragmentView{
    int getPosition();
    void setUpOverview(List<Pair<String,String>> overview);
}
