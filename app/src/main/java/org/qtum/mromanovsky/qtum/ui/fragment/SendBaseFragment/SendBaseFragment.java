package org.qtum.mromanovsky.qtum.ui.fragment.SendBaseFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class SendBaseFragment extends BaseFragment implements SendBaseFragmentView {

    public static final int LAYOUT = R.layout.fragment_send_base;
    private final int code_response = 200;
    private static final String IS_QR_CODE_RECOGNITION = "is_qr_code_recognition";
    private ActionBar mActionBar;

    SendBaseFragmentPresenterImpl sendBaseFragmentPresenter;

    @BindView(R.id.bt_qr_code)
    ImageButton mButtonQrCode;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @OnClick({R.id.bt_qr_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_qr_code:
                getPresenter().onClickQrCode();
                break;
        }
    }

    public static SendBaseFragment newInstance(boolean qrCodeRecognition){
        SendBaseFragment sendBaseFragment = new SendBaseFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_QR_CODE_RECOGNITION,qrCodeRecognition);
        sendBaseFragment.setArguments(args);
        return sendBaseFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().isQrCodeRecognition(getArguments().getBoolean(IS_QR_CODE_RECOGNITION));
    }

    @Override
    protected void createPresenter() {
        sendBaseFragmentPresenter = new SendBaseFragmentPresenterImpl(this);
    }

    @Override
    protected SendBaseFragmentPresenterImpl getPresenter() {
        return sendBaseFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void openInnerFragmentForResult(Fragment fragment) {
        fragment.setTargetFragment(this,code_response);
        getChildFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right,R.anim.exit_to_left,R.anim.enter_from_left,R.anim.exit_to_right)
                .add(R.id.fragment_container_send_base,fragment,fragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openInnerFragment(Fragment fragment) {
        fragment.setTargetFragment(this,code_response);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_send_base,fragment,fragment.getClass().getCanonicalName())
                .commit();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            mActionBar = activity.getSupportActionBar();
            mActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void qrCodeRecognitionToolBar() {
        mButtonQrCode.setVisibility(View.GONE);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void sendToolBar() {
        if(mButtonQrCode!=null) {
            mButtonQrCode.setVisibility(View.VISIBLE);
            mActionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    public void onResponse(String pubAddress, Double amount){
        getPresenter().onResponse(pubAddress,amount);
    }
}
