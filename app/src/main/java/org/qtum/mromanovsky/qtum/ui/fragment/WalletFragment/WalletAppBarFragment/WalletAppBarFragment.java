package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletAppBarFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class WalletAppBarFragment extends Fragment {

    private Unbinder mUnbinder;

    @BindView(R.id.tv_public_key)
    TextView mTvPublicKey;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.ll_receive)
    LinearLayout mLinearLayoutReceive;

    @BindView(R.id.progress_bar_balance)
    ProgressBar mProgressBarDialog;


    @OnClick({R.id.ll_receive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_receive:
                //getPresenter().onClickReceive();
                break;

        }
    }

    public static WalletAppBarFragment newInstance(){
        WalletAppBarFragment walletAppBarFragment = new WalletAppBarFragment();
        return walletAppBarFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet_appbar,container,false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public void startRefreshAnimation() {
        mTvBalance.setVisibility(View.GONE);
        mProgressBarDialog.setVisibility(View.VISIBLE);
    }

    public void updatePubKey(String pubKey) {
        mTvPublicKey.setText(pubKey);
    }

    public void updateBalance(double balance) {
        mTvBalance.setText(String.valueOf(balance));
        mTvBalance.setVisibility(View.VISIBLE);
        mProgressBarDialog.setVisibility(View.GONE);
    }

}
