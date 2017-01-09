package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;


public class TransactionFragment extends BaseFragment implements TransactionFragmentView {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tv_value) TextView mTextViewValue;
    @BindView(R.id.tv_received_time) TextView mTextViewReceivedTime;
    @BindView(R.id.tv_from) TextView mTextViewFrom;
    @BindView(R.id.tv_to) TextView mTextViewTo;

    public static final int LAYOUT = R.layout.fragment_transaction;
    final static String POSITION = "position";

    TransactionFragmentPresenterImpl mTransactionFragmentPresenter;

    public static TransactionFragment newInstance(int position){
        TransactionFragment transactionFragment = new TransactionFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION,position);
        transactionFragment.setArguments(args);
        return transactionFragment;
    }

    @Override
    protected void createPresenter() {
        mTransactionFragmentPresenter = new TransactionFragmentPresenterImpl(this);
    }

    @Override
    protected TransactionFragmentPresenterImpl getPresenter() {
        return mTransactionFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().openTransaction(getArguments().getInt(POSITION));
    }

    @Override
    public void initializeViews() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public void setUpTransactionData(Double value, String receivedTime, String from, String to) {
        mTextViewValue.setText(value.toString());
        mTextViewReceivedTime.setText(receivedTime);
        mTextViewFrom.setText(from);
        mTextViewTo.setText(to);
    }
}
