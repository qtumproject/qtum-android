package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;
import org.qtum.mromanovsky.qtum.utils.QtumSharedPreference;


public class CreateWalletNameFragmentPresenterImpl extends BaseFragmentPresenterImpl implements CreateWalletNameFragmentPresenter {

    private CreateWalletNameFragmentView mCreateWalletNameFragmentView;

    public CreateWalletNameFragmentPresenterImpl(CreateWalletNameFragmentView createWalletNameFragmentView){
        mCreateWalletNameFragmentView = createWalletNameFragmentView;
    }

    @Override
    public void confirm(String name) {
        QtumSharedPreference.getInstance().saveWalletName(getView().getContext(),name);
        PinFragment pinFragment = PinFragment.newInstance(true);
        getView().openFragment(pinFragment);
    }

    @Override
    public void cancel() {
        getView().finish();
    }

    @Override
    public CreateWalletNameFragmentView getView() {
        return mCreateWalletNameFragmentView;
    }
}
