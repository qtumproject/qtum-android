package org.qtum.wallet.ui.fragment.backup_contracts_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.io.File;

public interface BackupContractsView extends BaseFragmentView {
    void setUpFile(String fileSize);

    void checkPermissionForCreateFile();

    void checkPermissionForBackupFile();

    void chooseShareMethod(String authority, File file);
}
