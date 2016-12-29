package org.qtum.mromanovsky.qtum.ui.activity.BaseActivity;

import android.content.Intent;
import android.view.View;


public interface BaseContextView {

    void finish();

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    void hideKeyBoard();

    void hideKeyBoard(View v);

}
