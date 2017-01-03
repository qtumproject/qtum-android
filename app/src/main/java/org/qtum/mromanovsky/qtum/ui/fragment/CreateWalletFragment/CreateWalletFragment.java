package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BalanceFragment.BalanceFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateWalletFragment extends BaseFragment implements CreateWalletFragmentView{

    public static final int  LAYOUT = R.layout.fragment_create_wallet;
    public static final String TAG = "CreateWalletFragment";

    CreateWalletFragmentPresenterImpl mCreateWalletFragmentPresenter;

    @BindView(R.id.button_create_wallet)
    Button mButtonCreateWallet;

    @BindView(R.id.button_import_wallet_fragment_create)
    Button mButtonImportWallet;

    @OnClick({R.id.button_create_wallet,R.id.button_import_wallet_fragment_create})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button_create_wallet:
                BalanceFragment balanceFragment = BalanceFragment.newInstance();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main,balanceFragment,balanceFragment.getClass().getCanonicalName())
                        .commit();
                break;
            case R.id.button_import_wallet_fragment_create:
                //TODO : stub
                break;
        }
    }

    public static CreateWalletFragment newInstance(){
        CreateWalletFragment createWalletFragment = new CreateWalletFragment();
        return createWalletFragment;
    }

    @Override
    protected void createPresenter() {
        mCreateWalletFragmentPresenter = new CreateWalletFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mCreateWalletFragmentPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(LAYOUT,container,false);
    }

}
