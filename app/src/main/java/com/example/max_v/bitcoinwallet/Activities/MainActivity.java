package com.example.max_v.bitcoinwallet.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.max_v.bitcoinwallet.R;
import com.example.max_v.bitcoinwallet.Services.LoadingService;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String MyLog = "mylog";
    private Button mCreateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(MyLog,"hello");
        mCreateButton = (Button) findViewById(R.id.button_create_new);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),CreateWalletActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = new Intent(getApplicationContext(), LoadingService.class);
        startService(intent);

    }
}
