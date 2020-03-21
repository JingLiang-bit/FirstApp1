package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView result;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.result);
        input = (EditText)findViewById(R.id.inputText);
    }

    public void trans(View v) {
        String str = input.getText().toString();
        double cnt = Integer.parseInt(str) * 1.8+32;
        result.setText("转换结果为："+cnt);
    }
}