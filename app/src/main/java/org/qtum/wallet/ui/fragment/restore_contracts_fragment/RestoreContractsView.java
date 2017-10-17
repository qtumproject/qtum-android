package org.qtum.wallet.ui.fragment.restore_contracts_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.io.File;


public interface RestoreContractsView extends BaseFragmentView {
    void setFile(String name, String size);

    void deleteFile();

    void showRestoreDialogFragment(String dateCreate, String fileVersion, String templatesCount, String contractsCount, String tokensCount);

    BaseFragment.AlertDialogCallBack getAlertCallback();

    File getRestoreFile();

    void subscribeTokenBalanceChange(String contractAddress);
}
