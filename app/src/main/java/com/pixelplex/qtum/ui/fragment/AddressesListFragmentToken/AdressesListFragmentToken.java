package com.pixelplex.qtum.ui.fragment.AddressesListFragmentToken;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.UpdateService;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 03.08.17.
 */

public abstract class AdressesListFragmentToken extends BaseFragment implements AdressesListFragmentTokenView, OnAddressTokenClickListener {

    Handler handler;

    protected AlertDialog mTransferDialog;

    public static final String TOKEN = "token_item";
    public static final String TOKEN_CURRENCY = "token_currency";

    protected TokenAdressesAdapter adapter;

    public static BaseFragment newInstance(Context context, Token token, String currency) {
        Bundle args = new Bundle();
        args.putString(TOKEN_CURRENCY, currency);
        args.putSerializable(TOKEN,token);
        BaseFragment fragment = Factory.instantiateFragment(context, AdressesListFragmentToken.class);
        fragment.setArguments(args);
        return fragment;
    }

    AdressesListFragmentTokenPresenter presenter;

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    protected void createPresenter() {
        presenter = new AdressesListFragmentTokenPresenter(this);
    }

    @Override
    protected AdressesListFragmentTokenPresenter getPresenter() {
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
        if(mTransferDialog != null && mTransferDialog.isShowing()){
            mTransferDialog.dismiss();
        }
    }
}
