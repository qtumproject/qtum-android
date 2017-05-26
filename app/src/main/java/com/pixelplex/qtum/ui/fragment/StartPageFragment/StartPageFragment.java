package com.pixelplex.qtum.ui.fragment.StartPageFragment;

import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.SmartContractsManager.StorageManager;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.transitionseverywhere.extra.Scale;

import butterknife.BindView;
import butterknife.OnClick;


public class StartPageFragment extends BaseFragment implements StartPageFragmentView {

    private StartPageFragmentPresenterImpl mStartPageFragmentPresenter;

    @BindView(R.id.bt_create_new)
    Button mButtonCreateNew;
    @BindView(R.id.bt_import_wallet)
    Button mButtonImportWallet;
    @BindView(R.id.tv_start_page_you_dont_have)
    TextView mTextViewYouDontHave;
    @BindView(R.id.tv_start_page_create)
    TextView mTextViewStartPageCreate;
    @BindView(R.id.rl_button_container)
    RelativeLayout mRelativeLayoutButtonContainer;

    @BindView(R.id.logo_view)
    ImageView logoView;

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    @OnClick({R.id.bt_import_wallet, R.id.bt_create_new})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create_new:
                getPresenter().createNewWallet();
                break;
            case R.id.bt_import_wallet:
                getPresenter().importWallet();
                break;
        }
    }

    public static StartPageFragment newInstance() {

        Bundle args = new Bundle();

        StartPageFragment fragment = new StartPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mStartPageFragmentPresenter = new StartPageFragmentPresenterImpl(this);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected StartPageFragmentPresenterImpl getPresenter() {
        return mStartPageFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_start_page;
    }

    @Override
    public void initializeViews() {
        ((MainActivity) getActivity()).hideBottomNavigationView();
        StorageManager.getInstance().getContracts(getContext());
    }
}
