package com.example.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RateActivity extends AppCompatActivity implements Runnable{

    private final String TAG="Rate";
    private float dollarRate=0.1f;
    private float euroRate=0.2f;
    private float wonRate=0.3f;


    EditText rmb;
    TextView showOut;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb=findViewById(R.id.rmb);
        showOut=findViewById(R.id.showOut);

        //获取sp里面保存的数据；
        // private是私有访问，只有当前的APP应用可以读写该文件；
        SharedPreferences sharedPreferences = getSharedPreferences("myRate", Activity.MODE_PRIVATE);
        //另一种获取方式，不过只能创建一个配置文件，其余一样；
        //SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);

        Log.i(TAG,"onCreate:sp dollarRate"+ dollarRate);
        Log.i(TAG,"onCreate:sp euroRate"+ euroRate);
        Log.i(TAG,"onCreate:sp wonRate"+ wonRate);

        //开启子线程；
        Thread t = new Thread(this);//线程运行时，会去寻找this对象的run方法；
        t.start();

        handler = new Handler(){
            //对Handler类方法的重写，所以后面有“；”
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==5){    //判断是哪个线程返回的数据；
                    String str = (String) msg.obj;
                    Log.i(TAG,"handleMessage:getMessage msg ="+ str);
                    showOut.setText(str);
                }

                super.handleMessage(msg);
            }
        };
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
        if(requestCode==1&&resultCode==2){
            Bundle bundle=data.getExtras();
            dollarRate=bundle.getFloat("dollar_new_key",0.1f);//这里的key应该是更改后的；
            euroRate=bundle.getFloat("euro_new_key",0.2f);
            wonRate=bundle.getFloat("won_new_key",0.3f);

            //将新的汇率写到sp里面保存,保存数据的文件名和读取数据的文件名一样；
            SharedPreferences sharedPreferences = getSharedPreferences("myRate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.apply();
            Log.i(TAG,"onCreate:数据已保存到SharedPreferences");
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

    @Override
    public void run() {
        for(int i=1;i<4;i++){
        Log.i(TAG,"onCreate:i+"+ i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //获取Msg对象，用于返回主线程；
        Message msg = handler.obtainMessage();
        msg.what = 5;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);

        //从子线程获取网络数据；
        URL url = null;
        try {
            url=new URL("https://www.boc.cn/sourcedb/whpj/");
            HttpsURLConnection http = (HttpsURLConnection) url.openConnection();

            InputStream in = http.getInputStream();  //获得输入流；
            String html=inputStream2String(in);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    //输入流转成字符串过程；
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize=1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in =new InputStreamReader(inputStream,"UTF-8");
        for (; ; ){
            int rsz = in.read(buffer,0,buffer.length);
            if(rsz<0)
                break;
            out.append(buffer,0,rsz);
        }
        return out.toString();
    }

}