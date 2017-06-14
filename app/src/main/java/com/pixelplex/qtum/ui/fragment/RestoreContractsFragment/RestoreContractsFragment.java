package com.pixelplex.qtum.ui.fragment.RestoreContractsFragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FontCheckBox;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


/**
 * Created by max-v on 6/14/2017.
 */

public class RestoreContractsFragment extends BaseFragment implements RestoreContractsFragmentView {

    RestoreContractsFragmentPresenter mRestoreContractsFragmentPresenter;

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

    @OnClick({R.id.fl_back_up_file,R.id.cb_restore_templates,R.id.cb_restore_contracts,R.id.cb_restore_tokens,R.id.cb_restore_all})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fl_back_up_file:
                getPresenter().checkPermissionAndShow();
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

}