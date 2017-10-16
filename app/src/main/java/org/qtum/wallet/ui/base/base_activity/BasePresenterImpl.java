package org.qtum.wallet.ui.base.base_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public abstract class BasePresenterImpl implements BasePresenter {

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onPostCreate() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPostResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

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
