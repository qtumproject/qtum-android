package org.qtum.mromanovsky.qtum.ui.fragment.TransactionFragment.TransactionDetailFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.Vin;
import org.qtum.mromanovsky.qtum.dataprovider.RestAPI.gsonmodels.History.Vout;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TransactionDetailFragment extends Fragment implements TransactionDetailFragmentView{

    public static final int ACTION_FROM = 0;
    public static final int ACTION_TO = 1;
    public static String ACTION = "action";
    public static String POSITION = "position";
    private Unbinder mUnbinder;
    private TransactionDetailFragmentPresenter mTransactionFragmentPresenter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static TransactionDetailFragment newInstance(int action, int position) {

        Bundle args = new Bundle();
        args.putInt(ACTION,action);
        args.putInt(POSITION,position);
        TransactionDetailFragment fragment = new TransactionDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTransactionFragmentPresenter = new TransactionDetailFragmentPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_detail,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        getPresenter().onViewCreated(getArguments());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private TransactionDetailFragmentPresenter getPresenter(){
        return mTransactionFragmentPresenter;
    }

    @Override
    public void setUpViewPager(List<Vin> from, List<Vout> to) {
        
    }
}
