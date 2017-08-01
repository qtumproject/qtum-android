package com.pixelplex.qtum.ui.fragment.AddressListFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class AddressListFragment extends BaseFragment implements AddressListFragmentView {

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    protected AddressesWithBalanceAdapter mAddressesWithBalanceAdapter;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    AddressListFragmentPresenter mAddressListFragmentPresenter;

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, AddressListFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mAddressListFragmentPresenter = new AddressListFragmentPresenter(this);
    }

    @Override
    protected AddressListFragmentPresenter getPresenter() {
        return mAddressListFragmentPresenter;
    }

    @Override
    public void initializeViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
