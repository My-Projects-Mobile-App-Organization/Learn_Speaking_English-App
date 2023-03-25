package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.adapter.ReviewVocabularyAdapter;
import com.example.myapplication.model.VocabularyWord;
import com.example.myapplication.my_interface.IClickItemButtonTextToSpeechFastWPLListener;

import java.util.ArrayList;
import java.util.Locale;

public class ReviewVocabularyActivity extends AppCompatActivity {

    TextView txtExit;
    RecyclerView rcvVocabReview;
    Button btnNextVocab;
    CustomGridLayoutManager layoutManager;

    TextToSpeech textToSpeech;
    private int quesIdPos;

    ReviewVocabularyAdapter reviewVocabularyAdapter;
    ArrayList<VocabularyWord> listVocabReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_vocabulary);

        txtExit = findViewById(R.id.txtExitReviewVocab);
        rcvVocabReview = findViewById(R.id.rcv_list_vocab_review);
        btnNextVocab = findViewById(R.id.btnNextVocabReview);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage (Locale.ENGLISH);

                }
            }
        });

        btnNextVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quesIdPos < DbQuery.g_lessonList.get(DbQuery.g_selected_lesson_index).getListVocab().size() - 1){
                    rcvVocabReview.smoothScrollToPosition(quesIdPos + 1);
                }
            }
        });

        reviewVocabularyAdapter = new ReviewVocabularyAdapter(this,
                DbQuery.g_lessonList.get(DbQuery.g_selected_lesson_index).getListVocab(),
                new IClickItemButtonTextToSpeechFastWPLListener() {
                    @Override
                    public void onClickItemBtnFastTextToSpeech(String content) {
                        textToSpeech.setSpeechRate(1f);

                        textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
        reviewVocabularyAdapter.setData(DbQuery.g_lessonList.get(DbQuery.g_selected_lesson_index).getListVocab());


        layoutManager = new CustomGridLayoutManager(this);
        setScroll(true);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvVocabReview.setLayoutManager(layoutManager);

        rcvVocabReview.setAdapter(reviewVocabularyAdapter);
        txtExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewVocabularyActivity.this.finish();
            }
        });

        SetSnapHelper();

    }
    public void setScroll(boolean canScroll){
        layoutManager.setScrollEnabled(canScroll);
        layoutManager.canScrollHorizontally();
    }

    public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollHorizontally() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollHorizontally();
        }
    }

    private void SetSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rcvVocabReview);

        rcvVocabReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());

                quesIdPos = recyclerView.getLayoutManager().getPosition(view);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
}