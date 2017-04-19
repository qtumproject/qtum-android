package org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletAppBarFragment;

import android.app.Activity;
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

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class WalletAppBarFragment extends Fragment implements WalletAppBarFragmentView{

    private Unbinder mUnbinder;
    private WalletAppBarFragmentPresenter mWalletAppBarFragmentPresenter;
    private final static String POSITION = "position";

    @BindView(R.id.tv_public_key)
    TextView mTvPublicKey;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.ll_receive)
    LinearLayout mLinearLayoutReceive;
    @BindView(R.id.tv_unconfirmed_balance)
    TextView mTextViewUnconfirmedBalance;
    @BindView(R.id.tv_symbol)
    TextView mTextViewSymbol;

    @BindView(R.id.progress_bar_balance)
    ProgressBar mProgressBarDialog;


    @OnClick({R.id.ll_receive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_receive:
                getPresenter().onReceiveClick();
                break;
        }
    }

    public static WalletAppBarFragment newInstance(int position) {
        
        Bundle args = new Bundle();
        args.putInt(POSITION,position);
        WalletAppBarFragment fragment = new WalletAppBarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWalletAppBarFragmentPresenter = new WalletAppBarFragmentPresenter(this);
    }

    private WalletAppBarFragmentPresenter getPresenter(){
        return mWalletAppBarFragmentPresenter;
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
        getPresenter().setPosition(getArguments().getInt(POSITION));
        getPresenter().onViewCreated();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().onDestroyView();
        mUnbinder.unbind();
    }

    public void updatePubKey(String pubKey) {
        mTvPublicKey.setText(pubKey);
    }

    public void updateBalance(String balance,String unconfirmedBalance) {
        mTvBalance.setText(balance);
        mTextViewUnconfirmedBalance.setText(unconfirmedBalance);
        mTvBalance.setVisibility(View.VISIBLE);
        mProgressBarDialog.setVisibility(View.GONE);
    }

    @Override
    public void setSymbol(String symbol) {
        mTextViewSymbol.setText(symbol);
    }

    @Override
    public void openFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .add(R.id.fragment_container, fragment, fragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public Activity getFragmentActivity() {
        return getActivity();
    }
}