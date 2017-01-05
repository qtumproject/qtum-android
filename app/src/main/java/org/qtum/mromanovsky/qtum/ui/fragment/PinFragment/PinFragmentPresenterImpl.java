package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.utils.QtumSharedPreference;


public class PinFragmentPresenterImpl extends BaseFragmentPresenterImpl implements PinFragmentPresenter {

    private PinFragmentView mPinFragmentView;

    public PinFragmentPresenterImpl(PinFragmentView pinFragmentView){
        mPinFragmentView = pinFragmentView;
    }

    @Override
    public void confirm(String password, boolean isCreating) {
        int intPassword;
        if(password.length() < 4){
            getView().confirmError();
        } else {
            intPassword = Integer.parseInt(password);
            if(isCreating){
                QtumSharedPreference.getInstance().saveWalletPassword(getView().getContext(),intPassword);
                getView().confirm();
            } else if(intPassword == QtumSharedPreference.getInstance().getWalletPassword(getView().getContext())){
                getView().confirm();
            }
        }
    }

    @Override
    public PinFragmentView getView() {
        return mPinFragmentView;
    }
}
