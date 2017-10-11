package org.qtum.wallet.ui.fragment.import_wallet_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;


public interface ImportWalletView extends BaseFragmentView {
    void enableImportButton();
    void disableImportButton();
    void openPinFragment(String action, String passphrase);
}
