package org.qtum.wallet.ui.fragment.transaction_fragment.transaction_detail_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class TransactionDetailFragment extends Fragment implements TransactionDetailFragmentView {

    public static final int ACTION_FROM = 0;
    public static final int ACTION_TO = 1;
    public static String ACTION = "action";
    public static String POSITION = "position";
    private Unbinder mUnbinder;
    private TransactionDetailFragmentPresenter mTransactionFragmentPresenter;

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    public static Fragment newInstance(Context context, int action, int position) {
        Bundle args = new Bundle();
        args.putInt(ACTION, action);
        args.putInt(POSITION, position);
        Fragment fragment = Factory.instantiateDefaultFragment(context, TransactionDetailFragment.class);
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
        View view = inflater.inflate(R.layout.fragment_transaction_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getPresenter().onViewCreated(getArguments());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    private TransactionDetailFragmentPresenter getPresenter() {
        return mTransactionFragmentPresenter;
    }
}
