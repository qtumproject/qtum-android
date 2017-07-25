package com.pixelplex.qtum.ui.fragment.RestoreContractsFragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.utils.FontCheckBox;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class RestoreContractsFragment extends BaseFragment implements RestoreContractsFragmentView {

    private RestoreContractsFragmentPresenter mRestoreContractsFragmentPresenter;

    private boolean isSelectedFile = false;

    @BindView(R.id.rl_back_up_file)
    protected
    FrameLayout mFrameLayoutBackUpFile;
    @BindView(R.id.cb_restore_templates)
    FontCheckBox mCheckBoxRestoreTemplates;
    @BindView(R.id.cb_restore_contracts)
    FontCheckBox mCheckBoxRestoreContracts;
    @BindView(R.id.cb_restore_tokens)
    FontCheckBox mCheckBoxRestoreTokens;
    @BindView(R.id.cb_restore_all)
    FontCheckBox mCheckBoxRestoreAll;

    @BindView(R.id.tv_select_back_up)
    protected
    FontTextView mTextViewFileName;
    @BindView(R.id.tv_file_size)
    protected
    FontTextView mTextViewFileSize;
    @BindView(R.id.iv_restore_icon)
    protected
    ImageView mImageViewRestoreIcon;


    @OnClick({R.id.rl_back_up_file,R.id.cb_restore_templates,R.id.cb_restore_contracts,R.id.cb_restore_tokens,R.id.cb_restore_all, R.id.iv_restore_icon, R.id.ibt_back, R.id.bt_restore})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_back_up_file:
                getPresenter().checkPermissionAndOpenFileDialog();
                break;
            case  R.id.iv_restore_icon:
                getPresenter().onDeleteFileClick();
                break;
            case R.id.cb_restore_templates:
                if(mCheckBoxRestoreTemplates.isChecked()){
                    if(mCheckBoxRestoreContracts.isChecked() && mCheckBoxRestoreTokens.isChecked()){
                        mCheckBoxRestoreAll.setChecked(true);
                    }
                } else {
                    mCheckBoxRestoreAll.setChecked(false);
                }
                break;
            case R.id.cb_restore_contracts:
                if(mCheckBoxRestoreContracts.isChecked()){
                    if(mCheckBoxRestoreTemplates.isChecked() && mCheckBoxRestoreTokens.isChecked()){
                        mCheckBoxRestoreAll.setChecked(true);
                    }
                } else {
                    mCheckBoxRestoreAll.setChecked(false);
                }
                break;
            case R.id.cb_restore_tokens:
                if(mCheckBoxRestoreTokens.isChecked()){
                    if(mCheckBoxRestoreContracts.isChecked() && mCheckBoxRestoreTemplates.isChecked()){
                        mCheckBoxRestoreAll.setChecked(true);
                    }
                } else {
                    mCheckBoxRestoreAll.setChecked(false);
                }
                break;
            case R.id.cb_restore_all:
                if(mCheckBoxRestoreAll.isChecked()) {
                    mCheckBoxRestoreTemplates.setChecked(true);
                    mCheckBoxRestoreContracts.setChecked(true);
                    mCheckBoxRestoreTokens.setChecked(true);
                } else {
                    mCheckBoxRestoreTemplates.setChecked(false);
                    mCheckBoxRestoreContracts.setChecked(false);
                    mCheckBoxRestoreTokens.setChecked(false);
                }
                break;
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.bt_restore:
                if(mCheckBoxRestoreAll.isChecked()) {
                    getPresenter().onRestoreClick(true,true,true);
                } else {
                    getPresenter().onRestoreClick(mCheckBoxRestoreTemplates.isChecked(),mCheckBoxRestoreContracts.isChecked(),mCheckBoxRestoreTokens.isChecked());
                }
                break;
        }
    }

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, RestoreContractsFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mRestoreContractsFragmentPresenter = new RestoreContractsFragmentPresenter(this);
    }

    @Override
    protected RestoreContractsFragmentPresenter getPresenter() {
        return mRestoreContractsFragmentPresenter;
    }
}