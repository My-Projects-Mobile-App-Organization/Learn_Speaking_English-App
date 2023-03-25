package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class ResultConversationActivity extends AppCompatActivity {

    Button btnExitConversationResult;
    TextView txtTime, txtAchieve;

    long timeTaken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_conversation);

        btnExitConversationResult = findViewById(R.id.btnExitConversationResult);
        txtAchieve = findViewById(R.id.txtAchievementConverResult);
        txtTime = findViewById(R.id.txtTimeConverResult);

        btnExitConversationResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultConversationActivity.this.finish();
            }
        });

        timeTaken = getIntent().getLongExtra("TIME_TAKEN",0);

        String time = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))
        );

        txtTime.setText(time);

        int countCorrectQuestion = 0;
        for (int i=0;i<DbQuery.g_ConversationList.size();i++){
            if(DbQuery.g_ConversationList.get(i).isStatusConversation()){
                countCorrectQuestion++;
            }
        }
//        int progressConversation = (countCorrectQuestion/DbQuery.g_ConversationList.size())*100;
        int progressConversation = 60;
        txtAchieve.setText(progressConversation + "%");

    }
}