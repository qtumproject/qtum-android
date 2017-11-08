package org.qtum.wallet.ui.fragment.about_fragment;


import android.content.pm.PackageManager;

import org.qtum.wallet.R;
import org.qtum.wallet.model.Version;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class AboutPresenterImpl extends BaseFragmentPresenterImpl implements AboutPresenter{

    private AboutView mAboutFragmentView;
    private AboutInteractor mAboutInteractor;

    public AboutPresenterImpl(AboutView aboutFragmentView, AboutInteractor aboutInteractor){
        mAboutFragmentView = aboutFragmentView;
        mAboutInteractor = aboutInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        try {
            int versionCode = getInteractor().getCodeVersion();
            String version = getInteractor().getVersion();
            getView().updateVersion(new Version(version, versionCode));
        } catch (Exception e) {
            getView().setAlertDialog(R.string.receive_version_error, R.string.cancel, BaseFragment.PopUpType.error);
        }

    }

    @Override
    public AboutView getView() {
        return mAboutFragmentView;
    }

    private AboutInteractor getInteractor(){
        return mAboutInteractor;
    }
}
