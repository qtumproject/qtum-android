package com.example.max_v.bitcoinwallet.Activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.max_v.bitcoinwallet.R;

public class ReceiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_receive);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setSubtitle(getString(R.string.total_balance) + "3,700,456" + getString(R.string.btc));
        }
    }
}
