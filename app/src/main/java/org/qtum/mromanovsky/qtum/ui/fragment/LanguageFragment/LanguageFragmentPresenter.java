package org.qtum.mromanovsky.qtum.ui.fragment.LanguageFragment;

import org.qtum.mromanovsky.qtum.datastorage.LanguageChangeListener;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


class LanguageFragmentPresenter extends BaseFragmentPresenterImpl{

    private LanguageFragmentView mLanguageFragmentView;
    private LanguageFragmentInteractor mLanguageFragmentInteractor;
    private LanguageChangeListener mLanguageChangeListener;

    LanguageFragmentPresenter(LanguageFragmentView languageFragmentView){
        mLanguageFragmentView = languageFragmentView;
        mLanguageFragmentInteractor = new LanguageFragmentInteractor(getView().getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        QtumSharedPreference.getInstance().removeLanguageListener(mLanguageChangeListener);
    }

    @Override
    public LanguageFragmentView getView() {
        return mLanguageFragmentView;
    }

    private LanguageFragmentInteractor getInteractor(){
        return mLanguageFragmentInteractor;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().setUpLanguagesList(getInteractor().getLanguagesList());
        mLanguageChangeListener = new LanguageChangeListener() {
            @Override
            public void onLanguageChange() {
                getView().resetText();
            }
        };
        QtumSharedPreference.getInstance().addLanguageListener(mLanguageChangeListener);
    }

    public String getCurrentLanguage(){
        return getInteractor().getLanguage();
    }

    public void setCurrentLanguage(String language){
        getInteractor().setLanguage(language);
    }

}
