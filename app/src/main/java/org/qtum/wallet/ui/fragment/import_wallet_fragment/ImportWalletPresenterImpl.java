package org.qtum.wallet.ui.fragment.import_wallet_fragment;


import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.IMPORTING;


public class ImportWalletPresenterImpl extends BaseFragmentPresenterImpl implements ImportWalletPresenter {

    private ImportWalletView mImportWalletFragmentView;
    private ImportWalletInteractor mImportWalletFragmentInteractor;
    private boolean isDataLoaded = false;
    private String mPassphrase;
    private Subscription mSubscription;

    public ImportWalletPresenterImpl(ImportWalletView importWalletFragmentView, ImportWalletInteractor importWalletInteractor) {
        mImportWalletFragmentView = importWalletFragmentView;
        mImportWalletFragmentInteractor = importWalletInteractor;
    }

    @Override
    public ImportWalletView getView() {
        return mImportWalletFragmentView;
    }

    private ImportWalletInteractor getInteractor() {
        return mImportWalletFragmentInteractor;
    }

    @Override
    public void onImportClick(String brainCode) {

        getView().setProgressDialog();
        getView().hideKeyBoard();
        isDataLoaded = true;
        mSubscription = getInteractor().importWallet(brainCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setAlertDialog(R.string.error,R.string.cancel, BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(String passphrase) {
                        mPassphrase = passphrase;
                        getView().dismissProgressDialog();
                        isDataLoaded = false;
                        getView().openPinFragment(mPassphrase, IMPORTING);
                    }
                });
    }

    @Override
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
    public void onResume() {
        super.onResume();
        if (isDataLoaded) {
            getView().dismissProgressDialog();
            isDataLoaded = false;
            getView().openPinFragment(mPassphrase, IMPORTING);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mSubscription!=null){
            mSubscription.unsubscribe();
        }
    }

    //setter for unit-testing
    public void setDataLoaded(boolean dataLoaded) {
        isDataLoaded = dataLoaded;
    }
}