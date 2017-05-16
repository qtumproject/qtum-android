package com.pixelplex.qtum.ui.fragment.StartPageFragment;


import android.animation.Animator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class StartPageFragment extends BaseFragment implements StartPageFragmentView {

    private int mAnimState;
    private Animation mAnimation;
    private boolean mIsStarted = false;

    private StartPageFragmentPresenterImpl mStartPageFragmentPresenter;


    @BindView(R.id.bt_create_new)
    Button mButtonCreateNew;
    @BindView(R.id.bt_import_wallet)
    Button mButtonImportWallet;
    @BindView(R.id.tv_start_page_you_dont_have)
    TextView mTextViewYouDontHave;
    @BindView(R.id.tv_start_page_create)
    TextView mTextViewStartPageCreate;

    @BindView(R.id.wave1)
    LottieAnimationView mWave1;
    @BindView(R.id.wave2)
    LottieAnimationView mWave2;

    @BindView(R.id.iv_logo_txt)
    ImageView mImageViewLogoTxt;
    @BindView(R.id.rl_button_container)
    RelativeLayout mRelativeLayoutButtonContainer;
    @BindView(R.id.animation_view)
    LottieAnimationView mLottieAnimationView;

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

        Bundle args = new Bundle();

        StartPageFragment fragment = new StartPageFragment();
        fragment.setArguments(args);
        return fragment;
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
        return R.layout.fragment_start_page;
    }

    @Override
    public void initializeViews() {
        ((MainActivity) getActivity()).hideBottomNavigationView();

        if(mIsStarted){
            return;
        }

        mTextViewYouDontHave.setVisibility(View.INVISIBLE);
        mTextViewStartPageCreate.setVisibility(View.INVISIBLE);
        mRelativeLayoutButtonContainer.setVisibility(View.INVISIBLE);
        mImageViewLogoTxt.setVisibility(View.INVISIBLE);

        mLottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(mImageViewLogoTxt != null) {
                    mImageViewLogoTxt.startAnimation(mAnimation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        mAnimState = 0;

        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.logo_txt);

        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (mAnimState){
                    case 0:
                        mImageViewLogoTxt.setVisibility(View.VISIBLE);
                        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_for_text);
                        mTextViewYouDontHave.setVisibility(View.VISIBLE);
                        mTextViewYouDontHave.startAnimation(mAnimation);
                        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_for_wave);
                        mWave1.startAnimation(mAnimation);
                        mWave2.startAnimation(mAnimation);
                        break;
                    case 1:
                        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.alpha_for_text);
                        mTextViewYouDontHave.setVisibility(View.VISIBLE);
                        mTextViewStartPageCreate.startAnimation(mAnimation);

                        mWave2.setVisibility(View.INVISIBLE);
                        mWave1.setVisibility(View.INVISIBLE);

                        break;
                    case 2:
                        mTextViewStartPageCreate.setVisibility(View.VISIBLE);

                        mAnimation = AnimationUtils.loadAnimation(getContext(),R.anim.button_move);
                        mRelativeLayoutButtonContainer.startAnimation(mAnimation);
                        break;
                    case 3:
                        mIsStarted = true;
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
