package com.pixelplex.qtum.ui.fragment.about_fragment;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment_factory.Factory;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class AboutFragment extends BaseFragment implements AboutFragmentView{

    AboutFragmentPresenter mAboutFragmentPresenter;

    @OnClick({R.id.ibt_back})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @BindView(R.id.tv_qtum_version)
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
            String footer = getString(R.string._2017_qtum_n_skynet_testnet_version) + "Version " + version + "(" + String.valueOf(codeVersion) + ")";
            mTextViewQtumVersion.setText(footer);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void createPresenter() {
        mAboutFragmentPresenter = new AboutFragmentPresenter(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mAboutFragmentPresenter;
    }
}
