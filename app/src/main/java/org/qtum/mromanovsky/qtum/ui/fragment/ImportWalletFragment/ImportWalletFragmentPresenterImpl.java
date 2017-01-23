package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


public class ImportWalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ImportWalletFragmentPresenter {
    ImportWalletFragmentView mImportWalletFragmentView;

    public ImportWalletFragmentPresenterImpl(ImportWalletFragmentView importWalletFragmentView) {
        mImportWalletFragmentView = importWalletFragmentView;
        ;
    }

    @Override
    public ImportWalletFragmentView getView() {
        return mImportWalletFragmentView;
    }

    @Override
    public void cancel() {
        getView().getFragmentActivity().onBackPressed();
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
        getView().hideKeyBoard();
    }
}
