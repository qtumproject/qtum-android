package com.pixelplex.qtum.ui.fragment.RestoreContractsFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

/**
 * Created by max-v on 6/14/2017.
 */

public interface RestoreContractsFragmentView extends BaseFragmentView{
    void setFile(String name, String size);
    void deleteFile();
}
