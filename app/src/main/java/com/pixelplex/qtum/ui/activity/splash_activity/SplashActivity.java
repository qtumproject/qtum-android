package com.pixelplex.qtum.ui.activity.splash_activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.base_activity.BaseActivity;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.utils.QtumIntent;
import com.pixelplex.qtum.utils.ThemeUtils;
import com.transitionseverywhere.ChangeClipBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;


public class SplashActivity extends BaseActivity implements SplashActivityView, Transition.TransitionListener {

    private SplashActivityPresenterImpl presenter;
    private static final int LAYOUT = R.layout.lyt_splash;
    private static final int LAYOUT_LIGHT = R.layout.lyt_splash_light;

    @BindView(R.id.ic_app_logo)
    AppCompatImageView appLogo;

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    private ChangeClipBounds clip;

    private int appLogoHeight = 0;

    Handler handler;

    @Override
    public void initializeViews() {

        if(ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK)) {
            if (appLogo.getHeight() == 0) {
                appLogo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        appLogo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        appLogoHeight = (appLogoHeight == 0) ? appLogo.getHeight() : appLogoHeight;
                        DoTransition();
                    }
                });
            } else {
                appLogoHeight = (appLogoHeight == 0) ? appLogo.getHeight() : appLogoHeight;
                DoTransition();
            }
        }
    }

    private void DoTransition(){
        TransitionManager.endTransitions(rootLayout);
        appLogo.setClipBounds(new Rect(0,0,appLogoHeight,appLogoHeight));
        TransitionManager.beginDelayedTransition(rootLayout, clip);
        appLogo.setClipBounds(new Rect(0,0,appLogoHeight,0));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void updateTheme() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK)? LAYOUT : LAYOUT_LIGHT);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK)) {
            clip = new ChangeClipBounds();
            clip.addTarget(appLogo);
            clip.setDuration(2000);
            clip.addListener(this);
        }
    }

    @Override
    protected void createPresenter() {
        presenter = new SplashActivityPresenterImpl(this);
    }

    @Override
    protected SplashActivityPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    public void onTransitionStart(Transition transition) {

    }

    @Override
    public void onTransitionEnd(Transition transition) {
        DoTransition();
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
    public void startApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(QtumIntent.USER_START_APP);
        startActivity(intent);
    }
}
