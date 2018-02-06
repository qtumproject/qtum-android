package org.qtum.wallet.ui.base.base_activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public interface BaseContextView {
    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    void hideKeyBoard();

    void initializeViews();

    void hideKeyBoard(View v);

    Context getContext();

    void finish();
}
