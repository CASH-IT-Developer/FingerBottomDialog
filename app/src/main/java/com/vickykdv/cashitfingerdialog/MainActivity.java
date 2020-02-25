package com.vickykdv.cashitfingerdialog;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vickykdv.fingerbottomdialog.cashItFingerBottomDlg;
import com.vickykdv.fingerbottomdialog.interFace.cashItGlobalInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements cashItGlobalInterface.CashitFingerDlgCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cashItFingerBottomDlg.isAvailable(MainActivity.this)) {
                    cashItFingerBottomDlg.initialize(MainActivity.this)
                            .title("Masuk menggunakan fingerprint")
                            .message("Letakkan jari anda difingerprint")
                            .callback(MainActivity.this)
                            .show();
                }
            }
        });
    }


    @Override
    public void onAuthenticationSucceeded() {
        Log.d("MainActivity", "onAuthenticationSucceeded: ");
    }

    @Override
    public void onAuthenticationCancel() {
        Log.d("MainActivity", "onAuthenticationCancel: ");
    }
}
