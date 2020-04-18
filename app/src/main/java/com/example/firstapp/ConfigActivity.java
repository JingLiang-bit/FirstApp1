package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity {

    public final String TAG="ConfigActivity";

    EditText dollarText;
    EditText euroText;
    EditText wonText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        //在被传递参数的页面，用getIntent获得传递过来的参数；
        Intent intent =getIntent();
        //获取数据的时候是有类型的；
        float dollar2=intent.getFloatExtra("dollar_rate_key",0.1f);
        float euro2=intent.getFloatExtra("euro_rate_key",0.2f);
        float won2=intent.getFloatExtra("won_rate_key",0.3f);

        Log.i(TAG,"onCreate:dollar2="+dollar2);
        Log.i(TAG,"onCreate:euro2="+euro2);
        Log.i(TAG,"onCreate:won2="+won2);

        dollarText=(EditText)findViewById(R.id.dollar_rate);
        euroText=(EditText)findViewById(R.id.euro_rate);
        wonText=(EditText)findViewById(R.id.won_rate);

        //显示数据到控件；
        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));
    }

    public void save(View btn){
        //获取新的数据；
        float newDollar=Float.parseFloat(dollarText.getText().toString());
        float newEuro=Float.parseFloat(euroText.getText().toString());
        float newWon=Float.parseFloat(wonText.getText().toString());

        //保存到Bundle或放入到Extra;
        Intent intent=getIntent();
        Bundle bdl=new Bundle();
        bdl.putFloat("dollar_new_key",newDollar);
        bdl.putFloat("euro_new_key",newEuro);
        bdl.putFloat("won_new_key",newWon);
        intent.putExtras(bdl);
        setResult(2,intent);  //返回编码；

        //返回到调用页面；
        finish();
    }
}
