package org.qtum.wallet.ui.fragment.confirm_passphrase_fragment;



import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.Arrays;
import java.util.List;


public class ConfirmPassphrasePresenterImpl extends BaseFragmentPresenterImpl implements ConfirmPassphrasePresenter{

    ConfirmPassphraseView mConfirmPassphraseView;
    ConfirmPassphraseInteractor mConfirmPassphraseInteractor;

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
        List<String> wordsList = Arrays.asList(seed.split("\\W+"));
        getView().setUpRecyclerViews(wordsList);
    }


}
