package org.qtum.wallet.ui.fragment.confirm_passphrase_fragment;



import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.Arrays;
import java.util.List;


public class ConfirmPassphrasePresenterImpl extends BaseFragmentPresenterImpl implements ConfirmPassphrasePresenter{

    ConfirmPassphraseView mConfirmPassphraseView;
    ConfirmPassphraseInteractor mConfirmPassphraseInteractor;

    List<String> wordsList;

    ConfirmPassphrasePresenterImpl(ConfirmPassphraseView confirmPassphraseView, ConfirmPassphraseInteractor confirmPassphraseInteractor){
        mConfirmPassphraseView = confirmPassphraseView;
        mConfirmPassphraseInteractor = confirmPassphraseInteractor;
    }

    @Override
    public ConfirmPassphraseView getView() {
        return mConfirmPassphraseView;
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
            getView().confirmSeed();
        }else{
            getView().hideError();
        }
    }
}
