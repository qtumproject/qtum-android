package org.qtum.wallet.ui.fragment.restore_contracts_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;


public interface RestoreContractsFragmentView extends BaseFragmentView {
    void setFile(String name, String size);
    void deleteFile();
}
