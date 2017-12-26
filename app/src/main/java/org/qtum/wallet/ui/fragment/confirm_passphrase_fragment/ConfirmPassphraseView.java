package org.qtum.wallet.ui.fragment.confirm_passphrase_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;


public interface ConfirmPassphraseView extends BaseFragmentView{
    String getSeed();
    void setUpRecyclerViews(List<String> seed);
}
