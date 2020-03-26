package com.example.firstapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RateActivity extends AppCompatActivity {

    EditText rmb;
    TextView showOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=findViewById(R.id.rmb);
        showOut=findViewById(R.id.showOut);
    }

    public void onClick(View btn) {
        String str = rmb.getText().toString();
        float r = 0;
        if (str.length() > 0) {
            r = Float.parseFloat(str);
        } else {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }
        float val;
        if (btn.getId() == R.id.btn_dollar) {
            val = r * (1 / 7.08f);
        } else if (btn.getId() == R.id.btn_euro) {
            val = r * (1 / 7.75f);
        } else {
            val = r * 178.0f;
        }
        showOut.setText(String.valueOf(val));
    }

    public void openOne(View btn){
        //Intent hello=new Intent(this,SecondActivity.class);
        Intent web=new Intent(Intent.ACTION_VIEW, Uri.parse("https://authserver.swufe.edu.cn"));
        startActivity(web);
    }
}