package com.example.firstapp;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Scanner;

public class Final_SecondActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__second);

        button = findViewById(R.id.go);

        button.setOnClickListener(new View.OnClickListener() {//为button添加事件
            @Override
            public void onClick(View v) {

                EditText inp = findViewById(R.id.final_inp);//获取editText对象
                String thisword = inp.getText().toString();          //获取对象里输入的值
                //记录搜索到的结果条数
                int sum = 1;
                String line, str = "";
                String title = "";
                try {

                    TextView text = findViewById(R.id.final_out);
                    Scanner scan = new Scanner(getResources().getAssets().open("shici.txt"));//扫描古诗文件
                    out:
                    while (scan.hasNext()) {
                        line = scan.nextLine();
                        for (int i = 0; i < line.length(); i++) {
                            if (line.charAt(i) == '《') {
                                title = line;
                                continue out;
                            }

                            if (line.indexOf(thisword) == i) {
                                str += sum + ":" + line + "\n-------" + title + "\n";
                                sum++;
                                text.setText(str);
                                title = "";
                                //设置滚动条
                                text.setMovementMethod(ScrollingMovementMethod.getInstance());
                            }
                            else if
                            (thisword.equals ("。") || thisword.equals("？") || thisword .equals("，") || thisword.equals("！"))
                                break out;

                        }

                    }

                    int n = 0;
                    SpannableStringBuilder style = new SpannableStringBuilder(str);
                    while (n >= 0) {
                        int l = str.indexOf(thisword, n);
                        int r = l + thisword.length();
                        if (l == -1)
                            break;
                        n = l + 1;

                        //设置关键字标红
                        style.setSpan(new ForegroundColorSpan(Color.RED), l, r, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    }

                    //设置滚动条
                    text.setMovementMethod(ScrollingMovementMethod.getInstance());
                    text.setHorizontallyScrolling(true);
                    text.setText(style);

                }
                catch (IOException e) {
                    TextView text = findViewById(R.id.final_out);
                    str = "抱歉，没有查到您所输入的内容";
                    text.setText(str);
                }
            }
        });

    }

}

