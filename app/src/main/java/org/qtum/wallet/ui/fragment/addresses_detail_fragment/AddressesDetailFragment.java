package org.qtum.wallet.ui.fragment.addresses_detail_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;

public abstract class AddressesDetailFragment extends BaseFragment implements AddressesDetailView {


    public static String TX_HASH = "tx_hash";
    private AddressesDetailPresenter mAddressesDetailPresenter;

    protected AddressesDetailAdapter mAddressesDetailAdapterFrom;
    protected AddressesDetailAdapter mAddressesDetailAdapterTo;

    @BindView(R.id.recycler_view_from)
    protected
    RecyclerView mRecyclerViewFrom;
    @BindView(R.id.recycler_view_to)
    protected
    RecyclerView mRecyclerViewTo;

    public static Fragment newInstance(Context context, String txHash) {
        Bundle args = new Bundle();
        args.putString(TX_HASH, txHash);
        Fragment fragment = Factory.instantiateDefaultFragment(context, AddressesDetailFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mAddressesDetailPresenter = new AddressesDetailPresenterImpl(this, new AddressesDetailInteractorImpl(getContext()));
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mRecyclerViewFrom.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewTo.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public AddressesDetailPresenter getPresenter() {
        return mAddressesDetailPresenter;
    }

    @Override
    public String getTxHash() {
        return getArguments().getString(TX_HASH);
    }
}
