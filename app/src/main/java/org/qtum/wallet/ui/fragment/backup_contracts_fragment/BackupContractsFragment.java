package org.qtum.wallet.ui.fragment.backup_contracts_fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class BackupContractsFragment extends BaseFragment implements BackupContractsView {

    private BackupContractsPresenterImpl mBackupContractsFragmentPresenter;

    @BindView(R.id.tv_file_size)
    FontTextView mTextViewFileSize;

    @OnClick({R.id.ibt_back, R.id.rl_back_up_file})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.rl_back_up_file:
                getPresenter().onBackUpClick();
        }
    }

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, BackupContractsFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mBackupContractsFragmentPresenter = new BackupContractsPresenterImpl(this, new BackupContractsInteractorImpl(getContext()));
    }

    @Override
    protected BackupContractsPresenterImpl getPresenter() {
        return mBackupContractsFragmentPresenter;
    }

    @Override
    public void setUpFile(String fileSize) {
        mTextViewFileSize.setText(fileSize);
    }
}
