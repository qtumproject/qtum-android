package org.qtum.wallet.ui.activity.splash_activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_activity.BaseActivity;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.utils.QtumIntent;
import org.qtum.wallet.utils.ThemeUtils;

import com.transitionseverywhere.ChangeClipBounds;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;

public class SplashActivity extends BaseActivity implements SplashActivityView {

    private SplashActivityPresenter presenter;
    private static final int LAYOUT = R.layout.lyt_splash;

    @BindView(R.id.ic_app_logo)
    AppCompatImageView appLogo;

    @BindView(R.id.ic_app_logo_red)
    AppCompatImageView appLogoRed;

    @BindView(R.id.ic_app_logo_white)
    AppCompatImageView appLogoWhite;

    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;

    @BindView(R.id.lyt_light)
    View lytLight;

    @BindView(R.id.txt_app_name_white)
    TextView appNameWhite;

    @BindView(R.id.txt_wait)
    TextView waitText;

    @BindView(R.id.txt_app_name)
    TextView appName;

    private ChangeClipBounds clip;

    private int appLogoHeight = 0;

    @Override
    public void initializeViews() {
        if (ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_DARK)) {
            appLogoWhite.setVisibility(View.INVISIBLE);
            recolorStatusBar(R.color.background);
            waitText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        } else {
            appLogoRed.setVisibility(View.INVISIBLE);
            recolorStatusBar(R.color.title_color_light);
        }

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

        startHandler = new Handler();
        startHandler.postDelayed(startRunnable, 2000);
    }

    private void DoTransition() {
        TransitionManager.endTransitions(rootLayout);
        appLogo.setClipBounds(new Rect(0, 0, appLogoHeight, appLogoHeight));

        if (ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_LIGHT)) {
            appName.setClipBounds(new Rect(0, 0, appName.getWidth(), appName.getHeight()));
            lytLight.setClipBounds(new Rect(0, 0, getResources().getDisplayMetrics().widthPixels, 0));
        }

        TransitionManager.beginDelayedTransition(rootLayout, clip);
        appLogo.setClipBounds(new Rect(0, 0, appLogoHeight, 0));

        if (ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_LIGHT)) {
            lytLight.setVisibility(View.VISIBLE);
            appName.setClipBounds(new Rect(0, 0, appName.getWidth(), 0));
            lytLight.setClipBounds(new Rect(0, 0, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels));
        }
    }

    @Override
    protected void updateTheme() {
    }

    Handler startHandler;
    Runnable startRunnable = new Runnable() {
        @Override
        public void run() {
            startApp();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        bindView();
    }

    @Override
    protected void onDestroy() {
        if (startHandler != null) {
            startHandler.removeCallbacks(startRunnable);
        }
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        clip = new ChangeClipBounds();
        clip.addTarget(appLogo);
        if (ThemeUtils.getCurrentTheme(this).equals(ThemeUtils.THEME_LIGHT)) {
            clip.addTarget(appName);
            clip.addTarget(lytLight);
        }
        clip.setDuration(2000);
    }

    public void recolorStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), color));
        }
    }

    @Override
    protected void createPresenter() {
        presenter = new SplashActivityPresenterImpl(this, new SplashActivityInteractorImpl(getContext()));
    }

    @Override
    protected SplashActivityPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void startApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(QtumIntent.USER_START_APP);
        startActivity(intent);
    }
}