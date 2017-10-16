package org.qtum.wallet.ui.base.base_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public interface BasePresenter {

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onPostCreate();

    void initializeViews();

    void onRestoreInstanceState(Bundle savedState);

    void restoreState(Bundle savedState);

    void saveState(Bundle outState);

    void onPostResume();

    void handleInitialArguments(Bundle arg);

    void startActivity(Intent intent);

    BaseContextView getView();

}
