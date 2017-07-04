package com.pixelplex.qtum.ui.activity.base_activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;


public interface BaseContextView {

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    void setSoftMode();

    void hideKeyBoard();

    void hideKeyBoard(View v);

    void initializeViews();

    Context getContext();

    void finish();

}
