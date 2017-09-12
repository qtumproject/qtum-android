package org.qtum.wallet.ui.fragment.restore_contracts_fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.FontCheckBox;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class RestoreContractsFragment extends BaseFragment implements RestoreContractsFragmentView {

    private RestoreContractsFragmentPresenter mRestoreContractsFragmentPresenter;

    private boolean isSelectedFile = false;

    @BindView(org.qtum.wallet.R.id.rl_back_up_file)
    protected
    FrameLayout mFrameLayoutBackUpFile;
    @BindView(org.qtum.wallet.R.id.cb_restore_templates)
    FontCheckBox mCheckBoxRestoreTemplates;
    @BindView(org.qtum.wallet.R.id.cb_restore_contracts)
    FontCheckBox mCheckBoxRestoreContracts;
    @BindView(org.qtum.wallet.R.id.cb_restore_tokens)
    FontCheckBox mCheckBoxRestoreTokens;
    @BindView(org.qtum.wallet.R.id.cb_restore_all)
    FontCheckBox mCheckBoxRestoreAll;

    @BindView(org.qtum.wallet.R.id.tv_select_back_up)
    protected
    FontTextView mTextViewFileName;
    @BindView(org.qtum.wallet.R.id.tv_file_size)
    protected
    FontTextView mTextViewFileSize;
    @BindView(org.qtum.wallet.R.id.iv_restore_icon)
    protected
    ImageView mImageViewRestoreIcon;


    @OnClick({org.qtum.wallet.R.id.rl_back_up_file, org.qtum.wallet.R.id.cb_restore_templates, org.qtum.wallet.R.id.cb_restore_contracts, org.qtum.wallet.R.id.cb_restore_tokens, org.qtum.wallet.R.id.cb_restore_all, org.qtum.wallet.R.id.iv_restore_icon, org.qtum.wallet.R.id.ibt_back, org.qtum.wallet.R.id.bt_restore})
    public void onClick(View view){
        switch (view.getId()){
            case org.qtum.wallet.R.id.rl_back_up_file:
                getPresenter().checkPermissionAndOpenFileDialog();
                break;
            case  org.qtum.wallet.R.id.iv_restore_icon:
                getPresenter().onDeleteFileClick();
                break;
            case org.qtum.wallet.R.id.cb_restore_templates:
                if(mCheckBoxRestoreTemplates.isChecked()){
                    if(mCheckBoxRestoreContracts.isChecked() && mCheckBoxRestoreTokens.isChecked()){
                        mCheckBoxRestoreAll.setChecked(true);
                    }
                } else {
                    mCheckBoxRestoreAll.setChecked(false);
                }
                break;
            case org.qtum.wallet.R.id.cb_restore_contracts:
                if(mCheckBoxRestoreContracts.isChecked()){
                    if(mCheckBoxRestoreTemplates.isChecked() && mCheckBoxRestoreTokens.isChecked()){
                        mCheckBoxRestoreAll.setChecked(true);
                    }
                } else {
                    mCheckBoxRestoreAll.setChecked(false);
                }
                break;
            case org.qtum.wallet.R.id.cb_restore_tokens:
                if(mCheckBoxRestoreTokens.isChecked()){
                    if(mCheckBoxRestoreContracts.isChecked() && mCheckBoxRestoreTemplates.isChecked()){
                        mCheckBoxRestoreAll.setChecked(true);
                    }
                } else {
                    mCheckBoxRestoreAll.setChecked(false);
                }
                break;
            case org.qtum.wallet.R.id.cb_restore_all:
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
            case org.qtum.wallet.R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case org.qtum.wallet.R.id.bt_restore:
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