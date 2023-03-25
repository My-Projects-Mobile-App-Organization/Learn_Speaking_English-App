package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.adapter.VocabResultTestAdapter;
import com.example.myapplication.model.VocabularyWord;

import java.util.ArrayList;
import java.util.List;

public class ResultTestActivity extends AppCompatActivity {

    ProgressBar pb_Result;
    TextView txtScoreResult;
    Button btnBack;

    RecyclerView rcv_list_vocab;
    VocabResultTestAdapter vocabResultTestAdapter;
    ArrayList<VocabularyWord> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_test);

        AnhXa();

        loadData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultTestActivity.this.finish();
            }
        });

    }

    private void loadData() {

        int countCorrectQuestion = 0;
        for (int i=0;i<DbQuery.g_quesList.size();i++){
            if(DbQuery.g_quesList.get(i).isStatusQuestion()){
                countCorrectQuestion++;
            }
        }
        int progressTest = (countCorrectQuestion/DbQuery.g_quesList.size())*100;
        progressTest=20;
        pb_Result.setProgress(progressTest);
        txtScoreResult.setText(progressTest + "%");

        vocabResultTestAdapter = new VocabResultTestAdapter(this);
        vocabResultTestAdapter.setData(DbQuery.g_lessonList.get(DbQuery.g_selected_lesson_index).getListVocab());

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_list_vocab.setLayoutManager(llm);

        rcv_list_vocab.setAdapter(vocabResultTestAdapter);
    }

    private List<VocabularyWord> getListVocab() {
        mList = new ArrayList<>();



        return mList;
    }

    private void AnhXa() {
        pb_Result = findViewById(R.id.pbResult);
        txtScoreResult = findViewById(R.id.txtScoreResult);
        btnBack = findViewById(R.id.btnBackToLessonActivity);

        rcv_list_vocab = findViewById(R.id.rcv_list_vocab_result);
    }
}