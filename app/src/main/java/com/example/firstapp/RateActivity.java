package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RateActivity extends AppCompatActivity {

    private final String TAG="Rate";
    private float dollarRate=0.1f;
    private float euroRate=0.1f;
    private float wonRate=0.1f;


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
        }
        else {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }

        if (btn.getId() == R.id.btn_dollar) {
            showOut.setText(String.format("%.2f",r*dollarRate));
        } else if (btn.getId() == R.id.btn_euro) {
            showOut.setText(String.format("%.2f",r*euroRate));
        } else {
            showOut.setText(String.format("%.2f",r*wonRate));
        }
    }

    public void openOne(View btn){
        openConfig();
    }

    private void openConfig() {
        //跳转网页
        //Intent web=new Intent(Intent.ACTION_VIEW, Uri.parse("https://authserver.swufe.edu.cn"));


        //把参数传递到下一个页面，用Intent方法传递参数
        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);

        //这样的方法打开，只能不能返回，只能无限打开新的界面
        // startActivity(config);

        startActivityForResult(config, 1);//可以打开并带回数据；1是请求参数；
    }

    //处理Config页面带回来的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //因为一个窗口可以打开多个窗口，返回参数区别哪个窗口返回的数据；
        //resultCode可能带回不同的数据，我们区分返回的是怎样的数据；
        if(resultCode==1&&resultCode==2){
            Bundle bundle=data.getExtras();
            dollarRate=bundle.getFloat("dollar_new_key",0.1f);//这里的key应该是更改后的；
            euroRate=bundle.getFloat("euro_new_key",0.2f);
            wonRate=bundle.getFloat("won_new_key",0.3f);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);//Menu填充器；
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }
}