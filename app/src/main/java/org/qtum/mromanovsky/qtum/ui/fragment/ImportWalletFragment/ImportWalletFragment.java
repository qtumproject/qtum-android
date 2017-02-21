package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

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

    @OnClick({R.id.bt_cancel,R.id.bt_import})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                getPresenter().cancel();
                break;
            case R.id.bt_import:
                getPresenter().onImportClick(mEditTextYourBrainCode.getText().toString());
                break;
        }
    }

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

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
