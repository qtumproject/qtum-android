package com.pixelplex.qtum.ui.fragment;

import android.app.Dialog;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.pixelplex.qtum.R;
import com.transitionseverywhere.ChangeClipBounds;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by max-v on 6/7/2017.
 */

public class ProcessingDialogFragment extends DialogFragment implements Transition.TransitionListener{

    @BindView(R.id.spinner)
    FrameLayout mSpinner;
    @BindView(R.id.spinner_inside)
    View mSpinnerInside;
    @BindView(R.id.root_layout)
    RelativeLayout mRootLayout;

    ChangeClipBounds mClipBoundsTransition;
    Rotate mRotateTransition;
    int appLogoHeight = 0;

    String CLIP_BOUND = "com.transitionseverywhere.ChangeClipBounds";

    boolean forward_rotate = true;
    boolean forward_clip = true;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.lyt_processing_dialog,null);
        ButterKnife.bind(this,view);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        mClipBoundsTransition = new ChangeClipBounds();
        mClipBoundsTransition.addTarget(mSpinnerInside);
        mClipBoundsTransition.setDuration(300);
        mClipBoundsTransition.setInterpolator(new LinearInterpolator());
        mClipBoundsTransition.addListener(this);

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

                    mSpinnerInside.setClipBounds(new Rect(0, appLogoHeight, appLogoHeight, appLogoHeight));

                    doClipBoundsTransition();
                }
            });
        } else {
            appLogoHeight = (appLogoHeight == 0) ?  mSpinnerInside.getHeight() : appLogoHeight;

            mSpinnerInside.setClipBounds(new Rect(0, appLogoHeight, appLogoHeight, appLogoHeight));

            doClipBoundsTransition();
        }
    }

    private void doClipBoundsTransition(){
        //TransitionManager.endTransitions(mSpinner);
        //mSpinnerInside.setClipBounds(new Rect(0, 0, appLogoHeight, 0));
        if(forward_clip) {
//            TransitionManager.endTransitions(mSpinner);
//            mSpinnerInside.setClipBounds(new Rect(0, 0, appLogoHeight, appLogoHeight));
            TransitionManager.endTransitions(mSpinner);
            mSpinnerInside.setVisibility(View.VISIBLE);
            TransitionManager.beginDelayedTransition(mSpinner, mClipBoundsTransition);
            mSpinnerInside.setClipBounds(new Rect(0, 0, appLogoHeight, appLogoHeight));

        }else{
//            TransitionManager.endTransitions(mSpinner);
//            mSpinnerInside.setClipBounds(new Rect(0, 0, appLogoHeight, 0));
            TransitionManager.beginDelayedTransition(mSpinner, mClipBoundsTransition);

            mSpinnerInside.setClipBounds(new Rect(0, appLogoHeight, appLogoHeight, appLogoHeight));
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
        if(transition.getName().equals(CLIP_BOUND)) {

            if(forward_clip){
                mSpinnerInside.setVisibility(View.GONE);
            }

            doRotateTransition();

        } else {

            //mSpinnerInside.setClipBounds(new Rect(0, 0, appLogoHeight, appLogoHeight));
            //if(!forward_clip)
             //   mSpinnerInside.setClipBounds(new Rect(0, 0, appLogoHeight, appLogoHeight));
           // mSpinnerInside.setClipBounds(new Rect(0, 0, appLogoHeight, 0));
            doClipBoundsTransition();

            //doClipBoundsTransition();

        }
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
}
