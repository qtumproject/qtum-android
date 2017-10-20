package org.qtum.wallet.ui.fragment.restore_contracts_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

import rx.Observable;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public interface RestoreContractsPresenter extends BaseFragmentPresenter {
    void onDeleteFileClick();

    void onRestoreClick(boolean restoreTemplates, boolean restoreContracts, boolean restoreTokens);

    Observable<Boolean> createBackupData();

}
