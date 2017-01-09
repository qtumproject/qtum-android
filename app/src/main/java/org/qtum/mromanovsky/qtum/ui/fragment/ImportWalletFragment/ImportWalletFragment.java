package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;


public class ImportWalletFragment extends BaseFragment implements ImportWalletFragmentView {

    public static final int  LAYOUT = R.layout.fragment_import_wallet;

    ImportWalletFragmentPresenterImpl mImportWalletFragmentPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    public static ImportWalletFragment newInstance(){
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
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
