package com.example.myapplication;

import static com.example.myapplication.DbQuery.loadQuestion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.myapplication.my_interface.MyCompleteListener;

public class StandbyScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standby_screen);

        loadData();


    }

    private void loadData() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("AAA",DbQuery.g_lessonList.size()+"");
                loadQuestion(DbQuery.g_lessonList);
                Intent intent = new Intent(StandbyScreenActivity.this,TestActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);

    }
}