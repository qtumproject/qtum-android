package org.qtum.wallet.ui.fragment.addresses_detail_fragment;

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

public abstract class AddressesDetailFragment extends Fragment implements AddressesDetailView {


    public static String POSITION = "position";
    private Unbinder mUnbinder;
    private AddressesDetailFragmentPresenter mTransactionFragmentPresenter;

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    public static Fragment newInstance(Context context, int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        Fragment fragment = Factory.instantiateDefaultFragment(context, AddressesDetailFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTransactionFragmentPresenter = new AddressesDetailFragmentPresenter(this);
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

    private AddressesDetailFragmentPresenter getPresenter() {
        return mTransactionFragmentPresenter;
    }
}
