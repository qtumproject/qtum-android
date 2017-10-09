package org.qtum.wallet.ui.fragment.addresses_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;


public class AddressesPresenterImpl extends BaseFragmentPresenterImpl implements AddressesPresenter {

    private AddressesView mAddressesFragmentView;
    private AddressesInteractor mAddressesFragmentInteractor;

    public AddressesPresenterImpl(AddressesView addressesFragmentView, AddressesInteractor addressesFragmentInteractor){
        mAddressesFragmentView = addressesFragmentView;
        mAddressesFragmentInteractor = addressesFragmentInteractor;
    }

    @Override
    public AddressesView getView() {
        return mAddressesFragmentView;
    }

    private AddressesInteractor getInteractor() {
        return mAddressesFragmentInteractor;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().updateAddressList(getInteractor().getKeyList());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().setAdapterNull();
    }
}
