package com.pixelplex.qtum.ui.fragment.BackupContractsFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class BackupContractsFragment extends BaseFragment implements BackupContractsFragmentView {
    
    private BackupContractsFragmentPresenter mBackupContractsFragmentPresenter;

    @BindView(R.id.tv_file_size)
    FontTextView mTextViewFileSize;

    @OnClick({R.id.ibt_back, R.id.rl_back_up_file})
    public void onClick(View view){
        switch (view.getId()){
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
        mBackupContractsFragmentPresenter = new BackupContractsFragmentPresenter(this);
    }

    @Override
    protected BackupContractsFragmentPresenter getPresenter() {
        return mBackupContractsFragmentPresenter;
    }

    @Override
    public void setUpFile(String fileSize) {
        mTextViewFileSize.setText(fileSize);
    }
}
