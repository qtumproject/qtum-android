package org.qtum.wallet.ui.fragment.backup_contracts_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.io.File;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


class BackupContractsPresenterImpl extends BaseFragmentPresenterImpl implements BackupContractsPresenter {

    private BackupContractsView mBackupContractsFragmentView;
    private BackupContractsInteractor mBackupContractsInteractor;

    BackupContractsPresenterImpl(BackupContractsView backupContractsFragmentView, BackupContractsInteractor backupContractsInteractor) {
        mBackupContractsFragmentView = backupContractsFragmentView;
        mBackupContractsInteractor = backupContractsInteractor;
    }

    private File mBackUpFile;

    @Override
    public BackupContractsView getView() {
        return mBackupContractsFragmentView;
    }

    @Override
    public void onBackUpClick() {
        getView().checkPermissionForBackupFile();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBackUpFile != null) {
            mBackUpFile.delete();
        }
    }

    public void permissionGrantedForCreateBackUpFile() {
        getView().setProgressDialog();
        getInteractor().createBackUpFile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(File file) {
                        String backUpFileSize = String.valueOf((int) Math.ceil(file.length() / 1024.0)) + " Kb";
                        getView().dismissProgressDialog();
                        getView().setUpFile(backUpFileSize);
                        mBackUpFile = file;
                    }
                });

    }

    public void permissionGrantedForCreateAndBackUpFile() {
        getView().setProgressDialog();
        getInteractor().createBackUpFile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(File file) {
                        String backUpFileSize = String.valueOf((int) Math.ceil(file.length() / 1024.0)) + " Kb";
                        getView().dismissProgressDialog();
                        getView().setUpFile(backUpFileSize);
                        mBackUpFile = file;

                        getView().checkPermissionForBackupFile();
                    }
                });

    }

    public void permissionGrantedForChooseShareMethod() {
        if (mBackUpFile.exists()) {
            getView().chooseShareMethod(mBackUpFile.getAbsolutePath());
        }
    }

    private BackupContractsInteractor getInteractor() {
        return mBackupContractsInteractor;
    }

}