package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment;


import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.Vin;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.Vout;

import java.util.List;

public interface TransactionDetailFragmentView {
    void setUpViewPager(List<Vin> from, List<Vout> to);
}
