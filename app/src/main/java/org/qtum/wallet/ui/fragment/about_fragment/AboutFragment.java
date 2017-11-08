package org.qtum.wallet.ui.fragment.about_fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import org.qtum.wallet.model.Version;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
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
    public void updateVersion(Version version) {
        String footer = getString(org.qtum.wallet.R.string._2017_qtum_n_skynet_testnet_version) + "Version " + version.getVersionName() + "(" + String.valueOf(version.getVersionCode()) + ")";
        mTextViewQtumVersion.setText(footer);
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