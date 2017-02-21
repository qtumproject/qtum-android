package org.qtum.mromanovsky.qtum.ui.fragment.AddressesFragment;


import android.content.Context;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


class AddressesFragmentPresenterImpl extends BaseFragmentPresenterImpl implements AddressesFragmentPresenter{

    private AddressesFragmentView mAddressesFragmentView;
    private AddressesFragmentInteractorImpl mAddressesFragmentInteractor;

    AddressesFragmentPresenterImpl(AddressesFragmentView addressesFragmentView){
        mAddressesFragmentView = addressesFragmentView;
        mAddressesFragmentInteractor = new AddressesFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public AddressesFragmentView getView() {
        return mAddressesFragmentView;
    }

    public AddressesFragmentInteractorImpl getInteractor() {
        return mAddressesFragmentInteractor;
    }

    @Override
    public void onResume(Context context) {
        super.onResume(context);
        getView().updateAddressList(getInteractor().getKeyList());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setAdapterNull();
    }
}
