package org.qtum.wallet.ui.fragment.event_log_fragment;


import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class EventLogPresenterImpl extends BaseFragmentPresenterImpl implements EventLogPresenter{

    EventLogView mEventLogView;
    EventLogInteractor mEventLogInteractor;

    EventLogPresenterImpl(EventLogView view, EventLogInteractor interactor){
        mEventLogInteractor = interactor;
        mEventLogView = view;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        History history = getInteractor().getHistory(getView().getTxHash());
        getView().updateEventLog(history.getTransactionReceipt().getLog());
    }

    @Override
    public EventLogView getView() {
        return mEventLogView;
    }

    EventLogInteractor getInteractor(){
        return mEventLogInteractor;
    }
}
