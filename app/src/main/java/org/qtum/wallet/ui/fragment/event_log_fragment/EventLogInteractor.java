package org.qtum.wallet.ui.fragment.event_log_fragment;


import org.qtum.wallet.model.gson.history.History;

public interface EventLogInteractor {
    History getHistory(String txHash);
}
