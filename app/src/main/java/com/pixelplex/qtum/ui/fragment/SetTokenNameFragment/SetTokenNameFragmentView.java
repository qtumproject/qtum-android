package com.pixelplex.qtum.ui.fragment.SetTokenNameFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;


interface SetTokenNameFragmentView extends BaseFragmentView{
    void setError(String nameError, String symbolError);
    void clearError();
    void setData(String name,String symbol);
}
