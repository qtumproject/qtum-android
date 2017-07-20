package com.pixelplex.qtum.ui.fragment.CreateWalletNameFragment;


import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinFragment;


class CreateWalletNameFragmentPresenterImpl extends BaseFragmentPresenterImpl implements CreateWalletNameFragmentPresenter {

    private CreateWalletNameFragmentView mCreateWalletNameFragmentView;
    private CreateWalletNameFragmentInteractorImpl mCreateWalletNameFragmentInteractor;

    CreateWalletNameFragmentPresenterImpl(CreateWalletNameFragmentView createWalletNameFragmentView) {
        mCreateWalletNameFragmentView = createWalletNameFragmentView;
        mCreateWalletNameFragmentInteractor = new CreateWalletNameFragmentInteractorImpl(getView().getContext());
    }

    private CreateWalletNameFragmentInteractorImpl getInteractor() {
        return mCreateWalletNameFragmentInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getView().showSoftInput();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().hideKeyBoard();
    }

    @Override
    public void onConfirmClick(String name) {
        if (name.isEmpty()) {
            getView().setErrorText(getView().getContext().getString(R.string.empty_name));
        } else {
            getInteractor().saveWalletName(name);
            getView().clearError();
            PinFragment pinFragment;
            if(getView().isCreating()) {
                pinFragment = PinFragment.newInstance(PinFragment.CREATING);
            } else {
                pinFragment = PinFragment.newInstance(PinFragment.IMPORTING,getView().getPassphrase());

            }
            getView().openFragment(pinFragment);
        }
    }

    @Override
    public void onCancelClick() {
        getView().getMainActivity().onBackPressed();
    }

    @Override
    public CreateWalletNameFragmentView getView() {
        return mCreateWalletNameFragmentView;
    }

}
