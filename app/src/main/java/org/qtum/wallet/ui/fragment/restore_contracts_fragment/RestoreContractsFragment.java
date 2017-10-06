package org.qtum.wallet.ui.fragment.restore_contracts_fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.FileUtils;
import org.qtum.wallet.utils.FontCheckBox;
import org.qtum.wallet.utils.FontTextView;
import org.qtum.wallet.utils.ThemeUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;


public abstract class RestoreContractsFragment extends BaseFragment implements RestoreContractsView {
    private final int FILE_SELECT_CODE = 0;
    private final int READ_EXTERNAL_STORAGE_CODE = 1;

    private RestoreContractsPresenter mRestoreContractsPresenter;
    private UpdateService mUpdateService;
    private AlertDialog mRestoreDialog;
    private File mRestoreFile;
    private Subscription s;

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
    public void onClick(View view) {
        switch (view.getId()) {
            case org.qtum.wallet.R.id.rl_back_up_file:
                if (getMainActivity().checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showFileChooser();
                } else {
                    getMainActivity().loadPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_CODE);
                }
                break;
            case org.qtum.wallet.R.id.iv_restore_icon:
                getPresenter().onDeleteFileClick();
                if (mRestoreFile != null) {
                    mRestoreFile.delete();
                    mRestoreFile = null;
                }
                break;
            case org.qtum.wallet.R.id.cb_restore_templates:
                if (mCheckBoxRestoreTemplates.isChecked()) {
                    if (mCheckBoxRestoreContracts.isChecked() && mCheckBoxRestoreTokens.isChecked()) {
                        mCheckBoxRestoreAll.setChecked(true);
                    }
                } else {
                    mCheckBoxRestoreAll.setChecked(false);
                }
                break;
            case org.qtum.wallet.R.id.cb_restore_contracts:
                if (mCheckBoxRestoreContracts.isChecked()) {
                    if (mCheckBoxRestoreTemplates.isChecked() && mCheckBoxRestoreTokens.isChecked()) {
                        mCheckBoxRestoreAll.setChecked(true);
                    }
                } else {
                    mCheckBoxRestoreAll.setChecked(false);
                }
                break;
            case org.qtum.wallet.R.id.cb_restore_tokens:
                if (mCheckBoxRestoreTokens.isChecked()) {
                    if (mCheckBoxRestoreContracts.isChecked() && mCheckBoxRestoreTemplates.isChecked()) {
                        mCheckBoxRestoreAll.setChecked(true);
                    }
                } else {
                    mCheckBoxRestoreAll.setChecked(false);
                }
                break;
            case org.qtum.wallet.R.id.cb_restore_all:
                if (mCheckBoxRestoreAll.isChecked()) {
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
                if (mCheckBoxRestoreAll.isChecked()) {
                    getPresenter().onRestoreClick(true, true, true);
                } else {
                    getPresenter().onRestoreClick(mCheckBoxRestoreTemplates.isChecked(), mCheckBoxRestoreContracts.isChecked(), mCheckBoxRestoreTokens.isChecked());
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUpdateService = getMainActivity().getUpdateService();

        getMainActivity().addActivityResultListener(new MainActivity.ActivityResultListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == FILE_SELECT_CODE) {
                    if (resultCode == RESULT_OK) {
                        String path = FileUtils.getPath(getContext(), data.getData());
                        if (path != null) {
                            File responseFile = new File(path);
                            mRestoreFile = new File(getContext().getFilesDir(), responseFile.getName());
                            try {
                                copyFileUsingStream(responseFile, mRestoreFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                ParcelFileDescriptor mInputPFD;
                                mInputPFD = getMainActivity().getContentResolver().openFileDescriptor(data.getData(), "r");
                                if (mInputPFD != null) {
                                    FileDescriptor fd = mInputPFD.getFileDescriptor();
                                    FileInputStream fis = new FileInputStream(fd);

                                    Uri uri = data.getData();

                                    String fileName = "";
                                    Cursor cursor = null;
                                    try {
                                        cursor = getMainActivity().getContentResolver().query(uri, new String[]{
                                                MediaStore.Images.ImageColumns.DISPLAY_NAME
                                        }, null, null, null);
                                        if (cursor != null && cursor.moveToFirst()) {
                                            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                                        }
                                    } finally {
                                        if (cursor != null) {
                                            cursor.close();
                                        }
                                    }

                                    mRestoreFile = new File(getContext().getFilesDir(), fileName);

                                    byte[] buffer;
                                    buffer = new byte[fis.available()];
                                    fis.read(buffer);

                                    OutputStream outStream = new FileOutputStream(mRestoreFile);
                                    outStream.write(buffer);
                                    outStream.flush();
                                    outStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }

                        }
                        String name = mRestoreFile.getName();
                        if (getFileExtension(name) != null) {
                            if (!getFileExtension(name).equals(".json")) {
                                setAlertDialog(getString(R.string.something_went_wrong), "", "OK", BaseFragment.PopUpType.error, new BaseFragment.AlertDialogCallBack() {
                                    @Override
                                    public void onButtonClick() {
                                        mRestoreFile.delete();
                                        mRestoreFile = null;
                                    }

                                    @Override
                                    public void onButton2Click() {

                                    }
                                });
                                return;
                            }
                        }
                        String fileSize = String.valueOf((int) Math.ceil(mRestoreFile.length() / 1024.0)) + " Kb";
                        setFile(name, fileSize);
                    }
                }
            }
        });

        getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        showFileChooser();
                    }
                }
            }
        });
    }


    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getView().getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRestoreFile != null) {
            mRestoreFile.delete();
        }
        if (s != null) {
            s.unsubscribe();
        }
        showBottomNavView(false);
        getMainActivity().removeResultListener();
        getMainActivity().removePermissionResultListener();
    }

    private String getFileExtension(String mystr) {
        int index = mystr.indexOf('.');
        return index == -1 ? null : mystr.substring(index);
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    @Override
    protected void createPresenter() {
        mRestoreContractsPresenter = new RestoreContractsPresenterImpl(this, new RestoreContractsInteractorImpl(getContext()));
    }

    @Override
    protected RestoreContractsPresenter getPresenter() {
        return mRestoreContractsPresenter;
    }

    @Override
    public void showRestoreDialogFragment(String backupDate, String backupAppVersion, String templatesSum, String contractsSum, String tokensSum) {
        View view = LayoutInflater.from(getMainActivity()).inflate(ThemeUtils.getCurrentTheme(getContext()).equals(ThemeUtils.THEME_DARK) ? R.layout.dialog_restore_contracts_fragment : R.layout.dialog_restore_contracts_fragment_light, null);
        ((FontTextView) view.findViewById(R.id.tv_back_up_date)).setText(backupDate);
        ((FontTextView) view.findViewById(R.id.tv_back_up_app_version)).setText(backupAppVersion);
        ((FontTextView) view.findViewById(R.id.tv_templates)).setText(templatesSum);
        ((FontTextView) view.findViewById(R.id.tv_contracts)).setText(contractsSum);
        ((FontTextView) view.findViewById(R.id.tv_tokens)).setText(tokensSum);
        view.findViewById(R.id.bt_restore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onRestoreClick();
            }
        });
        view.findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRestoreDialog.dismiss();
            }
        });
        mRestoreDialog = new AlertDialog
                .Builder(getContext())
                .setView(view)
                .create();

        if (mRestoreDialog.getWindow() != null) {
            mRestoreDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        mRestoreDialog.setCanceledOnTouchOutside(false);
        mRestoreDialog.show();
    }

    RestoreContractsPresenterImpl.RestoreDialogCallBack callback = new RestoreContractsPresenterImpl.RestoreDialogCallBack() {
        @Override
        public void onRestoreClick() {
            setProgressDialog();
            s = getPresenter().createBackupData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            mRestoreDialog.dismiss();
                            if (aBoolean) {
                                setAlertDialog(getString(R.string.restored_successfully), "", "OK", BaseFragment.PopUpType.confirm, new BaseFragment.AlertDialogCallBack() {
                                    @Override
                                    public void onButtonClick() {
                                        FragmentManager fm = RestoreContractsFragment.this.getFragmentManager();
                                        int count = fm.getBackStackEntryCount() - 2;
                                        for (int i = 0; i < count; ++i) {
                                            fm.popBackStack();
                                        }
                                    }

                                    @Override
                                    public void onButton2Click() {

                                    }
                                });
                            } else {
                                setAlertDialog(getString(R.string.something_went_wrong), "", "OK", BaseFragment.PopUpType.error, getAlertCallback());
                            }
                        }
                    });
        }
    };

    @Override
    public AlertDialogCallBack getAlertCallback() {
        return new BaseFragment.AlertDialogCallBack() {
            @Override
            public void onButtonClick() {
                getPresenter().onDeleteFileClick();
                if (mRestoreFile != null) {
                    mRestoreFile.delete();
                    mRestoreFile = null;
                }
            }

            @Override
            public void onButton2Click() {

            }
        };
    }

    @Override
    public File getRestoreFile() {
        return mRestoreFile;
    }

    @Override
    public void subscribeTokenBalanceChange(String contractAddress) {
        mUpdateService.subscribeTokenBalanceChange(contractAddress);
    }

}