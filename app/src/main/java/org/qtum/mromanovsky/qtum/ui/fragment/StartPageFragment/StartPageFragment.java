package org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment;


import android.graphics.drawable.AnimatedVectorDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class StartPageFragment extends BaseFragment implements StartPageFragmentView {

    public static final int LAYOUT = R.layout.fragment_start_page;
    public static final String TAG = "StartPageFragment";
    private int mAnimState;
    private Animation mAnimation;
    AnimatedVectorDrawable drawableBottom;
    AnimatedVectorDrawable drawableTop;

    private StartPageFragmentPresenterImpl mStartPageFragmentPresenter;


    @BindView(R.id.bt_create_new)
    Button mButtonCreateNew;
    @BindView(R.id.bt_import_wallet)
    Button mButtonImportWallet;
    @BindView(R.id.tv_start_page_you_dont_have)
    TextView mTextViewYouDontHave;
    @BindView(R.id.tv_start_page_create)
    TextView mTextViewStartPageCreate;
    @BindView(R.id.iv_bottom_wave)
    ImageView mImageViewBottomWave;
    @BindView(R.id.iv_top_wave)
    ImageView mImageViewTopWave;
    @BindView(R.id.iv_logo)
    ImageView mImageViewLogo;
    @BindView(R.id.iv_logo_txt)
    ImageView mImageViewLogoTxt;
    @BindView(R.id.rl_button_container)
    RelativeLayout mRelativeLayoutButtonContainer;

    @OnClick({R.id.bt_import_wallet, R.id.bt_create_new})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.bt_create_new:
                getPresenter().createNewWallet();
                break;
            case R.id.bt_import_wallet:
                getPresenter().importWallet();
                break;
        }
    }

    public static StartPageFragment newInstance() {
        StartPageFragment startPageFragment = new StartPageFragment();
        return startPageFragment;
    }

    @Override
    protected void createPresenter() {
        mStartPageFragmentPresenter = new StartPageFragmentPresenterImpl(this);
    }


    @Override
    protected StartPageFragmentPresenterImpl getPresenter() {
        return mStartPageFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        ((MainActivity) getActivity()).hideBottomNavigationView();

        mTextViewYouDontHave.setVisibility(View.INVISIBLE);
        mTextViewStartPageCreate.setVisibility(View.INVISIBLE);
        mRelativeLayoutButtonContainer.setVisibility(View.INVISIBLE);

        mAnimState = 9;

        drawableBottom = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatable_bottom,getActivity().getTheme());
        mImageViewBottomWave.setImageDrawable(drawableBottom);
        drawableTop = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.animatable_top,getActivity().getTheme());
        mImageViewTopWave.setImageDrawable(drawableTop);
        drawableBottom.start();
        drawableTop.start();

        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.logo_txt);
        mImageViewLogoTxt.startAnimation(mAnimation);

        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (mAnimState){
                    case 9:
                        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_for_text);
                        mTextViewYouDontHave.startAnimation(mAnimation);
                        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_for_wave);
                        mImageViewBottomWave.startAnimation(mAnimation);
                        mImageViewTopWave.startAnimation(mAnimation);
                        break;
                    case 10:
                        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_for_text);
                        mTextViewYouDontHave.setVisibility(View.VISIBLE);
                        mTextViewStartPageCreate.startAnimation(mAnimation);

                        mImageViewTopWave.setVisibility(View.INVISIBLE);
                        mImageViewBottomWave.setVisibility(View.INVISIBLE);
                        drawableBottom.stop();
                        drawableTop.stop();

                        break;
                    case 11:
                        mTextViewStartPageCreate.setVisibility(View.VISIBLE);

                        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.button_move);
                        mRelativeLayoutButtonContainer.startAnimation(mAnimation);
                        break;
                    case 12:
                        mRelativeLayoutButtonContainer.setVisibility(View.VISIBLE);
                }
                mAnimation.setAnimationListener(this);
                mAnimState++;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
