package com.pixelplex.qtum.ui.fragment.RestoreContractsFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;


public interface RestoreContractsFragmentView extends BaseFragmentView{
    void setFile(String name, String size);
    void deleteFile();
}
