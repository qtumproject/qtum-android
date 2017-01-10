package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;


public class ImportWalletFragment extends BaseFragment implements ImportWalletFragmentView {

    public static final int LAYOUT = R.layout.fragment_import_wallet;

    ImportWalletFragmentPresenterImpl mImportWalletFragmentPresenter;

    public static ImportWalletFragment newInstance() {
        ImportWalletFragment importWalletFragment = new ImportWalletFragment();
        return importWalletFragment;
    }

    @Override
    protected void createPresenter() {
        mImportWalletFragmentPresenter = new ImportWalletFragmentPresenterImpl(this);
    }

    @Override
    protected ImportWalletFragmentPresenterImpl getPresenter() {
        return mImportWalletFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {

    }
}
