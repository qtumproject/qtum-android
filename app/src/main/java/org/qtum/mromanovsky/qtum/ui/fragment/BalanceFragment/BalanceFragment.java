package org.qtum.mromanovsky.qtum.ui.fragment.BalanceFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment.ReceiveFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.SendFragment.SendFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class BalanceFragment extends BaseFragment implements BalanceFragmentView{

    public static final int  LAYOUT = R.layout.fragment_balance;
    public static final String TAG = "BalanceFragment";

    BalanceFragmentPresenterImpl mBalanceFragmentPresenter;

    @BindView(R.id.send_button_my_wallet)
    Button mButtonSend;

    @BindView(R.id.recieve_button_my_wallet)
    Button mButtonImportRecieve;

    @OnClick({R.id.send_button_my_wallet,R.id.recieve_button_my_wallet})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.send_button_my_wallet:
                SendFragment sendFragment = SendFragment.newInstance();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main,sendFragment,sendFragment.getClass().getCanonicalName())
                        .commit();
                break;
            case R.id.recieve_button_my_wallet:
                ReceiveFragment receiveFragment = ReceiveFragment.newInstance();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main,receiveFragment,receiveFragment.getClass().getCanonicalName())
                        .commit();
                break;
        }
    }

    public static BalanceFragment newInstance(){
        BalanceFragment balanceFragment = new BalanceFragment();
        return balanceFragment;
    }

    @Override
    protected void createPresenter() {
        mBalanceFragmentPresenter = new BalanceFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mBalanceFragmentPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(LAYOUT, container, false);
    }
}
