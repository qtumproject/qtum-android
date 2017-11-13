package org.qtum.wallet.ui.base.base_activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import org.qtum.wallet.utils.ThemeUtils;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements BaseContextView {

    protected abstract void createPresenter();

    protected abstract BasePresenter getPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        ThemeUtils.setAppTheme(this,ThemeUtils.getCurrentTheme(this));

        super.onCreate(savedInstanceState);
        createPresenter();
        getPresenter().onCreate();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getPresenter().initializeViews();
        getPresenter().onPostCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getPresenter().onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy();
    }

    @Override
    public void hideKeyBoard() {
        Activity activity = this;
        if (isFinishing(activity)) return;
        View view = activity.getCurrentFocus();
        if (view != null) {
            hideKeyBoard(activity, view);
        }
    }

    @Override
    public void hideKeyBoard(View v) {
        Activity activity = this;
        if (isFinishing(activity)) return;
        if (v != null) {
            hideKeyBoard(activity, v);
        }
    }

    public void hideKeyBoard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    public void setFocusTextInput(View textInputEditText, View textInputLayout) {
        textInputEditText.setFocusableInTouchMode(true);
        textInputEditText.requestFocus();
        textInputLayout.setFocusableInTouchMode(true);
        textInputLayout.requestFocus();
    }

    public boolean isFinishing(Activity activity) {
        return activity == null || activity.isFinishing();
    }

    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setSoftMode() {
    }

    protected abstract void updateTheme();

    public void reloadActivity(){
        updateTheme();
    }
}
