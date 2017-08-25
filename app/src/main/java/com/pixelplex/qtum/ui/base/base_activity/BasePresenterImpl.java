package com.pixelplex.qtum.ui.base.base_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public abstract class BasePresenterImpl implements BasePresenter {

    @Override
    public void onCreate(Context context) {

    }

    @Override
    public void onStart(Context context) {

    }

    @Override
    public void onPostCreate(Context context) {

    }

    @Override
    public void onResume(Context context) {

    }

    @Override
    public void onPause(Context context) {

    }

    @Override
    public void onStop(Context context) {

    }

    @Override
    public void onDestroy(Context context) {

    }

    @Override
    public void initializeViews() {
        getView().initializeViews();
    }


    @Override
    public void onRestoreInstanceState(Bundle savedState) {

    }

    @Override
    public void restoreState(Bundle savedState) {

    }

    @Override
    public void saveState(Bundle outState) {

    }

    @Override
    public void handleInitialArguments(Bundle arg) {

    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public BaseContextView getView() {
        return null;
    }

}
