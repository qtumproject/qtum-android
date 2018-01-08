package org.qtum.wallet.ui.fragment.event_log_fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;

public abstract class EventLogFragment extends BaseFragment implements EventLogView{

    public static String POSITION = "position";
    private EventLogPresenter mEventLogPresenter;

    @BindView(R.id.tv_event_log)
    TextView mTextView;

    public static Fragment newInstance(Context context, int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        Fragment fragment = Factory.instantiateDefaultFragment(context, EventLogFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    private int getRelativeTop(View myView) {
        if (myView.getParent() == myView.getRootView())
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int marginTop = getRelativeTop(mTextView);
                ChooseDialogFragment chooseDialogFragment = ChooseDialogFragment.newInstance(marginTop);
                chooseDialogFragment.show(getFragmentManager(), ChooseDialogFragment.class.getCanonicalName());
            }
        });
    }

    @Override
    protected void createPresenter() {
        mEventLogPresenter = new EventLogPresenterImpl(this, new EventLogInteractorImpl(getContext()));
    }

    @Override
    protected EventLogPresenter getPresenter() {
        return mEventLogPresenter;
    }
}
