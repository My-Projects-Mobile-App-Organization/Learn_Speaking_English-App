package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.LessonTestAdapter;
import com.example.myapplication.model.LessonTest;
import com.example.myapplication.my_interface.MyCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class LessonTestActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView testView;
    private Dialog dialogProgress;
    private LessonTestAdapter testAdapter;
    private TextView txtDialog;
    private LinearLayout linearLayout;

    private List<LessonTest> listTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_test);

        testView = (RecyclerView) findViewById(R.id.rcv_lesson_test);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        linearLayout = findViewById(R.id.btnVocabLesson);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonTestActivity.this,ReviewVocabularyActivity.class);
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle("Family");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // set layout Recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(LessonTestActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);

        testAdapter = new LessonTestAdapter(this);
        testAdapter.setData(getListTest());
        testView.setAdapter(testAdapter);

    }
    private List<LessonTest> getListTest() {
        listTest = new ArrayList<>();
//
//        listTest.add(new LessonTest("1","Family","Lesson 1",5));
//        listTest.add(new LessonTest("1","Family","Lesson 1",5));
//        listTest.add(new LessonTest("1","Family","Lesson 1",5));

        DbQuery.loadLessonTest(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                listTest =DbQuery.g_lessonList;
            }

            @Override
            public void onFailure() {
                Toast.makeText(LessonTestActivity.this,"Da co loi gi do, vui long thu lai",Toast.LENGTH_SHORT).show();
            }
        });
        listTest =DbQuery.g_lessonList;
        return listTest;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            LessonTestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
