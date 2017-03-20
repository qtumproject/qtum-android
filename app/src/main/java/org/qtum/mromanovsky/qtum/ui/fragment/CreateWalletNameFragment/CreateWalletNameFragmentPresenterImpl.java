package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment;


import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;


class CreateWalletNameFragmentPresenterImpl extends BaseFragmentPresenterImpl implements CreateWalletNameFragmentPresenter {

    private CreateWalletNameFragmentView mCreateWalletNameFragmentView;
    private CreateWalletNameFragmentInteractorImpl mCreateWalletNameFragmentInteractor;

    CreateWalletNameFragmentPresenterImpl(CreateWalletNameFragmentView createWalletNameFragmentView) {
        mCreateWalletNameFragmentView = createWalletNameFragmentView;
        mCreateWalletNameFragmentInteractor = new CreateWalletNameFragmentInteractorImpl(getView().getContext());
    }

    public CreateWalletNameFragmentInteractorImpl getInteractor() {
        return mCreateWalletNameFragmentInteractor;
    }

    @Override
    public void onConfirmClick(String name) {
        if (name.isEmpty()) {
            getView().setErrorText(getView().getContext().getString(R.string.empty_name));
        } else {
            getInteractor().saveWalletName(name);
            getView().clearError();
            PinFragment pinFragment;
            if(CreateWalletNameFragment.mIsCreateNew) {
                pinFragment = PinFragment.newInstance(PinFragment.CREATING);
            } else {
                pinFragment = PinFragment.newInstance(PinFragment.IMPORTING);

            }
            getView().openFragment(pinFragment);
        }
    }

    @Override
    public void onCancelClick() {
        getView().getFragmentActivity().onBackPressed();
    }

    @Override
    public CreateWalletNameFragmentView getView() {
        return mCreateWalletNameFragmentView;
    }

}
