package com.pixelplex.qtum.ui.fragment.create_wallet_name_fragment;


import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.pin_fragment.PinFragment;


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
            BaseFragment pinFragment;
            if (getView().isCreating()) {
                pinFragment = PinFragment.newInstance(PinFragment.CREATING, getView().getContext());
            } else {
                if (CreateWalletNameFragment.mIsCreateNew) {
                    pinFragment = PinFragment.newInstance(PinFragment.CREATING, getView().getContext());
                } else {
                    pinFragment = PinFragment.newInstance(PinFragment.IMPORTING, getView().getPassphrase(), getView().getContext());
                }
            }
            getView().openFragment(pinFragment);
        }
    }

        @Override
        public void onCancelClick () {
            getView().getMainActivity().onBackPressed();
        }

        @Override
        public CreateWalletNameFragmentView getView () {
            return mCreateWalletNameFragmentView;
        }

    }
