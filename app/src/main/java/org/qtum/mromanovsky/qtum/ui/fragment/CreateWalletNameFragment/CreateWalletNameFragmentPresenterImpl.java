package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment;

import org.qtum.mromanovsky.qtum.R;
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
        if(name.isEmpty()){
            getView().incorrectName(getView().getContext().getString(R.string.empty_name));
        } else {
            QtumSharedPreference.getInstance().saveWalletName(getView().getContext(), name);
            PinFragment pinFragment = PinFragment.newInstance(PinFragment.CREATING);
            getView().openFragment(pinFragment);
        }
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
