package com.pixelplex.qtum.ui.fragment.start_page_fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.fragment_factory.Factory;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.pin_fragment.PinFragment;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class StartPageFragment extends BaseFragment implements StartPageFragmentView {

    private StartPageFragmentPresenterImpl mStartPageFragmentPresenter;

    private static final String IS_LOGIN = "is_login";

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

    @OnClick({R.id.bt_import_wallet, R.id.bt_create_new, R.id.bt_login})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create_new:
                getPresenter().createNewWallet();
                break;
            case R.id.bt_import_wallet:
                getPresenter().importWallet();
                break;
            case R.id.bt_login:
                if (QtumSharedPreference.getInstance().getKeyGeneratedInstance(getContext())){
                    BaseFragment fragment = PinFragment.newInstance(PinFragment.AUTHENTICATION, getContext());
                    openFragment(fragment);
                }
                break;
        }
    }

    public static BaseFragment newInstance(boolean isLogin, Context context) {
        Bundle args = new Bundle();
        args.putBoolean(IS_LOGIN, isLogin);
        BaseFragment fragment = Factory.instantiateFragment(context, StartPageFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mStartPageFragmentPresenter = new StartPageFragmentPresenterImpl(this);
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
        if(getArguments().getBoolean(IS_LOGIN,false)){
            BaseFragment fragment = PinFragment.newInstance(PinFragment.AUTHENTICATION, getContext());
            openFragment(fragment);
        }
    }

}
