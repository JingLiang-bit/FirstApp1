package com.example.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    TextView scoreA;
    TextView scoreB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        scoreA=(TextView)findViewById(R.id.scoreA);
        scoreB=(TextView)findViewById(R.id.scoreB);

    }

    public void btnAdd1(View btn){
        if(btn.getId()==R.id.button21){
            showScoreA(1);
        }
        else{
            showScoreB(1);
        }
    }

    public void btnAdd2(View btn) {
        if(btn.getId()==R.id.button22){
            showScoreA(2);
        }
        else{
            showScoreB(2);
        }
    }

    public void btnAdd3(View btn){
        if(btn.getId()==R.id.button23){
            showScoreA(3);
        }
        else{
            showScoreB(3);
        }
    }

    public void btnResit(View btn){
        scoreA.setText("0");
        scoreB.setText("0");
    }

    private void showScoreA(int inc){
        String oldScore=scoreA.getText().toString();
        int newScore=Integer.parseInt(oldScore)+inc;
        scoreA.setText(""+newScore);
    }

    private void showScoreB(int inc){
        String oldScore=scoreB.getText().toString();
        int newScore=Integer.parseInt(oldScore)+inc;
        scoreB.setText(""+newScore);
    }
}
