package org.qtum.wallet.ui.fragment.backup_contracts_fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class BackupContractsFragment extends BaseFragment implements BackupContractsView {

    private BackupContractsPresenter mBackupContractsFragmentPresenter;

    @BindView(R.id.tv_file_size)
    FontTextView mTextViewFileSize;

    private boolean PERMISION_GRANT = false;

    String STATE;
    private final String CHECK_PERMISSION_AND_BACKUP = "check_permission_and_backup";
    private final String CHECK_PERMISSION_AND_CREATE = "check_permission_and_create";

    private final int WRITE_EXTERNAL_STORAGE_CODE = 5;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermissionForCreateFile();
        getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if(requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        PERMISION_GRANT = true;
                    }
                }
            }
        });
    }

    @Override
    public void checkPermissionForCreateFile(){
        if(getMainActivity().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            getPresenter().permissionGrantedForCreateBackUpFile();
        } else {
            STATE = CHECK_PERMISSION_AND_CREATE;
            getMainActivity().loadPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);
        }
    }

    @Override
    public void checkPermissionForBackupFile(){
        if(getMainActivity().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            getPresenter().permissionGrantedForChooseShareMethod();
        } else {
            STATE = CHECK_PERMISSION_AND_BACKUP;
            getMainActivity().loadPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE_CODE);
        }
    }

    @Override
    public void chooseShareMethod(String absolutePath) {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("application/json");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+absolutePath));

        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                "Qtum Backup File");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Qtum Backup File");
        getMainActivity().startActivity(Intent.createChooser(intentShareFile, "Share File"));
    }

    @Override
    public void onResume() {
        super.onResume();
        if(PERMISION_GRANT){
            switch (STATE){
                case CHECK_PERMISSION_AND_BACKUP:
                    getPresenter().permissionGrantedForCreateAndBackUpFile();
                    break;
                case CHECK_PERMISSION_AND_CREATE:
                    getPresenter().permissionGrantedForCreateBackUpFile();
                    break;
            }
            PERMISION_GRANT = false;
        }
    }

    @Override
    protected void createPresenter() {
        mBackupContractsFragmentPresenter = new BackupContractsPresenterImpl(this, new BackupContractsInteractorImpl(getContext()));
    }

    @Override
    protected BackupContractsPresenter getPresenter() {
        return mBackupContractsFragmentPresenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivity().removePermissionResultListener();
    }

    @Override
    public void setUpFile(String fileSize) {
        mTextViewFileSize.setText(fileSize);
    }
}
