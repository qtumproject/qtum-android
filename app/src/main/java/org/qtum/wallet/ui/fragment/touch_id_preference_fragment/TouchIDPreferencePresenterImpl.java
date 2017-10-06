package org.qtum.wallet.ui.fragment.touch_id_preference_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class TouchIDPreferencePresenterImpl extends BaseFragmentPresenterImpl implements TouchIDPreferencePresenter {

    private TouchIDPreferenceView mTouchIDPreferenceView;
    private TouchIDInterractor mTouchIDPreferenceInteractor;

    public TouchIDPreferencePresenterImpl(TouchIDPreferenceView view, TouchIDInterractor interractor) {
        mTouchIDPreferenceView = view;
        mTouchIDPreferenceInteractor = interractor;
    }

    @Override
    public void onEnableTouchIdClick() {
        getInteractor().saveTouchIDEnabled();
    }

    @Override
    public TouchIDPreferenceView getView() {
        return mTouchIDPreferenceView;
    }

    public TouchIDInterractor getInteractor() {
        return mTouchIDPreferenceInteractor;
    }
}
