package com.example.firstapp;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class Test1Activity extends Activity {



    private EditText et_ss;

    private ListView lsv_ss;

    private List<String> list = new ArrayList<String>();

    boolean isFilter;

    private MyAdapter adapter = null;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setViews();// 控件初始化

        setData();// 给listView设置adapter

        setListeners();// 设置监听

    }
    // 控件初始化


    private void setViews() {

        et_ss = (EditText) findViewById(R.id.testInp);// EditText控件

        lsv_ss = (ListView)findViewById(R.id.testList);// ListView控件

    }


    //数据初始化并设置adapter


    private void setData() {

        initData();// 初始化数据



        // 这里创建adapter的时候，构造方法参数传了一个接口对象，这很关键，回调接口中的方法来实现对过滤后的数据的获取

        adapter = new MyAdapter(list, this, new FilterListener() {

            // 回调方法获取过滤后的数据

            public void getFilterData(List<String> list) {

                // 这里可以拿到过滤后数据，所以在这里可以对搜索后的数据进行操作

                Log.e("TAG", "接口回调成功");

                Log.e("TAG", list.toString());

                setItemClick(list);

            }

        });

        lsv_ss.setAdapter(adapter);

    }





     //给listView添加item的单击事件

    protected void setItemClick(final List<String> filter_lists) {

        lsv_ss.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,

                                    int position, long id) {

                // 点击对应的item时，弹出toast提示所点击的内容

                Toast.makeText(Test1Activity.this, filter_lists.get(position), Toast.LENGTH_SHORT).show();

            }

        });

    }





     //简单的list集合添加一些测试数据

    private void initData() {

        list.add("《春晓》春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。");

        list.add("《鹿柴》空山不见人，但闻人语响。返影入深林，复照青苔上。");

        list.add("《相思》红豆生南国，春来发几只。愿君多采撷，此物最相思。");

        list.add("《杂诗》君自故乡来，应知故乡事。来日倚窗前，寒梅著花未。");

        list.add("《钟南望余雪》终南阴岭秀，积雪浮云端。林表明霁色，城中增慕寒。");

    }



    private void setListeners() {

        // 没有进行搜索的时候，也要添加对listView的item单击监听

        setItemClick(list);



         //对编辑框添加文本改变监听，搜索的具体功能在这里实现

         //文本该变的时候进行搜索。关键方法是重写的onTextChanged（）方法。


        et_ss.addTextChangedListener(new TextWatcher() {




             // 编辑框内容改变的时候会执行该方法

            @Override

            public void onTextChanged(CharSequence s, int start, int before,

                                      int count) {

                // 如果adapter不为空的话就根据编辑框中的内容来过滤数据

                if(adapter != null){

                    adapter.getFilter().filter(s);

                }

            }



            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub

            }



            @Override

            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub

            }

        });

    }









}
