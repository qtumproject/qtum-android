package org.qtum.wallet.ui.fragment.confirm_passphrase_fragment;



import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.Arrays;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ConfirmPassphrasePresenterImpl extends BaseFragmentPresenterImpl implements ConfirmPassphrasePresenter{

    ConfirmPassphraseView mConfirmPassphraseView;
    ConfirmPassphraseInteractor mConfirmPassphraseInteractor;
    boolean isDataLoaded = false;

    List<String> wordsList;

    ConfirmPassphrasePresenterImpl(ConfirmPassphraseView confirmPassphraseView, ConfirmPassphraseInteractor confirmPassphraseInteractor){
        mConfirmPassphraseView = confirmPassphraseView;
        mConfirmPassphraseInteractor = confirmPassphraseInteractor;
    }

    @Override
    public ConfirmPassphraseView getView() {
        return mConfirmPassphraseView;
    }

    public ConfirmPassphraseInteractor getInteractor() {
        return mConfirmPassphraseInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        String seed = getView().getSeed();
        wordsList = Arrays.asList(seed.split("\\W+"));
        getView().setUpRecyclerViews(wordsList);
    }

    @Override
    public void onResetAllClick(){
        getView().hideError();
        getView().resetAll(wordsList);
    }

    @Override
    public void seedEntered(List<String> seed) {
        if(seed.size()==12){
            for(int i=0;i<12;i++){
                if(!seed.get(i).equals(wordsList.get(i))){
                    getView().showError();
                    return;
                }
            }
            confirmSeed();
        }else{
            getView().hideError();
        }
    }

    private void confirmSeed(){
        getView().setProgressDialog();
        String passphrase = getView().getSeed();
        getInteractor().createWallet(passphrase)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        getInteractor().setKeyGeneratedInstance(true);
                        isDataLoaded = true;
                        getView().onLogin();
                        getView().dismissProgressDialog();
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(isDataLoaded){
            getView().dismissProgressDialog();
            getView().onLogin();
        }
    }
}
