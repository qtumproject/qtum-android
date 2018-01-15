package org.qtum.wallet.ui.fragment.deleted_contract_fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.my_contracts_fragment.MyContractsFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class DeletedContractFragment extends BaseFragment implements DeletedContractView{

    DeletedContractPresenter mPresenter;
    final static String CONTRACT_ADDRESS = "contract_address";
    final static String CONTRACT_NAME = "contract_name";

    @BindView(R.id.tv_toolbar_deleted_contract)
    TextView mTextViewToolBar;

    @OnClick({R.id.ibt_back, R.id.bt_unsubscribe})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.bt_unsubscribe:
                getPresenter().onUnubscribeClick(getArguments().getString(CONTRACT_ADDRESS));
                ((MyContractsFragment)getTargetFragment()).onUnsubscribeClick();
                getActivity().onBackPressed();
                break;
        }
    }

    public static BaseFragment newInstance(Context context, String contractAddress, String contractName) {

        Bundle args = new Bundle();
        args.putString(CONTRACT_ADDRESS, contractAddress);
        args.putString(CONTRACT_NAME, contractName);
        BaseFragment fragment = Factory.instantiateFragment(context, DeletedContractFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mTextViewToolBar.setText(getArguments().getString(CONTRACT_NAME));
    }

    @Override
    protected DeletedContractPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void createPresenter() {
        mPresenter = new DeletedContractPresenterImpl(this, new DeletedContractInteractorImpl(getContext()));
    }
}
