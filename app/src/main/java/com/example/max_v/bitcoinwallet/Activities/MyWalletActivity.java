package com.example.max_v.bitcoinwallet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.max_v.bitcoinwallet.R;

public class MyWalletActivity extends AppCompatActivity {

    private Button mSendButton;
    private Button mReceiveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_my_wallet);
        setSupportActionBar(toolbar);

        mSendButton = (Button) findViewById(R.id.send_button_my_wallet);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),SendActivity.class);
                startActivity(intent);
            }
        });

        mReceiveButton = (Button) findViewById(R.id.recieve_button_my_wallet);
        mReceiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),ReceiveActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
