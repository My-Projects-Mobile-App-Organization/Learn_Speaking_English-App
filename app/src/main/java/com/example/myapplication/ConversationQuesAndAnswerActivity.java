package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.myapplication.adapter.ConversationQuesAndAnswerAdapter;
import com.example.myapplication.model.ConversationModel;
import com.example.myapplication.model.SimpleSpanBuilder;
import com.example.myapplication.model.StringConversationModel;
import com.example.myapplication.my_interface.IClickItemButtonNextQuestionListener;
import com.example.myapplication.my_interface.IClickItemButtonRecordListener;
import com.example.myapplication.my_interface.IClickItemButtonTextToSpeechSlowWPLListener;

import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.android.RecognitionListener;
import org.vosk.android.SpeechService;
import org.vosk.android.SpeechStreamService;
import org.vosk.android.StorageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ConversationQuesAndAnswerActivity extends AppCompatActivity implements RecognitionListener {

    static private final int STATE_START = 0;
    static private final int STATE_READY = 1;
    static private final int STATE_DONE = 2;
    static private final int STATE_FILE = 3;
    static private final int STATE_MIC = 4;

    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private Model model;
    private SpeechService speechService;
    private SpeechStreamService speechStreamService;
    private TextView resultView;

    RecyclerView rcv_list_conversation;
    ProgressBar pbConversation;
    TextView txtExitConversation;
    Button btnNext;
    TextView txtTitle;

    Integer posIDConversation;
    String resContentQuestion;

    ConversationQuesAndAnswerAdapter conversationQuesAndAnswerAdapter;
    private TextToSpeech textToSpeech;

    ArrayList<ConversationModel> listMain;
    ArrayList<ConversationModel> listOld;

    static int indexOfList = 0;

    private CountDownTimer countDownTimer;
    // lấy time làm bài
    private long timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_ques_and_answer);

        AnhXa();
        setStatusOfBtn(true);



        listMain = new ArrayList<>();
        listOld = getListConversation();

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage (Locale.ENGLISH);

                }
            }
        });

        txtExitConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexOfList = 0;
                ConversationQuesAndAnswerActivity.this.finish();
            }
        });

        pbConversation.setProgress(1);
        pbConversation.setMax(DbQuery.g_ConversationList.size());

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatusOfBtn(false);
                if(indexOfList < listOld.size()){
                    listMain.add(listOld.get(indexOfList));
                    conversationQuesAndAnswerAdapter.notifyDataSetChanged();
                    rcv_list_conversation.smoothScrollToPosition(indexOfList);
                    pbConversation.setProgress(pbConversation.getProgress()+1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(listMain.get(indexOfList-1).getConversationType()==1){
                                setStatusOfBtn(true);
                            }
                        }
                    }, 2000);
                    indexOfList++;
                }else {
                    Intent intent = new Intent(ConversationQuesAndAnswerActivity.this,ResultConversationActivity.class);
                    indexOfList = 0;
                    long totalTime = 30*60*1000;
                    intent.putExtra("TIME_TAKEN",totalTime-timeLeft);
                    startActivity(intent);
                    ConversationQuesAndAnswerActivity.this.finish();
                }

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_list_conversation.setLayoutManager(layoutManager);

        conversationQuesAndAnswerAdapter = new ConversationQuesAndAnswerAdapter(this,
                new IClickItemButtonTextToSpeechSlowWPLListener() {
                    @Override
                    public void onClickItemBtnSlowTextToSpeech(String content) {

//                        if(indexOfList==listOld.size()){
//                            textToSpeech.setSpeechRate(1f);
//                            textToSpeech.speak(listOld.get(listOld.size()-1).getContentConversation(), TextToSpeech.QUEUE_FLUSH, null);
//                        }

                        textToSpeech.setSpeechRate(1f);
                        textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);

                    }
                }, new IClickItemButtonRecordListener() {
            @Override
            public void onClickItemBtnRecord(String contentQuestion, int posQues) {
                if (contentQuestion != "" && contentQuestion != null ) resContentQuestion = contentQuestion;
                posIDConversation = new Integer(posQues);
                recognizeMicrophone();
            }
        }, new IClickItemButtonNextQuestionListener() {
            @Override
            public void iClickBtnNextQuestion(int posQues) {
                if(listMain.get(0).getContentConversationToSpeak()!=""||listMain.get(0).getContentConversationToSpeak()!=null)
                    listMain.get(0).setContentConversationToSpeak("The end.");
                if(textToSpeech.isSpeaking()&&indexOfList==listOld.size()-1){
                    setStatusOfBtn(true);
                    textToSpeech.stop();
                    return;
                }
                posIDConversation = posQues;
//                if(posQues!=listMain.size()-1) {
                    if (listMain.get(posQues).getConversationType() == 3 || listMain.get(posQues).getConversationType() == 2) {
                        listMain.get(posQues).setConversationType(4);
                        textToSpeech.stop();
                    }
//                }
                textToSpeech.stop();
                Log.d("AAAA","index of list " + indexOfList + " size "+listMain.size()+" " + listOld.size());
                setStatusOfBtn(true);
            }
        });

        conversationQuesAndAnswerAdapter.setData(listMain);
        rcv_list_conversation.setAdapter(conversationQuesAndAnswerAdapter);


        startTimer();

        resultView = findViewById(R.id.result_text_conversation);
        setUiState(STATE_START);

        findViewById(R.id.recognize_file_conversation).setOnClickListener(view -> recognizeFile());
        findViewById(R.id.recognize_mic_conversation).setOnClickListener(view -> recognizeMicrophone());
        ((ToggleButton) findViewById(R.id.pause_conversation)).setOnCheckedChangeListener((view, isChecked) -> pause(isChecked));


        LibVosk.setLogLevel(LogLevel.INFO);

        // Check if user has given permission to record audio, init the model after permission is granted
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            initModel();
        }
    }

    private void startTimer() {
        long totalTime = 30 * 60 * 1000;

        countDownTimer = new CountDownTimer(totalTime + 1000,1000) {
            @Override
            public void onTick(long remainingTime) {
                timeLeft = remainingTime;
            }
            @Override
            public void onFinish() {
                Intent intent = new Intent(ConversationQuesAndAnswerActivity.this,ResultConversationActivity.class);

                long totalTime = 30*60*1000;
                indexOfList = 0;
                intent.putExtra("TIME_TAKEN",totalTime-timeLeft);
                startActivity(intent);
                ConversationQuesAndAnswerActivity.this.finish();
            }
        };
        countDownTimer.start();
    }

    private void setStatusOfBtn(boolean status){
        if(status){
            btnNext.setBackgroundResource(R.drawable.background_button_check_green);
            btnNext.setTextColor(Color.parseColor("#FFFFFF"));
            btnNext.setEnabled(true);
            btnNext.setVisibility(View.VISIBLE);
        }else if(!status){
            btnNext.setBackgroundResource(R.drawable.background_shadow_board);
            btnNext.setTextColor(Color.parseColor("#000000"));
            btnNext.setEnabled(false);
            btnNext.setVisibility(View.INVISIBLE);
        }
    }

    private ArrayList<ConversationModel> getListConversation() {
        ArrayList<ConversationModel> list = new ArrayList<>();
        list.add(new ConversationModel("How many people are there in your family?",1));
        list.add(new ConversationModel("There are 5 people in my family: my father, mother, brother, sister, and me.",2));
        list.add(new ConversationModel("Does your family live in a house or an apartment?",1));
        list.add(new ConversationModel("We live in a house in the countryside.",3));

        return list;
    }


    private void initModel() {
        StorageService.unpack(this, "model-en-us", "model",
                (model) -> {
                    this.model = model;
                    setUiState(STATE_READY);
                },
                (exception) -> setErrorState("Failed to unpack the model" + exception.getMessage()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recognizer initialization is a time-consuming and it involves IO,
                // so we execute it in async task
                initModel();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (speechService != null) {
            speechService.stop();
            speechService.shutdown();
        }

        if (speechStreamService != null) {
            speechStreamService.stop();
        }

        //Dont forget to shut down text to speech
        if (textToSpeech != null) {
            textToSpeech.stop ();
            textToSpeech.shutdown ();
        }
    }

    @Override
    public void onPartialResult(String hypothesis) {
        resultView.append(hypothesis + "\n");
    }

    @Override
    public void onResult(String hypothesis) {
        resultView.append(hypothesis + "\n");
    }

    @Override
    public void onFinalResult(String hypothesis) {
        resultView.append(hypothesis + "\n");
//        Log.d("AAA",hypothesis);
        if(hypothesis == null || hypothesis.length()<=0)
            return;
        String res = hypothesis.substring(hypothesis.indexOf(':')+3,hypothesis.lastIndexOf('"'));
        //resultView.append(s);
        Log.d("AAA",res);
        setUiState(STATE_DONE);
        if (speechStreamService != null) {
            speechStreamService = null;
        }
        if(res == null || res.length()<=0) {
            Toast.makeText(ConversationQuesAndAnswerActivity.this, "Mời nói lại!", Toast.LENGTH_SHORT).show();
            return;
        }
        checkResultRecord1(res,resContentQuestion);
    }

    @Override
    public void onError(Exception exception) {
        setErrorState(exception.getMessage());
    }

    @Override
    public void onTimeout() {
        setUiState(STATE_DONE);
    }

    private void setUiState(int state) {
        switch (state) {
            case STATE_START:
                resultView.setText(R.string.preparing);
                resultView.setMovementMethod(new ScrollingMovementMethod());
                findViewById(R.id.recognize_file_conversation).setEnabled(false);
                findViewById(R.id.recognize_mic_conversation).setEnabled(false);
                findViewById(R.id.pause_conversation).setEnabled((false));
                break;
            case STATE_READY:
                resultView.setText(R.string.ready);
                ((Button) findViewById(R.id.recognize_mic_conversation)).setText(R.string.recognize_microphone);
                findViewById(R.id.recognize_file_conversation).setEnabled(true);
                findViewById(R.id.recognize_mic_conversation).setEnabled(true);
                findViewById(R.id.pause_conversation).setEnabled((false));
                break;
            case STATE_DONE:
                ((Button) findViewById(R.id.recognize_file_conversation)).setText(R.string.recognize_file);
                ((Button) findViewById(R.id.recognize_mic_conversation)).setText(R.string.recognize_microphone);
                findViewById(R.id.recognize_file_conversation).setEnabled(true);
                findViewById(R.id.recognize_mic_conversation).setEnabled(true);
                findViewById(R.id.pause_conversation).setEnabled((false));
                break;
            case STATE_FILE:
                ((Button) findViewById(R.id.recognize_file_conversation)).setText(R.string.stop_file);
                resultView.setText(getString(R.string.starting));
                findViewById(R.id.recognize_mic_conversation).setEnabled(false);
                findViewById(R.id.recognize_file_conversation).setEnabled(true);
                findViewById(R.id.pause_conversation).setEnabled((false));
                break;
            case STATE_MIC:
                ((Button) findViewById(R.id.recognize_mic_conversation)).setText(R.string.stop_microphone);
                resultView.setText(getString(R.string.say_something));
                findViewById(R.id.recognize_file_conversation).setEnabled(false);
                findViewById(R.id.recognize_mic_conversation).setEnabled(true);
                findViewById(R.id.pause_conversation).setEnabled((true));

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
    }

    private void setErrorState(String message) {
        resultView.setText(message);
        ((Button) findViewById(R.id.recognize_mic_conversation)).setText(R.string.recognize_microphone);
        findViewById(R.id.recognize_file_conversation).setEnabled(false);
        findViewById(R.id.recognize_mic_conversation).setEnabled(false);
    }

    private void recognizeFile() {
        if (speechStreamService != null) {
            setUiState(STATE_DONE);
            speechStreamService.stop();
            speechStreamService = null;
        } else {
            setUiState(STATE_FILE);
            try {
                Recognizer rec = new Recognizer(model, 16000.f, "[\"one zero zero zero one\", " +
                        "\"oh zero one two three four five six seven eight nine\", \"[unk]\"]");

                InputStream ais = getAssets().open(
                        "10001-90210-01803.wav");
                if (ais.skip(44) != 44) throw new IOException("File too short");

                speechStreamService = new SpeechStreamService(rec, ais, 16000);
                speechStreamService.start(this);
            } catch (IOException e) {
                setErrorState(e.getMessage());
            }
        }
    }

    public void recognizeMicrophone() {
        if (speechService != null) {
            setUiState(STATE_DONE);
            speechService.stop();
            speechService = null;
        } else {
            setUiState(STATE_MIC);
            try {
                Recognizer rec = new Recognizer(model, 16000.0f);
                speechService = new SpeechService(rec, 16000.0f);
                speechService.startListening(this);
            } catch (IOException e) {
                setErrorState(e.getMessage());
                Log.d("AAAA", "recognizeMicrophone: " + e.getMessage());

            }
        }
    }


    public void pause(boolean checked) {
        if (speechService != null) {
            speechService.setPause(checked);
        }
    }

    private void checResultRecord(String contentRecord,String contentQuestion){
//        contentQuestion = questionModel.getListContentQues().txtContent.getText().toString();
//                promptSpeechInput();
//                String contentRecord = quesTypeReadingViewHolder.returnStringAfterFinalRes();

        if(contentRecord==null || contentRecord == ""){
            return;
        }

        contentRecord = upperCaseFirstCharacter(contentRecord);
        contentQuestion = upperCaseFirstCharacter(contentQuestion);
        char[] vocabRecordCharArr = contentRecord.toCharArray();
        char[] vocabDBCharArr = contentQuestion.toCharArray();
        ArrayList<SpannableString> list_spannable_string = new ArrayList<>();

        SpannableString spannable = new SpannableString(contentRecord);
        if(contentRecord.length() > contentQuestion.length()){
            for (int i=0;i<contentQuestion.length();i++){
                if(vocabRecordCharArr[i] == vocabDBCharArr[i]){
                    spannable.setSpan(new ForegroundColorSpan(Color.GREEN), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new UnderlineSpan(), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else {
                    spannable.setSpan(new ForegroundColorSpan(Color.RED), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new UnderlineSpan(), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            spannable.setSpan(new ForegroundColorSpan(Color.RED),contentQuestion.length(),contentRecord.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new UnderlineSpan(),contentQuestion.length(),contentRecord.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            Toast.makeText(this, "Sai rồi", Toast.LENGTH_SHORT).show();
//            ShowDialog("failedRes",spannable);

            MediaPlayer player1 = MediaPlayer.create(ConversationQuesAndAnswerActivity.this,R.raw.wrong_answer_sound_effect);
            player1.start();
            //ShowFailedDialog(spannable);
        }else if(contentRecord.length() < contentQuestion.length()){
            for (int i=0;i<contentRecord.length();i++){
                if(vocabRecordCharArr[i] == vocabDBCharArr[i]){
                    spannable.setSpan(new ForegroundColorSpan(Color.GREEN), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else {
                    spannable.setSpan(new ForegroundColorSpan(Color.RED), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
//            Toast.makeText(this, "Sai rồi", Toast.LENGTH_SHORT).show();
          //  ShowDialog("failedRes",spannable);

            MediaPlayer player1 = MediaPlayer.create(ConversationQuesAndAnswerActivity.this,R.raw.wrong_answer_sound_effect);
            player1.start();
            // ShowFailedDialog(spannable);
        }else {
            boolean passOrFaill = true;
            for (int i=0;i<contentQuestion.length();i++){
                if(vocabRecordCharArr[i] == vocabDBCharArr[i]){
                    spannable.setSpan(new ForegroundColorSpan(Color.GREEN), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else {
                    if(passOrFaill)
                        passOrFaill = false;
                    spannable.setSpan(new ForegroundColorSpan(Color.RED), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
//            Toast.makeText(this, "Đúng rồi", Toast.LENGTH_SHORT).show();

            DbQuery.g_ConversationList.get(posIDConversation).setStatusConversation(true);
           // updateStatusVocabulary(contentQuestion);

            if(passOrFaill){
                MediaPlayer player = MediaPlayer.create(ConversationQuesAndAnswerActivity.this,R.raw.right_answer_sound_effect);
                player.start();
//                ShowDialog("passRes",spannable);
            }
            else   {
                MediaPlayer player1 = MediaPlayer.create(ConversationQuesAndAnswerActivity.this,R.raw.wrong_answer_sound_effect);
                player1.start();
//                ShowDialog("failedRes",spannable);
            }
        }
    }
    boolean statusPassFailed;
    private void checkResultRecord1(String contentRecord,String contentQuestion){
        if(contentRecord==null || contentRecord == ""){
            return;
        }

        contentRecord = upperCaseFirstCharacter(contentRecord);
        contentQuestion = upperCaseFirstCharacter(contentQuestion);
        String[] listStringSplitContentRecord = contentRecord.trim().split(" ");
        String[] listStringSplitContentQuestion = contentQuestion.trim().split(" ");

        ArrayList<SpannableString> list_spannable_string = new ArrayList<>();

        SimpleSpanBuilder ssbTest = new SimpleSpanBuilder(this);

        if(listStringSplitContentRecord.length == listStringSplitContentQuestion.length){

            for (int i=0;i<listStringSplitContentRecord.length;i++){
                char[] vocabRecordCharArr = listStringSplitContentRecord[i].toCharArray();
                char[] vocabDBCharArr = listStringSplitContentQuestion[i].toCharArray();

                if(vocabRecordCharArr.length > vocabDBCharArr.length){
                    SpannableString spannable = statusOfSpannableString(1, vocabRecordCharArr.length, vocabDBCharArr.length, listStringSplitContentRecord[i], listStringSplitContentQuestion[i], vocabRecordCharArr, vocabDBCharArr);
                    list_spannable_string.add(spannable);
                }else if(vocabRecordCharArr.length < vocabDBCharArr.length){
                    SpannableString spannable = statusOfSpannableString(2, vocabRecordCharArr.length, vocabDBCharArr.length, listStringSplitContentRecord[i], listStringSplitContentQuestion[i], vocabRecordCharArr, vocabDBCharArr);
                    list_spannable_string.add(spannable);
                }else {
                    statusPassFailed = true;
                    SpannableString spannable = statusOfSpannableString(3, vocabRecordCharArr.length, vocabDBCharArr.length, listStringSplitContentRecord[i], listStringSplitContentQuestion[i], vocabRecordCharArr, vocabDBCharArr);
                    list_spannable_string.add(spannable);
                }
            }
            if(statusPassFailed){
                MediaPlayer player = MediaPlayer.create(ConversationQuesAndAnswerActivity.this,R.raw.right_answer_sound_effect);
                player.start();
//                ShowDialog("passRes",list_spannable_string);
            }
            else   {
                MediaPlayer player1 = MediaPlayer.create(ConversationQuesAndAnswerActivity.this,R.raw.wrong_answer_sound_effect);
                player1.start();
//                ShowDialog("failedRes",list_spannable_string);
            }
        }else if(listStringSplitContentRecord.length > listStringSplitContentQuestion.length){

            for (int j=0;j<listStringSplitContentQuestion.length;j++){
                char[] vocabRecordCharArr = listStringSplitContentRecord[j].toCharArray();
                char[] vocabDBCharArr = listStringSplitContentQuestion[j].toCharArray();

                if(vocabRecordCharArr.length > vocabDBCharArr.length){
                    SpannableString spannable = statusOfSpannableString(1, vocabRecordCharArr.length, vocabDBCharArr.length,
                            listStringSplitContentRecord[j], listStringSplitContentQuestion[j], vocabRecordCharArr, vocabDBCharArr);
                    list_spannable_string.add(spannable);
                }else if(vocabRecordCharArr.length < vocabDBCharArr.length){
                    SpannableString spannable = statusOfSpannableString(2, vocabRecordCharArr.length, vocabDBCharArr.length,
                            listStringSplitContentRecord[j], listStringSplitContentQuestion[j], vocabRecordCharArr, vocabDBCharArr);
                    list_spannable_string.add(spannable);
                }else {
                    SpannableString spannable = statusOfSpannableString(3, vocabRecordCharArr.length, vocabDBCharArr.length,
                            listStringSplitContentRecord[j], listStringSplitContentQuestion[j], vocabRecordCharArr, vocabDBCharArr);
                    list_spannable_string.add(spannable);
                }
            }

//            ShowDialog("failedRes",list_spannable_string);

            MediaPlayer player1 = MediaPlayer.create(ConversationQuesAndAnswerActivity.this,R.raw.wrong_answer_sound_effect);
            player1.start();
        }else if(listStringSplitContentRecord.length < listStringSplitContentQuestion.length){
            ArrayList<StringConversationModel> listString = new ArrayList<>();
            for (int n=0;n<listStringSplitContentQuestion.length;n++){
                listString.add(new StringConversationModel(listStringSplitContentQuestion[n], false));
            }
            for (int o=0;o<listStringSplitContentRecord.length;o++){
                for (int p=0;p<listString.size();p++){
                    Log.d("C",listStringSplitContentRecord[o].length() + " vs " + listString.get(p).getContent().length());
                    Log.d("C",listStringSplitContentRecord[o] + " vs " + listString.get(p).getContent());

                    if(listStringSplitContentRecord[o].equals(listString.get(p).getContent())){
                        listString.get(p).setStatus(true);
                        break;
                    }
                }
            }
            for (StringConversationModel stringConversationModel : listString){
                if(stringConversationModel.isStatus()){
//                    SpannableString spannableString = new SpannableString(stringConversationModel.getContent());
//                    spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, stringConversationModel.getContent().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    list_spannable_string.add(spannableString);
                    ssbTest.append(" " + stringConversationModel.getContent(),1);
                }else if(!stringConversationModel.isStatus()){
//                    SpannableString spannableString = new SpannableString(stringConversationModel.getContent());
//                    spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, stringConversationModel.getContent().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    list_spannable_string.add(spannableString);
                    ssbTest.append(" " + stringConversationModel.getContent(),4);
                }
            }
            for (StringConversationModel s:
                 listString) {
                Log.d("B",s.toString());
            }

            ShowDialog("failedRes",ssbTest);

            MediaPlayer player1 = MediaPlayer.create(ConversationQuesAndAnswerActivity.this,R.raw.wrong_answer_sound_effect);
            player1.start();

        }

        Log.d("AAA",contentRecord);
    }

    private SpannableString statusOfSpannableString(int status, int lengthRecord, int lengthQuestion, String recordString, String questionString, char[] records, char[] questions){
        SpannableString spannableString = new SpannableString(questionString);
        switch (status){
            case 1:
                for (int i=0;i<lengthQuestion;i++){
                    if(records[i] == questions[i]){
                        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(new UnderlineSpan(), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }else {
                        spannableString.setSpan(new ForegroundColorSpan(Color.RED), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString.setSpan(new UnderlineSpan(), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                break;
            case 2:
                for (int i=0;i<lengthRecord;i++){
                    if(records[i] == questions[i]){
                        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }else {

                        spannableString.setSpan(new ForegroundColorSpan(Color.RED), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                spannableString.setSpan(new ForegroundColorSpan(Color.RED),recordString.length(),questionString.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new UnderlineSpan(),recordString.length(),questionString.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                break;
            case 3:
                for (int i=0;i<lengthQuestion;i++){
                    if(records[i] == questions[i]){
                        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }else {
                        if(statusPassFailed)
                            statusPassFailed = false;
                        spannableString.setSpan(new ForegroundColorSpan(Color.RED), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                break;

            default:
                break;
        }
        return spannableString;
    }

    private String upperCaseFirstCharacter(String s){
        String firstLetter = s.substring(0, 1);

        String remainingLetters = s.substring(1, s.length());

        firstLetter = firstLetter.toUpperCase();

        s = firstLetter + remainingLetters;

        return s;
    }

    private void updateStatusVocabulary(String vocabTitle){
        vocabTitle = vocabTitle.toLowerCase();
        for (int i=0;i<DbQuery.g_lessonList.get(DbQuery.g_selected_lesson_index).getListVocab().size();i++){
            if(DbQuery.g_lessonList.get(DbQuery.g_selected_lesson_index).getListVocab().get(i).getTitleVocab()==vocabTitle){
                DbQuery.g_lessonList.get(DbQuery.g_selected_lesson_index).getListVocab().get(i).setStatusVocab(true);
                break;
            }
        }
    }

    private void ShowDialog(String choice, SimpleSpanBuilder ssb){
//        ArrayList<SpannableString> list_answer
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        if(listMain!=null && listMain.size()>0 &&listOld!=null && listOld.size()>0)
        switch (choice){
            case "passRes":
                dialog.setContentView(R.layout.layout_dialog_pass_question_show_result);

                TextView txtContentDialogRes = dialog.findViewById(R.id.contentTxtDialogRes);
//                if(list_answer.size()>0) {
//                    txtContentDialogRes.setText(list_answer.get(0));
//                    for (int i = 1; i < list_answer.size(); i++) {
//                        txtContentDialogRes.append(" " + list_answer.get(i));
//                    }
//                }else {
//                    txtContentDialogRes.setText(list_answer.get(0));
//                }
                Button btnNextDialogPassRes = dialog.findViewById(R.id.btnNextQuestionDialogRes);
                btnNextDialogPassRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (posIDConversation != null) {
                            if (posIDConversation != null) {
                                if (listMain.get(posIDConversation).getConversationType() == 3 || listMain.get(posIDConversation).getConversationType() == 2) {
                                    listMain.get(posIDConversation).setConversationType(4);
                                    textToSpeech.stop();
                                }
                                Log.d("AAAA", "index of list " + indexOfList + " size " + listMain.size() + " " + listOld.size());
                                setStatusOfBtn(true);
                                dialog.cancel();
                            }
                        }
                    }
                });

                break;
            case "failedRes":
                dialog.setContentView(R.layout.layout_dialog_failed_question_show_result);

                TextView txtContentFailedDialogRes = dialog.findViewById(R.id.contentTxtFailedDialogRes);
//                if(list_answer.size()>0) {
//                    txtContentFailedDialogRes.setText(list_answer.get(0));
//                    for (int i = 1; i < list_answer.size(); i++) {
//                        txtContentFailedDialogRes.append(" " + list_answer.get(i));
//                    }
//                }else {
//                    txtContentFailedDialogRes.setText(list_answer.get(0));
//                }
                txtContentFailedDialogRes.setText(ssb.build());
                Button btnNextDialogFailedRes = dialog.findViewById(R.id.btnNextQuestionFailedDialogRes);
                btnNextDialogFailedRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(posIDConversation != null){
                            if (listMain.get(posIDConversation).getConversationType() == 3 || listMain.get(posIDConversation).getConversationType() == 2) {
                                listMain.get(posIDConversation).setConversationType(4);
                                textToSpeech.stop();
                            }
                            Log.d("AAAA","index of list " + indexOfList + " size "+listMain.size()+" " + listOld.size());
                            setStatusOfBtn(true);
                            dialog.cancel();
                        }
                    }
                });

                break;
            default:
                break;
        }

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.0f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void AnhXa() {
        rcv_list_conversation = findViewById(R.id.rcv_conversation_view);
        pbConversation = findViewById(R.id.progressBarConversation);
        btnNext = findViewById(R.id.btnCheckConversation);
        txtExitConversation = findViewById(R.id.txtExitConversation);
        txtTitle = findViewById(R.id.txtTitleTopicConversation);
    }
}