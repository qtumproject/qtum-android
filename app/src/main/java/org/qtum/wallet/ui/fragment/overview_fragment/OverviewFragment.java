package org.qtum.wallet.ui.fragment.overview_fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.addresses_detail_fragment.AddressesDetailFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

public abstract class OverviewFragment extends BaseFragment implements OverviewView{

    OverviewPresenter mOverviewPresenter;
    public static String POSITION = "position";

    public static Fragment newInstance(Context context, int position) {
        Bundle args = new Bundle();
        args.putInt(POSITION, position);
        Fragment fragment = Factory.instantiateDefaultFragment(context, OverviewFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mOverviewPresenter = new OverviewPresenterImpl(this, new OverviewIteractorImpl(getContext()));
    }

    @Override
    protected OverviewPresenter getPresenter() {
        return mOverviewPresenter;
    }

    @Override
    public int getPosition() {
        return getArguments().getInt(AddressesDetailFragment.POSITION);
    }
}
