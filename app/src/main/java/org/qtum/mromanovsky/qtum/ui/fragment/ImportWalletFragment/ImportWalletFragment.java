package org.qtum.mromanovsky.qtum.ui.fragment.ImportWalletFragment;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.widget.ImageView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;


public class ImportWalletFragment extends BaseFragment implements ImportWalletFragmentView {

    public static final int LAYOUT = R.layout.fragment_import_wallet;

    ImportWalletFragmentPresenterImpl mImportWalletFragmentPresenter;
    AnimatedVectorDrawable drawableBottom;
    AnimatedVectorDrawable drawableTop;

    @BindView(R.id.iv_bottom_wave)
    ImageView mImageViewBottomWave;
    @BindView(R.id.iv_top_wave)
    ImageView mImageViewTopWave;

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
        drawableBottom = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatable_bottom,getActivity().getTheme());
        mImageViewBottomWave.setImageDrawable(drawableBottom);
        drawableBottom.start();
        drawableTop = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatable_top,getActivity().getTheme());
        mImageViewTopWave.setImageDrawable(drawableTop);
        drawableTop.start();
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
