package org.qtum.wallet.ui.fragment.token_cash_management_fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.TokenBalanceChangeListener;
import org.qtum.wallet.model.AddressWithTokenBalance;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class AddressesListFragmentToken extends BaseFragment implements AddressesListTokenView, OnAddressTokenClickListener {

    public static final String TOKEN = "token_item";
    public static final String TOKEN_CURRENCY = "token_currency";

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    AddressesListTokenPresenter presenter;
    protected TokenAddressesAdapter adapter;
    Handler handler;
    protected AlertDialog mTransferDialog;

    public static BaseFragment newInstance(Context context, Token token, String currency) {
        Bundle args = new Bundle();
        args.putString(TOKEN_CURRENCY, currency);
        args.putSerializable(TOKEN, token);
        BaseFragment fragment = Factory.instantiateFragment(context, AddressesListFragmentToken.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getSocketInstance().addTokenBalanceChangeListener(getPresenter().getContractAddress(), new TokenBalanceChangeListener() {
            @Override
            public void onBalanceChange(TokenBalance tokenBalance) {
                getSocketInstance().removeTokenBalanceChangeListener(tokenBalance.getContractAddress(), this);
                getPresenter().setTokenBalance(tokenBalance);
                getPresenter().processTokenBalances(tokenBalance);
                getHandler().post((Runnable) getPresenter());
            }
        });
    }

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onPause() {
        getHandler().removeCallbacks((Runnable) getPresenter());
        if (mTransferDialog != null) {
            mTransferDialog.dismiss();
        }
        super.onPause();
    }

    @Override
    protected void createPresenter() {
        presenter = new AddressesListTokenPresenterImpl(this, new AddressesListTokenInteractorImpl(getContext()));
    }

    @Override
    protected AddressesListTokenPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void initializeViews() {
        handler = new Handler();
        presenter.setToken((Token) getArguments().getSerializable(TOKEN));
        presenter.setCurrency(getArguments().getString(TOKEN_CURRENCY));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public UpdateService getSocketInstance() {
        return getMainActivity().getUpdateService();
    }

    @Override
    public void hideTransferDialog() {
        if (mTransferDialog != null && mTransferDialog.isShowing()) {
            mTransferDialog.dismiss();
        }
    }

    @Override
    public void goToSendFragment(AddressWithTokenBalance keyWithTokenBalanceFrom, AddressWithTokenBalance keyWithBalanceTo, String amountString, String contractAddress) {
        getMainActivity().setIconChecked(3);
        Fragment fragment = SendFragment.newInstance(keyWithTokenBalanceFrom.getAddress(), keyWithBalanceTo.getAddress(), amountString, contractAddress, getContext());
        getMainActivity().setRootFragment(fragment);
        openRootFragment(fragment);
    }
}
