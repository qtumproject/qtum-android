package com.pixelplex.qtum.ui.fragment.restore_contracts_fragment;

import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentView;


public interface RestoreContractsFragmentView extends BaseFragmentView{
    void setFile(String name, String size);
    void deleteFile();
}
