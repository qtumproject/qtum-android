package org.qtum.wallet.ui.fragment.event_log_fragment;

import org.qtum.wallet.model.gson.history.Log;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


public interface EventLogView extends BaseFragmentView {
    String getTxHash();
    void updateEventLog(List<Log> logs);
}
