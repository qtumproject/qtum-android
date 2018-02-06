package org.qtum.wallet.ui.fragment.overview_fragment;


import org.qtum.wallet.model.gson.history.History;

public interface OverviewIteractor {
    History getHistory(String txHash);
}
