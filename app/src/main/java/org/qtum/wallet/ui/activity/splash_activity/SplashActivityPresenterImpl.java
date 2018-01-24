package org.qtum.wallet.ui.activity.splash_activity;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.ui.base.base_activity.BasePresenterImpl;
import org.qtum.wallet.utils.migration_manager.KeystoreMigrationResult;
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
        if(MigrationManager.migrateFromKeystore(getView().getContext())
                .equalsName(KeystoreMigrationResult.ERROR.name())) {
            clearWallet();
        }
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

    public void clearWallet() {
        QtumSharedPreference.getInstance().forceClear(getView().getContext());
        KeyStorage.getInstance().clearKeyStorage();
        HistoryList.getInstance().clearHistoryList();
        TinyDB db = new TinyDB(getView().getContext());
        db.clearTokenList();
        db.clearContractList();
        db.clearTemplateList();
    }
}
