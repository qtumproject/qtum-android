package com.pixelplex.qtum.ui.fragment.qtum_cash_management_fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment_factory.Factory;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class AddressListFragment extends BaseFragment implements AddressListFragmentView, OnAddressClickListener {

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    protected AlertDialog mTransferDialog;
    protected boolean showTransferDialog = false;

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
    public void onPause() {
        super.onPause();
        if (mTransferDialog != null) {
            mTransferDialog.dismiss();
        }
    }

    @Override
    public void initializeViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getMainActivity().addAuthenticationListener(new MainActivity.AuthenticationListener() {
            @Override
            public void onAuthenticate() {
                if (showTransferDialog) {
                    mTransferDialog.show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivity().removeAuthenticationListener();
    }
}
