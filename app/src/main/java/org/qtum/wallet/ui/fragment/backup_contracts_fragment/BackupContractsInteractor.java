package org.qtum.wallet.ui.fragment.backup_contracts_fragment;

import java.io.File;
import java.math.BigDecimal;

import rx.Observable;

public interface BackupContractsInteractor {
    Observable<File> createBackUpFile();
}
