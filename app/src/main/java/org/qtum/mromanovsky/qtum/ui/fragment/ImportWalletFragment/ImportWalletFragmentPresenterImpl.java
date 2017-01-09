package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;

import org.qtum.mromanovsky.qtum.ui.activity.BaseActivity.BaseContextView;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

/**
 * Created by Romanovsky_m on 08.01.2017.
 */

public class ImportWalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ImportWalletFragmentPresenter {
    ImportWalletFragmentView mImportWalletFragmentView;

    public ImportWalletFragmentPresenterImpl(ImportWalletFragmentView importWalletFragmentView){
        mImportWalletFragmentView = importWalletFragmentView;;
    }

    @Override
    public ImportWalletFragmentView getView() {
        return mImportWalletFragmentView;
    }
}
