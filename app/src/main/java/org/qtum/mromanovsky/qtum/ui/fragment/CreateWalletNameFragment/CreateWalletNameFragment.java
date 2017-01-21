package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment;


import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateWalletNameFragment extends BaseFragment implements CreateWalletNameFragmentView {

    public static final int LAYOUT = R.layout.fragment_create_wallet_name;
    public static final String TAG = "CreateWalletNameFragment";

    CreateWalletNameFragmentPresenterImpl mCreateWalletFragmentPresenter;

    @BindView(R.id.bt_confirm)
    Button mButtonConfirm;
    @BindView(R.id.bt_cancel)
    Button mButtonCancel;
    @BindView(R.id.et_wallet_name)
    TextInputEditText mTextInputEditTextWalletName;
    @BindView(R.id.til_wallet_name)
    TextInputLayout mTextInputLayoutWalletName;
    @BindView(R.id.iv_bottom_wave)
    ImageView mImageViewBottomWave;
    @BindView(R.id.iv_top_wave)
    ImageView mImageViewTopWave;

    @OnClick({R.id.bt_confirm, R.id.bt_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm:
                getPresenter().confirm(mTextInputEditTextWalletName.getText().toString());
                break;
            case R.id.bt_cancel:
                getPresenter().cancel();
                break;
        }
    }

    public static CreateWalletNameFragment newInstance() {
        CreateWalletNameFragment createWalletNameFragment = new CreateWalletNameFragment();
        return createWalletNameFragment;
    }

    @Override
    protected void createPresenter() {
        mCreateWalletFragmentPresenter = new CreateWalletNameFragmentPresenterImpl(this);
    }

    @Override
    protected CreateWalletNameFragmentPresenterImpl getPresenter() {
        return mCreateWalletFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        final AnimatedVectorDrawable drawableBottom = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatable_bottom);
        mImageViewBottomWave.setImageDrawable(drawableBottom);
        drawableBottom.start();
        final AnimatedVectorDrawable drawableTop = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatable_top);
        mImageViewTopWave.setImageDrawable(drawableTop);
        drawableTop.start();
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void incorrectName(String errorText) {
        mTextInputEditTextWalletName.setText("");
        mTextInputLayoutWalletName.setErrorEnabled(true);
        mTextInputLayoutWalletName.setError(errorText);
    }
}
