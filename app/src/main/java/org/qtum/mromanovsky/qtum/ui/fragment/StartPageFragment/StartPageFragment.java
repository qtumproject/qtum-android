package org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletFragment.CreateWalletFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class StartPageFragment extends BaseFragment implements StartPageFragmentView{

    public static final int LAYOUT = R.layout.fragment_start_page;
    public static final String TAG = "StartPageFragment";

    private StartPageFragmentPresenterImpl mStartPageFragmentPresenter;


    @BindView(R.id.button_create_new)
    Button mButtonCreateNew;

    @BindView(R.id.button_import_wallet)
    Button mButtonImportWallet;

    @OnClick({R.id.button_import_wallet,R.id.button_create_new})
    public void OnClick(View view){
        switch (view.getId()) {
            case R.id.button_create_new:
                CreateWalletFragment createWalletFragment = CreateWalletFragment.newInstance();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.activity_main,createWalletFragment,createWalletFragment.getClass().getCanonicalName())
                        .commit();
                break;
            case R.id.button_import_wallet:
                mStartPageFragmentPresenter.onClick();
                break;
        }
    }


    public static StartPageFragment newInstance(){
        StartPageFragment startPageFragment = new StartPageFragment();
        return startPageFragment;
    }

    @Override
    protected void createPresenter() {
        mStartPageFragmentPresenter = new StartPageFragmentPresenterImpl(this);

    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mStartPageFragmentPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(LAYOUT,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    /*@Override
    public Context getContext() {
        return super.getContext();
    }*/
}
