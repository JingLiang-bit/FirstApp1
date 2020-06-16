package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Final_WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__welcome);


    }

    public void openOne(View btn) {

        openConfig();

    }

    private void openConfig() {
        Intent config = new Intent(this, Final_SecondActivity.class);
        startActivity(config);
    }
}
