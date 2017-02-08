package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment.CreateWalletNameFragment;


public class ImportWalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ImportWalletFragmentPresenter {

    ImportWalletFragmentView mImportWalletFragmentView;
    ImportWalletFragmentInteractorImpl mImportWalletFragmentInteractor;

    public ImportWalletFragmentPresenterImpl(ImportWalletFragmentView importWalletFragmentView) {
        mImportWalletFragmentView = importWalletFragmentView;
        mImportWalletFragmentInteractor = new ImportWalletFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public ImportWalletFragmentView getView() {
        return mImportWalletFragmentView;
    }

    public ImportWalletFragmentInteractorImpl getInteractor() {
        return mImportWalletFragmentInteractor;
    }

    @Override
    public void cancel() {
        getView().getFragmentActivity().onBackPressed();
    }

    @Override
    public void onImportClick(String brainCode) {
        getView().setProgressDialog("Importing wallet");
        getView().hideKeyBoard();
        getInteractor().importWallet(brainCode, new ImportWalletFragmentInteractorImpl.ImportWalletCallBack() {
            @Override
            public void onSuccess() {
                CreateWalletNameFragment createWalletNameFragment = CreateWalletNameFragment.newInstance(false);
                getView().openFragment(createWalletNameFragment);
                getView().dismissProgressDialog();
            }
        });
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
    }
}
