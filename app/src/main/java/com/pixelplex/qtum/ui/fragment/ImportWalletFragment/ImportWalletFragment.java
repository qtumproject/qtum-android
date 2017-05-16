package com.pixelplex.qtum.ui.fragment.ImportWalletFragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class ImportWalletFragment extends BaseFragment implements ImportWalletFragmentView {

    public final int LAYOUT = R.layout.fragment_import_wallet;

    ImportWalletFragmentPresenterImpl mImportWalletFragmentPresenter;

    @BindView(R.id.bt_cancel)
    Button mButtonCancel;
    @BindView(R.id.bt_import)
    Button mButtonImport;
    @BindView(R.id.et_your_brain_code)
    EditText mEditTextYourBrainCode;

    @OnClick({R.id.bt_cancel, R.id.bt_import})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                getPresenter().onCancelClick();
                break;
            case R.id.bt_import:
                getPresenter().onImportClick(mEditTextYourBrainCode.getText().toString());
                break;
        }
    }

    public static ImportWalletFragment newInstance() {

        Bundle args = new Bundle();

        ImportWalletFragment fragment = new ImportWalletFragment();
        fragment.setArguments(args);
        return fragment;
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
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
