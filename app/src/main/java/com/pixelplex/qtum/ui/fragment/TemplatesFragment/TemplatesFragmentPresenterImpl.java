package com.pixelplex.qtum.ui.fragment.TemplatesFragment;

import android.content.Context;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.SetYourTokenFragment.SetYourTokenFragment;


public class TemplatesFragmentPresenterImpl extends BaseFragmentPresenterImpl implements TemplatesFragmentPresenter {

    private final TemplatesFragmentView view;
    private final Context mContext;
    private final TemplatesFragmentInteractorImpl interactor;

    TemplatesFragmentPresenterImpl(TemplatesFragmentView view) {
        this.view = view;
        mContext = getView().getContext();
        interactor = new TemplatesFragmentInteractorImpl();
    }

    @Override
    public TemplatesFragmentView getView() {
        return view;
    }

    public void openConstructorByName(String uiid) {
        SetYourTokenFragment fragment = SetYourTokenFragment.newInstance(uiid);
        getView().openFragment(fragment);
    }
}
