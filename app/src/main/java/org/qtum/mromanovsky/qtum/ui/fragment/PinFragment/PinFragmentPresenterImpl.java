package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.utils.QtumSharedPreference;


public class PinFragmentPresenterImpl extends BaseFragmentPresenterImpl implements PinFragmentPresenter {

    private PinFragmentView mPinFragmentView;
    private PinFragmentInteractorImpl mPinFragmentInteractor;

    public PinFragmentPresenterImpl(PinFragmentView pinFragmentView){
        mPinFragmentView = pinFragmentView;
        mPinFragmentInteractor = new PinFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public void confirm(String password, boolean isCreating) {
        int intPassword;
        if(password.length() < 4){
            getView().confirmError(getView().getContext().getString(R.string.invalid_pin));
        } else {
            intPassword = Integer.parseInt(password);
            if(isCreating){
                getInteractor().savePassword(intPassword);
                getView().confirm();
            } else if(intPassword == getInteractor().getPassword()){
                getView().confirm();
            } else {
                getView().confirmError(getView().getContext().getString(R.string.incorrect_password));
            }
        }
    }

    @Override
    public void cancel() {
        getView().finish();
    }

    @Override
    public PinFragmentView getView() {
        return mPinFragmentView;
    }

    public PinFragmentInteractorImpl getInteractor() {
        return mPinFragmentInteractor;
    }
}
