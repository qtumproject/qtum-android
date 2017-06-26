package com.pixelplex.qtum.ui.fragment.BackupContractsFragment;

import android.os.Bundle;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;


public class BackupContractsFragment extends BaseFragment implements BackupContractsFragmentView {
    
    BackupContractsFragmentPresenter mBackupContractsFragmentPresenter;

    @BindView(R.id.tv_file_size)
    FontTextView mTextViewFileSize;

    @OnClick({R.id.ibt_back, R.id.fl_back_up_file})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.fl_back_up_file:
                getPresenter().onBackUpClick();
        }
    }

    public static BackupContractsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        BackupContractsFragment fragment = new BackupContractsFragment();
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
    protected int getLayout() {
        return R.layout.fragment_backup_contracts;
    }

    @Override
    public void setUpFile(String fileSize) {
        mTextViewFileSize.setText(fileSize);
    }
}
