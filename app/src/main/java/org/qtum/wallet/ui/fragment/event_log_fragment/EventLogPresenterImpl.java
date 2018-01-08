package org.qtum.wallet.ui.fragment.event_log_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class EventLogPresenterImpl extends BaseFragmentPresenterImpl implements EventLogPresenter{

    EventLogView mEventLogView;
    EventLogInteractor mEventLogInteractor;

    EventLogPresenterImpl(EventLogView view, EventLogInteractor interactor){
        mEventLogInteractor = interactor;
        mEventLogView = view;
    }

    @Override
    public EventLogView getView() {
        return mEventLogView;
    }
}
