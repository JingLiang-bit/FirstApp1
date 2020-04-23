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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RateActivity<runnable> extends AppCompatActivity implements Runnable {

    private final String TAG = "Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;
    private String updateDate = "";


    EditText rmb;
    TextView showOut;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = findViewById(R.id.rmb);
        showOut = findViewById(R.id.showOut);


        //获取sp里面保存的数据；
        // private是私有访问，只有当前的APP应用可以读写该文件；
        SharedPreferences sharedPreferences = getSharedPreferences("myRate", Activity.MODE_PRIVATE);
        //另一种获取方式，不过只能创建一个配置文件，其余一样；
        //SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.1f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.2f);
        wonRate = sharedPreferences.getFloat("won_rate",0.3f);
        updateDate = sharedPreferences.getString("update_date","");

        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr = sdf.format(today);


        Log.i(TAG,"onCreate:sp dollarRate"+ dollarRate);
        Log.i(TAG,"onCreate:sp euroRate"+ euroRate);
        Log.i(TAG,"onCreate:sp wonRate"+ wonRate);
        Log.i(TAG,"onCreate:sp updateDate"+ updateDate);

        //判断时间
        if(!todayStr.equals(updateDate)){
            Log.i(TAG,"onCreate：需要更新");
            //开启子线程；
            Thread t = new Thread(this);//线程运行时，会去寻找this对象的run方法；
            t.start();
        }else{
            Log.i(TAG,"onCreate：不需要更新");
        }


        //对Handler类方法的重写，所以后面有“；”
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 5) {    //判断是哪个线程返回的数据；
                    Bundle bdl = (Bundle) msg.obj;
                    dollarRate = bdl.getFloat("dollar-rate");
                    euroRate = bdl.getFloat("euro-rate");
                    wonRate = bdl.getFloat("won-rate");

                    Log.i(TAG, "dollarRate=" + dollarRate);
                    Log.i(TAG, "euroRate=" + euroRate);
                    Log.i(TAG, "wonRate=" + wonRate);

                    //保存更新的日期；
                    SharedPreferences sharedPreferences = getSharedPreferences("myRate", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putFloat("dollar-rate", dollarRate);
                    editor.putFloat("euro-rate", euroRate);
                    editor.putFloat("won-rate", wonRate);
                    editor.putString("update_date",todayStr);
                    editor.apply();


                    Toast.makeText(RateActivity.this, "汇率已更新", Toast.LENGTH_SHORT).show();
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
        } else {
            Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
        }

        if (btn.getId() == R.id.btn_dollar) {
            showOut.setText(String.format("%.2f", r * dollarRate));
        } else if (btn.getId() == R.id.btn_euro) {
            showOut.setText(String.format("%.2f", r * euroRate));
        } else {
            showOut.setText(String.format("%.2f", r * wonRate));
        }
    }

    public void openOne(View btn) {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);//Menu填充器；
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();
        }else if(item.getItemId()==R.id.open_list){
            //打开列表窗口
            Intent list = new Intent(this, RateListActivity.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }



    //处理Config页面带回来的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //因为一个窗口可以打开多个窗口，返回参数区别哪个窗口返回的数据；
        //resultCode可能带回不同的数据，我们区分返回的是怎样的数据；
        if (requestCode == 1 && resultCode == 2) {
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("dollar_new_key", 0.1f);//这里的key应该是更改后的；
            euroRate = bundle.getFloat("euro_new_key", 0.1f);
            wonRate = bundle.getFloat("won_new_key", 0.1f);

            //将新的汇率写到sp里面保存,保存数据的文件名和读取数据的文件名一样；
            SharedPreferences sharedPreferences = getSharedPreferences("myRate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar-rate", dollarRate);
            editor.putFloat("euro-rate", euroRate);
            editor.putFloat("won-rate", wonRate);
            editor.apply();
            Log.i(TAG, "onCreate:数据已保存到SharedPreferences");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void run() {
        Log.i(TAG, "run: run()......");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //从子线程获取网络数据；
        /*URL url = null;
        try {
            url=new URL("http://www.usd-cny.com/bankofchina.htm");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            InputStream in = http.getInputStream();  //获得输入流；
            String html=inputStream2String(in);
            Document doc = Jsoup.parse(html);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/


        //用于储存获取的汇率
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            //doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            //doc = Jsoup.parse(html);
            String url = "http://www.usd-cny.com/bankofchina.htm";
            doc = Jsoup.connect(url).get();
            Log.i(TAG, "run:" + doc.title());
            Elements tables = doc.getElementsByTag("table");
            Element table1 = tables.get(0);
            Log.i(TAG, "table1= " + table1);
            //获取table里TD中的数据；
            Elements tds = table1.getElementsByTag("td");
            for (int i = 0; i < tds.size(); i += 6) {
                Element td1 = tds.get(i);
                Element td2 = tds.get(i + 5);

                //Log.i(TAG,"run:" + td1.text() +"==>"+ td2.text());

                String str1 = td1.text();
                String val = td2.text();
                if ("美元".equals(str1)) {
                    bundle.putFloat("dollar-rate", 100.0f / Float.parseFloat(val));
                } else if ("欧元".equals(str1)) {
                    bundle.putFloat("euro-rate", 100.0f / Float.parseFloat(val));
                } else if ("韩元".equals(str1)) {
                    bundle.putFloat("won-rate", 100.0f / Float.parseFloat(val));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //boundle中保存获取的汇率；

        //获取Msg对象，用于返回主线程；
        Message msg = handler.obtainMessage(5);
        //msg.what = 5;
        //msg.obj = "Hello from run()"; 带回字符串
        msg.obj = bundle;
        handler.sendMessage(msg);
    }

    //输入流转成字符串过程；
    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

}
