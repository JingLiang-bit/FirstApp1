package com.example.firstapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Test2Activity extends ListActivity implements Runnable{

    private final String TAG = "Test2";
    Handler handler;
    EditText testInp;
    ListView testList;
    private String updateDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_test2);


        testInp = findViewById(R.id.testInp);
        testList = findViewById(R.id.testList);

        //获取当前系统时间
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr = sdf.format(today);

        SharedPreferences sharedPreferences = getSharedPreferences("myRate", Activity.MODE_PRIVATE);
        updateDate = sharedPreferences.getString("update_date","");
        if(!todayStr.equals(updateDate+6)){
            Log.i(TAG,"onCreate：需要更新");
            //开启子线程；
            Thread t = new Thread(this);//线程运行时，会去寻找this对象的run方法；
            t.start();
        }else{
            Log.i(TAG,"onCreate：不需要更新");
        }

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==7){
                    List<String> list2 = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(Test2Activity.this, android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };


    }

    public void openOne(View btn) {
        openConfig();
    }

    private void openConfig() {
        Intent web=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.swufe.edu.cn/index/tzgg.htm"));
    }

    @Override
    public void run() {
        //获取网络数据，带回到主线程里
        List<String> retList = new ArrayList<String>();

        Document doc = null;
        try {
            Thread.sleep(1000);
            String url = "https://www.swufe.edu.cn/index/tzgg.htm";
            doc = Jsoup.connect(url).get();
            Elements scripts = doc.getElementsByTag("script");
            Element script = scripts.get(0);
            Elements tds = script.getElementsByTag("li");
            for (int i = 0; i < tds.size(); i++) {
                Element td1 = tds.get(i);

                String str1 = td1.text();
                retList.add(td1.text() );
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage(7);
        msg.obj = retList;
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
