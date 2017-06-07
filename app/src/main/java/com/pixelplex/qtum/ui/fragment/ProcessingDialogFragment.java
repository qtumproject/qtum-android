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

    ChangeClipBounds mClip;
    int appLogoHeight = 0;

    String CLIP_BOUND = "com.transitionseverywhere.ChangeClipBounds";

    int rotate_state = 0;
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

        mClip = new ChangeClipBounds();
        mClip.addTarget(mSpinnerInside);
        mClip.setDuration(1000);
        mClip.addListener(this);

        if(mSpinnerInside.getHeight()==0) {
            mSpinnerInside.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mSpinnerInside.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    appLogoHeight = (appLogoHeight == 0) ?  mSpinnerInside.getHeight() : appLogoHeight;
                    doClipTransition();
                }
            });
        } else {
            appLogoHeight = (appLogoHeight == 0) ?  mSpinnerInside.getHeight() : appLogoHeight;
            doClipTransition();
        }
    }

    private void doClipTransition(){
        //TransitionManager.endTransitions(mRootLayout);
        //mSpinnerFull.setClipBounds(new Rect(0,0,appLogoHeight,appLogoHeight));

        TransitionManager.beginDelayedTransition(mRootLayout, mClip);
        mSpinnerInside.setClipBounds(new Rect(0,0,appLogoHeight,forward_clip?0:appLogoHeight));


    }

    private void doRotateTransition(){
        TransitionManager.beginDelayedTransition(mRootLayout, (new Rotate().setDuration(1000)).addListener(this));
        switch(rotate_state) {
            case 0:
                mSpinner.setRotation(0);
                break;
            case 1:
                mSpinner.setRotation(180);
                break;
            case 2:
                mSpinner.setRotation(360);
                break;
        }
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {
        if(transition.getName().equals(CLIP_BOUND)) {
            doRotateTransition();
            rotate_state = (rotate_state+1)%3;
        } else {
            doClipTransition();
            forward_clip = !forward_clip;
        }
        //doTransition();
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
