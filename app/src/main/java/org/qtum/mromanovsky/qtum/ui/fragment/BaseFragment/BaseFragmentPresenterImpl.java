package org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment;


import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BasePresenterImpl;


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
