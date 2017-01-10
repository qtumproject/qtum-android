package org.qtum.mromanovsky.qtum.ui.fragment.ReceiveFragment;


import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;

public class ReceiveFragment extends BaseFragment implements ReceiveFragmentView {

    ReceiveFragmentPresenterImpl mReceiveFragmentPresenter;

    public static final int LAYOUT = R.layout.fragment_receive;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_qr_code)
    ImageView mImageViewQrCode;

    public static ReceiveFragment newInstance() {
        ReceiveFragment receiveFragment = new ReceiveFragment();
        return receiveFragment;
    }

    @Override
    protected void createPresenter() {
        mReceiveFragmentPresenter = new ReceiveFragmentPresenterImpl(this);
    }

    @Override
    protected ReceiveFragmentPresenterImpl getPresenter() {
        return mReceiveFragmentPresenter;
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
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getPresenter().generateQrCode();
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getFragmentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void setQrCode(Bitmap bitmap) {
        mImageViewQrCode.setImageBitmap(bitmap);
    }
}
