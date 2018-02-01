package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import org.jsoup.helper.StringUtil;
import org.qtum.wallet.R;
import org.qtum.wallet.model.AddressWithBalance;
import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class AddressListFragment extends BaseFragment implements AddressListView, OnAddressClickListener {

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    protected AlertDialog mTransferDialog;
    protected boolean showTransferDialog = false;

    AddressListPresenter mAddressListPresenter;
    protected AddressesWithBalanceAdapter mAddressesWithBalanceAdapter;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, AddressListFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mAddressListPresenter = new AddressListPresenterImpl(this, new AddressListInteractorImpl(getContext()));
    }

    @Override
    protected AddressListPresenter getPresenter() {
        return mAddressListPresenter;
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

    public void transfer(AddressWithBalance keyWithBalanceTo, AddressWithBalance keyWithBalanceFrom, String amountString) {
        if (!isValidFloat(amountString)) {
            setAlertDialog(getString(R.string.error),
                    getString(R.string.enter_valid_amount_value),
                    getString(R.string.ok),
                    BaseFragment.PopUpType.error);
            return;
        }

        if (Float.valueOf(amountString) <= 0) {
            setAlertDialog(getString(R.string.error),
                    getString(R.string.transaction_amount_cant_be_zero),
                    getString(R.string.ok),
                    BaseFragment.PopUpType.error);
            return;
        }

        Fragment fragment = SendFragment.newInstance(keyWithBalanceFrom.getAddress(), keyWithBalanceTo.getAddress(), amountString, "", getContext());
        getMainActivity().setRootFragment(fragment);
        openRootFragment(fragment);
    }

    private boolean isValidFloat(String value) {
        return !TextUtils.isEmpty(value) && !(value.length() == 1 && (value.charAt(0) == '.' || value.charAt(0) == ','));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivity().removeAuthenticationListener();
    }
}
