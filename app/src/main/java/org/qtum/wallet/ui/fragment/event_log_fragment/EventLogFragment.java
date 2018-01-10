package org.qtum.wallet.ui.fragment.event_log_fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.history.Log;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.addresses_detail_fragment.AddressesDetailFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class EventLogFragment extends BaseFragment implements EventLogView{

    public static String POSITION = "position";
    private EventLogPresenter mEventLogPresenter;
    protected EventLogAdapter mEventLogAdapter;

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
//        mTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int marginTop = getRelativeTop(mTextView)-mTextView.getLayoutParams().height;
//                ChooseDialogFragment chooseDialogFragment = ChooseDialogFragment.newInstance(marginTop);
//                chooseDialogFragment.show(getFragmentManager(), ChooseDialogFragment.class.getCanonicalName());
//            }
//        });
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

    class EventLogViewHolder extends RecyclerView.ViewHolder{

        @LayoutRes
        int mResId;

        @BindView(R.id.tv_address)
        TextView mTextViewAddress;

        @BindView(R.id.recycler_view_topics)
        RecyclerView mRecyclerViewTopics;

        @BindView(R.id.recycler_view_data)
        RecyclerView mRecyclerViewData;

        public EventLogViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public EventLogViewHolder(View view,  @LayoutRes int resId){
            super(view);
            mResId = resId;
        }

        public void bind(Log log){
            mTextViewAddress.setText(log.getAddress());

            mRecyclerViewTopics.setLayoutManager(new LinearLayoutManager(mRecyclerViewTopics.getContext()));
            EventLogDataAdapter eventLogDataAdapter = new EventLogDataAdapter(log.getTopics(),mResId);
            mRecyclerViewTopics.setAdapter(eventLogDataAdapter);

            List<String> data = new ArrayList<>();
            int dataCount = log.getData().length()/64;
            for(int i = 0; i<dataCount; i++){
                data.add(log.getData().substring(i,(i+1)*64));
            }
            mRecyclerViewData.setLayoutManager(new LinearLayoutManager(mRecyclerViewData.getContext()));
            EventLogDataAdapter eventLogDataAdapter1 = new EventLogDataAdapter(data,mResId);
            mRecyclerViewData.setAdapter(eventLogDataAdapter1);

        }

    }

    public class EventLogAdapter extends RecyclerView.Adapter<EventLogViewHolder>{

        List<Log> mLogs;
        @LayoutRes int mResIdLog;
        @LayoutRes int mResIdData;

        public EventLogAdapter(List<Log> logs, @LayoutRes int resIdLog, @LayoutRes int resIdData){
            mLogs = logs;
            mResIdLog = resIdLog;
            mResIdData = resIdData;
        }

        @Override
        public EventLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EventLogViewHolder(LayoutInflater.from(parent.getContext()).inflate(mResIdLog, parent, false),mResIdData);
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

    class EventLogDataViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_view_type)
        TextView mTextViewType;

        @BindView(R.id.tv_log_data)
        TextView mTextViewLogData;

        public EventLogDataViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String data){
            mTextViewLogData.setText(data);
        }

    }

    class EventLogDataAdapter extends RecyclerView.Adapter<EventLogDataViewHolder>{

        List<String> mData;
        @LayoutRes int mResId;

        EventLogDataAdapter(List<String> data, @LayoutRes int resId){
            mData = data;
            mResId = resId;
        }

        @Override
        public EventLogDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new EventLogDataViewHolder(LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false));
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

}
