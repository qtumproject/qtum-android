package com.pixelplex.qtum.ui.fragment.ProcessingDialog.Dark;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.ProcessingDialog.ProcessingDialogFragment;
import com.pixelplex.qtum.utils.ResizeHeightAnimation;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class ProcessingDialogFragmentDark extends ProcessingDialogFragment implements Transition.TransitionListener, Animation.AnimationListener {

    @BindView(R.id.spinner)
    FrameLayout mSpinner;
    @BindView(R.id.spinner_inside)
    View mSpinnerInside;

    private ResizeHeightAnimation mAnimForward;
    private ResizeHeightAnimation mAnimBackward;
    private Rotate mRotateTransition;
    private int appLogoHeight = 0;

    private boolean forward_rotate = true;
    private boolean forward_clip = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.lyt_processing_dialog,null);
        ButterKnife.bind(this,view);
        Dialog dialog = new Dialog(getContext());
        if(dialog.getWindow()!=null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        if(dialog.getWindow()!=null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        mRotateTransition = new Rotate();
        mRotateTransition.addTarget(mSpinner);
        mRotateTransition.setDuration(300);
        mRotateTransition.addListener(this);

        if(mSpinnerInside.getHeight()==0) {
            mSpinnerInside.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mSpinnerInside.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    appLogoHeight = (appLogoHeight == 0) ?  mSpinnerInside.getHeight() : appLogoHeight;
                    initializeAnim();
                    doClipBoundsTransition();
                }
            });
        } else {
            appLogoHeight = (appLogoHeight == 0) ?  mSpinnerInside.getHeight() : appLogoHeight;
            initializeAnim();
            doClipBoundsTransition();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initializeAnim(){
        mAnimForward = new ResizeHeightAnimation(mSpinnerInside, 0, appLogoHeight);
        mAnimForward.setDuration(300);
        mAnimForward.setFillEnabled(true);
        mAnimForward.setFillAfter(true);
        mAnimForward.setAnimationListener(this);

        mAnimBackward = new ResizeHeightAnimation(mSpinnerInside, appLogoHeight, 0);
        mAnimBackward.setDuration(300);
        mAnimBackward.setFillEnabled(true);
        mAnimBackward.setFillAfter(true);
        mAnimBackward.setAnimationListener(this);
    }

    private void doClipBoundsTransition(){

        if(forward_clip) {
            mSpinnerInside.startAnimation(mAnimForward);
        }else{
            mSpinnerInside.startAnimation(mAnimBackward);
        }
        forward_clip = !forward_clip;
    }

    private void doRotateTransition(){

        if(forward_rotate) {
            TransitionManager.endTransitions(mRootLayout);
            mSpinner.setRotation(0);
            TransitionManager.beginDelayedTransition(mRootLayout, (new Rotate().setDuration(1000)).addListener(this));
            mSpinner.setRotation(180);

        }else {
            TransitionManager.beginDelayedTransition(mRootLayout, (new Rotate().setDuration(1000)).addListener(this));
            mSpinner.setRotation(360);
        }

        forward_rotate = !forward_rotate;

    }

    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {
        doClipBoundsTransition();
    }

    @Override
    public void onTransitionCancel(Transition transition) {

    }

    @Override
    public void onTransitionPause(Transition transition) {

    }

    @Override
    public void onTransitionResume(Transition transition) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        doRotateTransition();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

}
