package org.qtum.wallet.ui.fragment.import_wallet_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;


interface ImportWalletFragmentView extends BaseFragmentView {
    void enableImportButton();
    void disableImportButton();
}
