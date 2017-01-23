package org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BasePresenterImpl;


public class BaseFragmentPresenterImpl extends BasePresenterImpl implements BaseFragmentPresenter {

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        getView().getFragmentActivity().getWindow().setStatusBarColor(((BaseFragmentView) getView()).getFragmentActivity().getResources().getColor(R.color.colorPrimaryDark));
        getView().startAnimation();
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
        getView().stopAnimation();
    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public BaseFragmentView getView() {
        return (BaseFragmentView) super.getView();
    }
}
