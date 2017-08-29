package com.pixelplex.qtum.ui.base.base_fragment;


import com.pixelplex.qtum.ui.base.base_activity.BasePresenterImpl;


public class BaseFragmentPresenterImpl extends BasePresenterImpl implements BaseFragmentPresenter {

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public BaseFragmentView getView() {
        return (BaseFragmentView) super.getView();
    }
}
