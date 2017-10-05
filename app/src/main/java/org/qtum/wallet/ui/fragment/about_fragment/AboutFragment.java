package org.qtum.wallet.ui.fragment.about_fragment;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class AboutFragment extends BaseFragment implements AboutView {

    AboutPresenter mAboutFragmentPresenter;

    @OnClick({org.qtum.wallet.R.id.ibt_back})
    public void onClick(View view){
        switch (view.getId()) {
            case org.qtum.wallet.R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @BindView(org.qtum.wallet.R.id.tv_qtum_version)
    FontTextView mTextViewQtumVersion;

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, AboutFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        try {
            PackageInfo pInfo = getMainActivity().getPackageManager().getPackageInfo(getMainActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            int codeVersion = pInfo.versionCode;
            String footer = getString(org.qtum.wallet.R.string._2017_qtum_n_skynet_testnet_version) + "Version " + version + "(" + String.valueOf(codeVersion) + ")";
            mTextViewQtumVersion.setText(footer);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void createPresenter() {
        mAboutFragmentPresenter = new AboutPresenterImpl(this, new AboutInteractorImpl(getContext()));
    }

    @Override
    protected AboutPresenter getPresenter() {
        return mAboutFragmentPresenter;
    }
}
