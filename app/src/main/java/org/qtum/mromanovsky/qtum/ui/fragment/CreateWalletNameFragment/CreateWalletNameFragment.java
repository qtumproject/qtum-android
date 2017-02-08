package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment;


import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateWalletNameFragment extends BaseFragment implements CreateWalletNameFragmentView {

    public final int LAYOUT = R.layout.fragment_create_wallet_name;
    public static final String TAG = "CreateWalletNameFragment";
    public static final String IS_CREATE_NEW = "is_create_new";
    public static boolean mIsCreateNew;
    AnimatedVectorDrawable drawableBottom;
    AnimatedVectorDrawable drawableTop;

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

    public static CreateWalletNameFragment newInstance(boolean isCreateNew) {
        CreateWalletNameFragment createWalletNameFragment = new CreateWalletNameFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_CREATE_NEW, isCreateNew);
        createWalletNameFragment.setArguments(args);
        return createWalletNameFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsCreateNew = getArguments().getBoolean(IS_CREATE_NEW);
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
        drawableBottom = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatable_bottom,getActivity().getTheme());
        mImageViewBottomWave.setImageDrawable(drawableBottom);
        drawableTop = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatable_top,getActivity().getTheme());
        mImageViewTopWave.setImageDrawable(drawableTop);

        mTextInputLayoutWalletName.setFocusableInTouchMode(true);
        mTextInputLayoutWalletName.requestFocus();
        mTextInputEditTextWalletName.setFocusable(true);
        mTextInputEditTextWalletName.requestFocus();
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

    @Override
    public void clearError() {
        mTextInputLayoutWalletName.setErrorEnabled(false);
    }

    @Override
    public void startAnimation() {
        super.startAnimation();
        drawableBottom.start();
        drawableTop.start();
    }

    @Override
    public void stopAnimation() {
        super.stopAnimation();
        drawableBottom.stop();
        drawableTop.stop();
    }
}
