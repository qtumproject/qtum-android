package com.pixelplex.qtum.ui.fragment.RestoreContractsFragment;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.utils.FontCheckBox;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;


public class RestoreContractsFragment extends BaseFragment implements RestoreContractsFragmentView {

    RestoreContractsFragmentPresenter mRestoreContractsFragmentPresenter;

    private boolean isSelectedFile = false;

    @BindView(R.id.fl_back_up_file)
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
    FontTextView mTextViewFileName;
    @BindView(R.id.tv_file_size)
    FontTextView mTextViewFileSize;
    @BindView(R.id.iv_restore_icon)
    ImageView mImageViewRestoreIcon;


    @OnClick({R.id.fl_back_up_file,R.id.cb_restore_templates,R.id.cb_restore_contracts,R.id.cb_restore_tokens,R.id.cb_restore_all, R.id.iv_restore_icon, R.id.ibt_back, R.id.bt_restore})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fl_back_up_file:
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

    public static RestoreContractsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        RestoreContractsFragment fragment = new RestoreContractsFragment();
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

    @Override
    protected int getLayout() {
        return R.layout.fragment_restore_contracts;
    }

    @Override
    public void setFile(String name, String size) {
        mTextViewFileName.setText(name);
        mTextViewFileSize.setVisibility(View.VISIBLE);
        mTextViewFileSize.setText(size);
        mImageViewRestoreIcon.setClickable(true);
        mFrameLayoutBackUpFile.setClickable(false);
        mImageViewRestoreIcon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_delete));
    }

    @Override
    public void deleteFile() {
        mTextViewFileName.setText(R.string.select_back_up_file);
        mTextViewFileSize.setVisibility(View.GONE);
        mImageViewRestoreIcon.setClickable(false);
        mFrameLayoutBackUpFile.setClickable(true);
        mImageViewRestoreIcon.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_attach));
    }
}