package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class WalletFragment extends BaseFragment implements WalletFragmentView {

    public static final int  LAYOUT = R.layout.fragment_wallet;
    public static final String TAG = "WalletFragment";

    WalletFragmentPresenterImpl mWalletFragmentPresenter;


    @BindView(R.id.tv_public_key)
    TextView mTextViewPublicKey;
    @BindView(R.id.tv_balance)
    TextView mTextViewBalance;
    @BindView(R.id.fab)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.receive)
    LinearLayout mReceive;
//    @BindView(R.id.swipe_refresh)
//    SwipeRefreshLayout mSwipeRefreshLayout;


    @OnClick({R.id.fab,R.id.receive})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.fab:
//                mWalletFragmentPresenter.onFabClick();
                break;
            case R.id.receive:
                getPresenter().onClickReceive();
        }
    }

    public static WalletFragment newInstance(){
        WalletFragment walletFragment = new WalletFragment();
        return walletFragment;
    }

    @Override
    protected void createPresenter() {
        mWalletFragmentPresenter = new WalletFragmentPresenterImpl(this);
    }

    @Override
    protected WalletFragmentPresenterImpl getPresenter() {
        return mWalletFragmentPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(LAYOUT, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (null != toolbar) {
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setTitle(R.string.my_wallet);
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ((MainActivity) getActivity()).showBottomNavigationView();
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//        });
    }

    @Override
    public void openFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,fragment,fragment.getClass().getCanonicalName())
                .commit();
    }

    //    @Override
//    public void updatePublicKey(final String publicKey) {
//        getView().post(new Runnable() {
//            @Override
//            public void run() {
//                mTextViewPublicKey.setText(publicKey);
//        }
//        });
//    }
//
//    @Override
//    public void updateBalance(final String balance) {
//        getView().post(new Runnable() {
//            @Override
//            public void run() {
//                mTextViewBalance.setText(balance);
//            }
//        });
//    }
}
