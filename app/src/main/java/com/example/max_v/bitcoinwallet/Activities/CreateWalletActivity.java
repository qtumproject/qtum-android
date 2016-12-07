package com.example.max_v.bitcoinwallet.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.max_v.bitcoinwallet.R;

public class CreateWalletActivity extends AppCompatActivity {

    Button mCreateWalletButton;
    TextInputEditText mWalletNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_create);
        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if(supportActionBar!=null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mCreateWalletButton = (Button) findViewById(R.id.button_create_wallet);
        mCreateWalletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),MyWalletActivity.class);
                startActivity(intent);
            }
        });

        mWalletNameEditText = (TextInputEditText) findViewById(R.id.wallet_name_edit_text_create_wallet_activity);

    }
}
