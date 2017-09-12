package org.qtum.wallet.ui.fragment.import_wallet_fragment;

import android.content.Context;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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

    private ImportWalletFragmentInteractorImpl getInteractor() {
        return mImportWalletFragmentInteractor;
    }

    @Override
    public void onCancelClick() {
        getView().getMainActivity().onBackPressed();
    }

    @Override
    public void onImportClick(String brainCode) {

        getView().setProgressDialog();
        getView().hideKeyBoard();
        getInteractor().importWallet(brainCode, new ImportWalletFragmentInteractorImpl.ImportWalletCallBack() {
            @Override
            public void onSuccess() {
                BaseFragment pinfragment = PinFragment.newInstance(PinFragment.IMPORTING, ImportWalletFragmentInteractorImpl.sPassphrase, getView().getContext());
                getView().openRootFragment(pinfragment);
                getView().dismissProgressDialog();
                ImportWalletFragmentInteractorImpl.isDataLoaded = false;
            }
        });
    }

    public void onPassphraseChange(String passphrase){
        if(validatePassphrase(passphrase)){
            getView().enableImportButton();
        }else{
            getView().disableImportButton();
        }
    }

    private boolean validatePassphrase(String passphrase){
        passphrase = passphrase.trim().replaceAll("[\\s]{2,}", " ");
        Pattern p = Pattern.compile(" ");
        Matcher m = p.matcher(passphrase);
        int counter = 0;
        while(m.find()) {
            counter++;
        }
        if(counter!=11){
            return false;
        }
        char[] chars = passphrase.replaceAll(" ", "").toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        if (ImportWalletFragmentInteractorImpl.isDataLoaded) {
            BaseFragment pinFragment = PinFragment.newInstance(PinFragment.IMPORTING, ImportWalletFragmentInteractorImpl.sPassphrase, getView().getContext());
            getView().openRootFragment(pinFragment);
            getView().dismissProgressDialog();
            ImportWalletFragmentInteractorImpl.isDataLoaded = false;
        }
    }

}
