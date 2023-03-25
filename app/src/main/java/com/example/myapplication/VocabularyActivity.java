package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.model.VocabularyWord;
import com.example.myapplication.my_interface.IClickItemButtonTextToSpeechFastWPLListener;
import com.wajahatkarim3.easyflipview.EasyFlipView;

public class VocabularyActivity extends AppCompatActivity {

    EasyFlipView flipView;
    LinearLayout btnLoudSpFront,btnLoudSpBack;
    TextView txtTitleVocabFront,txtPronouVocabFront,txtTitleVocabBack,txtTypeVocabBack,txtExampleVocab;
    ImageView imgVocabFront;
    VocabularyWord vocabularyWord;
    IClickItemButtonTextToSpeechFastWPLListener iClickItemButtonTextToSpeechFastWPLListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        AnhXa();

        vocabularyWord = (VocabularyWord) getIntent().getSerializableExtra("vocabItemChoose");
        if(vocabularyWord==null) return;

        txtTitleVocabFront.setText(vocabularyWord.getTitleVocab());
        txtTitleVocabBack.setText(vocabularyWord.getTitleVocab());
        txtTypeVocabBack.setText(vocabularyWord.getTypeVocab());
        txtPronouVocabFront.setText(vocabularyWord.getPronounceVocab());
        txtExampleVocab.setText("Example: " + "\n" + vocabularyWord.getExampleVocab() + "\n" + vocabularyWord.getTranslateExampleVocab());

        btnLoudSpFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonTextToSpeechFastWPLListener.onClickItemBtnFastTextToSpeech(vocabularyWord.getTitleVocab());
            }
        });
        btnLoudSpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonTextToSpeechFastWPLListener.onClickItemBtnFastTextToSpeech(vocabularyWord.getTitleVocab());
            }
        });
//        holder.imgVocabFront.setImageResource(vocabularyWord.getLinkImgVocab());

        if (flipView.getCurrentFlipState() == EasyFlipView.FlipState.FRONT_SIDE && vocabularyWord.isFlipped()) {
            flipView.setFlipDuration(0);
            flipView.flipTheView();
        } else if (flipView.getCurrentFlipState() == EasyFlipView.FlipState.BACK_SIDE
                && !vocabularyWord.isFlipped()) {
            flipView.setFlipDuration(0);
            flipView.flipTheView();
        }
        flipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vocabularyWord.isFlipped()) {
                    vocabularyWord.setFlipped(false);
                } else {
                    vocabularyWord.setFlipped(true);
                }
                flipView.setFlipDuration(700);
                flipView.flipTheView();
            }
        });

    }

    private void AnhXa() {
        flipView = findViewById(R.id.flipviewVocabAct);
        btnLoudSpBack = findViewById(R.id.btnLoudVocabBack);
        btnLoudSpFront = findViewById(R.id.btnLoudVocabFront);
        imgVocabFront = findViewById(R.id.imgVocabFront);
        txtTitleVocabFront = findViewById(R.id.txTitleVocabFront);
        txtPronouVocabFront = findViewById(R.id.txtVocabPhienam);
        txtTitleVocabBack = findViewById(R.id.txTitleVocabBack);
        txtTypeVocabBack = findViewById(R.id.txtTypeVocab);
        txtExampleVocab = findViewById(R.id.txtExampleVocab);
    }
}