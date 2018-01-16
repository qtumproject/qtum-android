package org.qtum.wallet.ui.fragment.event_log_fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.history.DisplayedData;
import org.qtum.wallet.model.gson.history.Log;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.addresses_detail_fragment.AddressesDetailFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class EventLogFragment extends BaseFragment implements EventLogView {

    public static String POSITION = "position";
    private EventLogPresenter mEventLogPresenter;
    protected EventLogAdapter mEventLogAdapter;

    DataTypeChangeListener mDataTypeChangeListener;

    public static Fragment newInstance(Context context, int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        Fragment fragment = Factory.instantiateDefaultFragment(context, EventLogFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.recycler_view_event_logs)
    protected
    RecyclerView mRecyclerViewLogs;
    @BindView(R.id.nested_scroll_view_container)
    NestedScrollView mNestedScrollView;

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mRecyclerViewLogs.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public int getPosition() {
        return getArguments().getInt(POSITION);
    }

    @Override
    protected void createPresenter() {
        mEventLogPresenter = new EventLogPresenterImpl(this, new EventLogInteractorImpl(getContext()));
    }

    @Override
    protected EventLogPresenter getPresenter() {
        return mEventLogPresenter;
    }

    class EventLogViewHolder extends RecyclerView.ViewHolder {

        @LayoutRes
        int mResId;

        @BindView(R.id.tv_address)
        TextView mTextViewAddress;

        @BindView(R.id.recycler_view_topics)
        RecyclerView mRecyclerViewTopics;

        @BindView(R.id.recycler_view_data)
        RecyclerView mRecyclerViewData;

        public EventLogViewHolder(View view, @LayoutRes int resId) {
            super(view);
            ButterKnife.bind(this, itemView);
            mResId = resId;
        }

        public void bind(Log log) {
            mTextViewAddress.setText(log.getAddress());

            mRecyclerViewTopics.setLayoutManager(new LinearLayoutManager(mRecyclerViewTopics.getContext()));
            EventLogDataAdapter eventLogTopicsAdapter = new EventLogDataAdapter(log.getDisplayedTopics(), mResId);
            mRecyclerViewTopics.setAdapter(eventLogTopicsAdapter);

            mRecyclerViewData.setLayoutManager(new LinearLayoutManager(mRecyclerViewData.getContext()));
            EventLogDataAdapter eventLogDataAdapter = new EventLogDataAdapter(log.getDisplayedData(), mResId);
            mRecyclerViewData.setAdapter(eventLogDataAdapter);

        }

    }

    public class EventLogAdapter extends RecyclerView.Adapter<EventLogViewHolder> {

        List<Log> mLogs;
        @LayoutRes
        int mResIdLog;
        @LayoutRes
        int mResIdData;

        public EventLogAdapter(List<Log> logs, @LayoutRes int resIdLog, @LayoutRes int resIdData) {
            mLogs = logs;
            mResIdLog = resIdLog;
            mResIdData = resIdData;
        }

        @Override
        public EventLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EventLogViewHolder(LayoutInflater.from(parent.getContext()).inflate(mResIdLog, parent, false), mResIdData);
        }

        @Override
        public void onBindViewHolder(EventLogViewHolder holder, int position) {
            holder.bind(mLogs.get(position));
        }

        @Override
        public int getItemCount() {
            return mLogs.size();
        }

    }

    class EventLogDataViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_view_type)
        TextView mTextViewType;

        @BindView(R.id.tv_log_data)
        TextView mTextViewLogData;

        @BindView(R.id.event_log_view_type)
        LinearLayout mEventLogViewType;

        DisplayedData mDisplayedData;

        public EventLogDataViewHolder(View itemView, final DataTypeChangeListener dataTypeChangeListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mEventLogViewType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int marginTop = getRelativeTop(view) - view.getHeight() - mNestedScrollView.getScrollY();
                    ChooseDialogFragment chooseDialogFragment = ChooseDialogFragment.newInstance(marginTop,mDisplayedData.getDataHex());
                    chooseDialogFragment.setTargetFragment(getFragment(),1234);
                    chooseDialogFragment.show(getFragmentManager(), ChooseDialogFragment.class.getCanonicalName());
                    mDataTypeChangeListener = new DataTypeChangeListener() {
                        @Override
                        public void onChange(DisplayedData newData, Integer position) {
                            dataTypeChangeListener.onChange(newData, getAdapterPosition());
                        }
                    };
                }
            });
        }

        public void bind(DisplayedData displayedData) {
            mDisplayedData = displayedData;
            mTextViewLogData.setText(displayedData.getData());
            mTextViewType.setText(displayedData.getDataType());
        }

    }

    class EventLogDataAdapter extends RecyclerView.Adapter<EventLogDataViewHolder> {

        List<DisplayedData> mData;
        @LayoutRes
        int mResId;

        EventLogDataAdapter(List<DisplayedData> data, @LayoutRes int resId) {
            mData = data;
            mResId = resId;

        }

        @Override
        public EventLogDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EventLogDataViewHolder(LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false), new DataTypeChangeListener() {
                @Override
                public void onChange(DisplayedData newData, Integer position) {
                    mData.set(position, newData);
                    notifyItemChanged(position);
                }
            });
        }

        @Override
        public void onBindViewHolder(EventLogDataViewHolder holder, int position) {
            holder.bind(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

    }

    interface DataTypeChangeListener{
        void onChange(DisplayedData newData, Integer position);
    }


    public void onViewTypeChoose(DisplayedData newData){
        mDataTypeChangeListener.onChange(newData, null);
    }
}
