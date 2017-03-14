package org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BasePresenterImpl;


public class BaseFragmentPresenterImpl extends BasePresenterImpl implements BaseFragmentPresenter {

    @Override
    public void onViewCreated() {

    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public BaseFragmentView getView() {
        return (BaseFragmentView) super.getView();
    }
}
