package org.qtum.mromanovsky.qtum.ui.activity.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements BaseContextView {

    protected abstract void createPresenter();

    protected abstract BasePresenterImpl getPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createPresenter();
        getPresenter().onCreate(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        bindView();
        getPresenter().initializeViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getPresenter().onStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getPresenter().onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroy(this);
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

    public boolean isFinishing(Activity activity) {
        return activity == null || activity.isFinishing() || activity.isDestroyed();
    }

    protected void bindView() {
        ButterKnife.bind(this);
    }
}
