package org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class BackUpWalletFragment extends BaseFragment implements BackUpWalletFragmentView {

    public static final int LAYOUT = R.layout.fragment_back_up_wallet;
    BackUpWalletFragmentPresenterImpl mBackUpWalletFragmentPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_brain_code)
    TextView mTextViewBrainCode;
    @BindView(R.id.bt_copy_brain_code)
    Button mButtonCopyBrainCode;

    @OnClick({R.id.bt_copy_brain_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_copy_brain_code:
                getPresenter().onCopyBrainCodeClick();
                break;
        }
    }

    public static BackUpWalletFragment newInstance() {
        BackUpWalletFragment backUpWalletFragment = new BackUpWalletFragment();
        return backUpWalletFragment;
    }

    @Override
    protected void createPresenter() {
        mBackUpWalletFragmentPresenter = new BackUpWalletFragmentPresenterImpl(this);
    }

    @Override
    protected BackUpWalletFragmentPresenterImpl getPresenter() {
        return mBackUpWalletFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_indicator);
        }
    }

    @Override
    public void setBrainCode(String seed) {
        mTextViewBrainCode.setText(seed);
    }
}
