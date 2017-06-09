package com.pixelplex.qtum.ui.fragment.ImportWalletFragment;

import android.content.Context;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.CreateWalletNameFragment.CreateWalletNameFragment;


class ImportWalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ImportWalletFragmentPresenter {

    private ImportWalletFragmentView mImportWalletFragmentView;
    private ImportWalletFragmentInteractorImpl mImportWalletFragmentInteractor;

    ImportWalletFragmentPresenterImpl(ImportWalletFragmentView importWalletFragmentView) {
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
    public void onCancelClick() {
        getView().getFragmentActivity().onBackPressed();
    }

    @Override
    public void onImportClick(String brainCode) {
        getView().setProgressDialog();
        getView().hideKeyBoard();
        getInteractor().importWallet(brainCode, new ImportWalletFragmentInteractorImpl.ImportWalletCallBack() {
            @Override
            public void onSuccess() {
                CreateWalletNameFragment createWalletNameFragment = CreateWalletNameFragment.newInstance(false);
                getView().openRootFragment(createWalletNameFragment);
                getView().dismissProgressDialog();
                ImportWalletFragmentInteractorImpl.isDataLoaded = false;
            }
        });
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        if (ImportWalletFragmentInteractorImpl.isDataLoaded) {
            CreateWalletNameFragment createWalletNameFragment = CreateWalletNameFragment.newInstance(false);
            getView().openRootFragment(createWalletNameFragment);
            getView().dismissProgressDialog();
            ImportWalletFragmentInteractorImpl.isDataLoaded = false;
        }
    }

}
