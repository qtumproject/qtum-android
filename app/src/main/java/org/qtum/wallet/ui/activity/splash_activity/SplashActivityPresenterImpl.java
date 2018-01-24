package org.qtum.wallet.ui.activity.splash_activity;

import org.qtum.wallet.ui.base.base_activity.BasePresenterImpl;
import org.qtum.wallet.utils.migration_manager.MigrationManager;

public class SplashActivityPresenterImpl extends BasePresenterImpl implements SplashActivityPresenter {

    private SplashActivityView mMainActivityView;
    private SplashActivityInteractor mMainActivityInteractor;

    public SplashActivityPresenterImpl(SplashActivityView mainActivityView, SplashActivityInteractor splashActivityInteractor) {
        mMainActivityView = mainActivityView;
        mMainActivityInteractor = splashActivityInteractor;
    }

    @Override
    public void initializeViews() {
        MigrationManager.migratePassphrase(getView().getContext());
        getInteractor().migrateDefaultContracts();
        getView().initializeViews();
    }

    @Override
    public SplashActivityView getView() {
        return mMainActivityView;
    }

    public SplashActivityInteractor getInteractor() {
        return mMainActivityInteractor;
    }
}
