package org.qtum.wallet.ui.fragment.backup_contracts_fragment;

import android.support.v4.content.FileProvider;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.io.File;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class BackupContractsPresenterImpl extends BaseFragmentPresenterImpl implements BackupContractsPresenter {

    private BackupContractsView mBackupContractsFragmentView;
    private BackupContractsInteractor mBackupContractsInteractor;

    public BackupContractsPresenterImpl(BackupContractsView backupContractsFragmentView, BackupContractsInteractor backupContractsInteractor) {
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
                        getView().setAlertDialog(R.string.error, R.string.cancel, BaseFragment.PopUpType.error);
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
                        getView().setAlertDialog(R.string.error, R.string.cancel, BaseFragment.PopUpType.error);
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

    @Override
    public void permissionGrantedForChooseShareMethod() {
        if (mBackUpFile.exists()) {
            String authority = "org.qtum.wallet.FileProvider";
            getView()
                    .chooseShareMethod(
                            FileProvider.getUriForFile(
                                    getView().getContext(),
                                    authority,
                                    mBackUpFile).toString());
        } else {
            getView().setAlertDialog(R.string.error, R.string.cancel, BaseFragment.PopUpType.error);
        }
    }

    public void setBackUpFile(File backUpFile) {
        mBackUpFile = backUpFile;
    }

    private BackupContractsInteractor getInteractor() {
        return mBackupContractsInteractor;
    }

}