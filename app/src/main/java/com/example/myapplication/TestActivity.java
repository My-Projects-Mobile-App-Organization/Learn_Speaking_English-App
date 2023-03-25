package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
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

import com.example.myapplication.adapter.QuestionAdapter;
import com.example.myapplication.model.AudioWordAnsMatchFrag;
import com.example.myapplication.model.ContentWordAnsMatchFrag;
import com.example.myapplication.model.QuestionModel;
import com.example.myapplication.my_interface.IClickItemButtonNextQuestionListener;
import com.example.myapplication.my_interface.IClickItemButtonPauseRecordListener;
import com.example.myapplication.my_interface.IClickItemButtonRecordListener;
import com.example.myapplication.my_interface.IClickItemButtonTextToSpeechFastWPLListener;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TestActivity extends AppCompatActivity implements RecognitionListener {

    static private final int STATE_START = 0;
    static private final int STATE_READY = 1;
    static private final int STATE_DONE = 2;
    static private final int STATE_FILE = 3;
    static private final int STATE_MIC = 4;

    String resContentQuestion;
    Integer posIDQuestion;
    /* Used to handle permission request */
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private Model model;
    private SpeechService speechService;
    private SpeechStreamService speechStreamService;
    private TextView resultView;

    TextToSpeech textToSpeech;

    RecyclerView testView;
    ProgressBar progressBar;
    TextView txtExitTest;
    Button btnCheck;

    List<QuestionModel> listQues;
    QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        AnhXa();


        txtExitTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickExitTest();
                onBackPressed();
            }
        });

        listQues = new ArrayList<>();

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage (Locale.ENGLISH);

                }
            }
        });
        questionAdapter = new QuestionAdapter(this, new IClickItemButtonRecordListener() {
            @Override
            public void onClickItemBtnRecord(String contentQuestion, int posQues) {
                if (contentQuestion != "") resContentQuestion = contentQuestion;
                posIDQuestion = new Integer(posQues);
                recognizeMicrophone();
            }
        }, new IClickItemButtonPauseRecordListener() {
            @Override
            public void onClickItemPauseBtnRecord(boolean isChecked) {
                pause(isChecked);
            }
        }, new IClickItemButtonTextToSpeechFastWPLListener() {
            @Override
            public void onClickItemBtnFastTextToSpeech(String content) {

                textToSpeech.setSpeechRate(1f);

                textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, new IClickItemButtonTextToSpeechSlowWPLListener() {
            @Override
            public void onClickItemBtnSlowTextToSpeech(String content) {
                textToSpeech.setSpeechRate(0.5f);
                textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, null);
            }
        }, new IClickItemButtonNextQuestionListener() {
            @Override
            public void iClickBtnNextQuestion(int posQues) {
                posIDQuestion = new Integer(posQues);
                setClickBtnNextQuestion(posQues);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        testView.setLayoutManager(layoutManager);

        Collections.shuffle(DbQuery.g_quesList);
//        listQues = getListQuestion();
        for (int i=0;i<5;i++){
            if(DbQuery.g_quesList.get(0).getTypeQues()!=3){
                break;
            }
            Collections.shuffle(DbQuery.g_quesList);
        }

        questionAdapter.setData(DbQuery.g_quesList);
//        questionAdapter.setData(listQues);
        testView.setAdapter(questionAdapter);

        progressBar.setProgress(1);
        progressBar.setMax(DbQuery.g_quesList.size());

        SetSnapHelper();

        resultView = findViewById(R.id.result_text);
        setUiState(STATE_START);

        findViewById(R.id.recognize_file).setOnClickListener(view -> recognizeFile());
        findViewById(R.id.recognize_mic).setOnClickListener(view -> recognizeMicrophone());
        ((ToggleButton) findViewById(R.id.pause)).setOnCheckedChangeListener((view, isChecked) -> pause(isChecked));

        LibVosk.setLogLevel(LogLevel.INFO);

        // Check if user has given permission to record audio, init the model after permission is granted
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
        } else {
            initModel();
        }
    }

    private void onClickExitTest() {

    }

    private void SetSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(testView);

        testView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());

                //quesId = recyclerView.getLayoutManager().getPosition(view);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private List<QuestionModel> getListQuestion() {
        List<QuestionModel> list = new ArrayList<>();

//        // Type WordPairing Listening
//        ArrayList<ContentWordAnsMatchFrag> list5a = new ArrayList<>();
//        list5a.add(new ContentWordAnsMatchFrag("How about",1));
//        ArrayList<AudioWordAnsMatchFrag> list5b = new ArrayList<>();
//
//        ArrayList<Integer> list5c = new ArrayList<>();
//
//        list.add(new QuestionModel(5,"Ques 5",list5a,list5b,list5c));

//        // Type WordPairing
//        ArrayList<ContentWordAnsMatchFrag> list4a = new ArrayList<>();
//        list4a.add(new ContentWordAnsMatchFrag("Abide by",1));
//        ArrayList<AudioWordAnsMatchFrag> list4b = new ArrayList<>();
//
//        ArrayList<Integer> list4c = new ArrayList<>();
//
//        list.add(new QuestionModel(4,"Ques 4",list4a,list4b,list4c));

//        // Type Reading
//        ArrayList<ContentWordAnsMatchFrag> list3a = new ArrayList<>();
//        list3a.add(new ContentWordAnsMatchFrag("Abide by",1));
//        ArrayList<AudioWordAnsMatchFrag> list3b = new ArrayList<>();
//        list3b.add(new AudioWordAnsMatchFrag("https://600tuvungtoeic.com/audio/abide_by.mp3",1));
//        ArrayList<Integer> list3c = new ArrayList<>();
//
//        list.add(new QuestionModel(3,"Ques 3",list3a,list3b,list3c));


        // Type Matching
        ArrayList<ContentWordAnsMatchFrag> list2a = new ArrayList<>();
        list2a.add(new ContentWordAnsMatchFrag("Abide by",1));
        list2a.add(new ContentWordAnsMatchFrag("Agreement",2));
        list2a.add(new ContentWordAnsMatchFrag("Assurance",3));
        list2a.add(new ContentWordAnsMatchFrag("Cancellation",4));
        ArrayList<AudioWordAnsMatchFrag> list2b = new ArrayList<>();
        list2b.add(new AudioWordAnsMatchFrag("Abide by",1));
        list2b.add(new AudioWordAnsMatchFrag("Agreement",2));
        list2b.add(new AudioWordAnsMatchFrag("Assurance",3));
        list2b.add(new AudioWordAnsMatchFrag("Cancellation",4));
        ArrayList<Integer> list2c = new ArrayList<>();

        list.add(new QuestionModel(2,"Ques 2",list2a,list2b,list2c));

        // Type Listening
        ArrayList<ContentWordAnsMatchFrag> list1a = new ArrayList<>();
        list1a.add(new ContentWordAnsMatchFrag("https://600tuvungtoeic.com/audio/abide_by.mp3",1));
        ArrayList<AudioWordAnsMatchFrag> list1b = new ArrayList<>();
        list1b.add(new AudioWordAnsMatchFrag("Abide by",1));
        list1b.add(new AudioWordAnsMatchFrag("Agreement",2));
        list1b.add(new AudioWordAnsMatchFrag("Assurance",3));
        list1b.add(new AudioWordAnsMatchFrag("Cancellation",4));ArrayList<Integer> list1c = new ArrayList<>();
        list1c.add(1);
        list.add(new QuestionModel(1,"Ques 1",list1a,list1b,list1c));




        return list;
    }

    private void AnhXa() {
        testView = findViewById(R.id.rcv_test_view);
        progressBar = findViewById(R.id.progressBar);
        btnCheck = findViewById(R.id.btnCheckTest);
        txtExitTest = findViewById(R.id.txtExitTest);
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
            Toast.makeText(TestActivity.this, "Mời nói lại!", Toast.LENGTH_SHORT).show();
            return;
        }
        checResultRecord(res,resContentQuestion);
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
                findViewById(R.id.recognize_file).setEnabled(false);
                findViewById(R.id.recognize_mic).setEnabled(false);
                findViewById(R.id.pause).setEnabled((false));
                break;
            case STATE_READY:
                resultView.setText(R.string.ready);
                ((Button) findViewById(R.id.recognize_mic)).setText(R.string.recognize_microphone);
                findViewById(R.id.recognize_file).setEnabled(true);
                findViewById(R.id.recognize_mic).setEnabled(true);
                findViewById(R.id.pause).setEnabled((false));
                break;
            case STATE_DONE:
                ((Button) findViewById(R.id.recognize_file)).setText(R.string.recognize_file);
                ((Button) findViewById(R.id.recognize_mic)).setText(R.string.recognize_microphone);
                findViewById(R.id.recognize_file).setEnabled(true);
                findViewById(R.id.recognize_mic).setEnabled(true);
                findViewById(R.id.pause).setEnabled((false));
                break;
            case STATE_FILE:
                ((Button) findViewById(R.id.recognize_file)).setText(R.string.stop_file);
                resultView.setText(getString(R.string.starting));
                findViewById(R.id.recognize_mic).setEnabled(false);
                findViewById(R.id.recognize_file).setEnabled(true);
                findViewById(R.id.pause).setEnabled((false));
                break;
            case STATE_MIC:
                ((Button) findViewById(R.id.recognize_mic)).setText(R.string.stop_microphone);
                resultView.setText(getString(R.string.say_something));
                findViewById(R.id.recognize_file).setEnabled(false);
                findViewById(R.id.recognize_mic).setEnabled(true);
                findViewById(R.id.pause).setEnabled((true));

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + state);
        }
    }

    private void setErrorState(String message) {
        resultView.setText(message);
        ((Button) findViewById(R.id.recognize_mic)).setText(R.string.recognize_microphone);
        findViewById(R.id.recognize_file).setEnabled(false);
        findViewById(R.id.recognize_mic).setEnabled(false);
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

    private String upperCaseFirstCharacter(String s){
        String firstLetter = s.substring(0, 1);

        String remainingLetters = s.substring(1, s.length());

        firstLetter = firstLetter.toUpperCase();

        s = firstLetter + remainingLetters;

        return s;
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

        SpannableString spannable = new SpannableString(contentRecord);
        if(contentRecord.length() > contentQuestion.length()){
            for (int i=0;i<contentQuestion.length();i++){
                if(vocabRecordCharArr[i] == vocabDBCharArr[i]){
                    spannable.setSpan(new ForegroundColorSpan(Color.GREEN), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else {
                    spannable.setSpan(new ForegroundColorSpan(Color.RED), i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            spannable.setSpan(new ForegroundColorSpan(Color.RED),contentQuestion.length(),contentRecord.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            Toast.makeText(this, "Sai rồi", Toast.LENGTH_SHORT).show();
            ShowDialog("failedRes",spannable);

            MediaPlayer player1 = MediaPlayer.create(TestActivity.this,R.raw.wrong_answer_sound_effect);
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
            ShowDialog("failedRes",spannable);

            MediaPlayer player1 = MediaPlayer.create(TestActivity.this,R.raw.wrong_answer_sound_effect);
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
            ShowDialog("passRes",spannable);
            if(posIDQuestion!=null)
            DbQuery.g_quesList.get(posIDQuestion).setStatusQuestion(true);
            updateStatusVocabulary(contentQuestion);
            MediaPlayer player = MediaPlayer.create(TestActivity.this,R.raw.right_answer_sound_effect);
            player.start();
//                        if(passOrFaill)
//                            ShowCorrectDialog(vocabId);
//                        else   ShowFailedDialog(spannable);
        }
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

    private void ShowDialog(String choice, SpannableString answer){

        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        switch (choice){
            case "pass":
                dialog.setContentView(R.layout.layout_dialog_pass_question);

                Button btnNextDialogPass = dialog.findViewById(R.id.btnNextQuestionDialog);
                btnNextDialogPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(posIDQuestion != null){
                            setClickBtnNextQuestion(posIDQuestion);
                            dialog.cancel();
                        }
                    }
                });

                break;
            case "failed":
                dialog.setContentView(R.layout.layout_dialog_failed_question);

                Button btnNextDialogFailed = dialog.findViewById(R.id.btnNextQuestionFailedDialog);
                btnNextDialogFailed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(posIDQuestion != null){
                            setClickBtnNextQuestion(posIDQuestion);
                            dialog.cancel();
                        }
                    }
                });

                break;
            case "passRes":
                dialog.setContentView(R.layout.layout_dialog_pass_question_show_result);

                TextView txtContentDialogRes = dialog.findViewById(R.id.contentTxtDialogRes);
                txtContentDialogRes.setText(answer);

                Button btnNextDialogPassRes = dialog.findViewById(R.id.btnNextQuestionDialogRes);
                btnNextDialogPassRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(posIDQuestion != null){
                            setClickBtnNextQuestion(posIDQuestion);
                            dialog.cancel();
                        }
                    }
                });

                break;
            case "failedRes":
                dialog.setContentView(R.layout.layout_dialog_failed_question_show_result);

                TextView txtContentFailedDialogRes = dialog.findViewById(R.id.contentTxtFailedDialogRes);
                txtContentFailedDialogRes.setText(answer);
                Button btnNextDialogFailedRes = dialog.findViewById(R.id.btnNextQuestionFailedDialogRes);
                btnNextDialogFailedRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(posIDQuestion != null){
                            setClickBtnNextQuestion(posIDQuestion);
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

    private void setClickBtnNextQuestion(int posID){
        if(posID<DbQuery.g_quesList.size()-1){
            testView.smoothScrollToPosition(posID+1);
            progressBar.setProgress(progressBar.getProgress()+1);
        }else if(posID==DbQuery.g_quesList.size()-1){
            Intent intent = new Intent(TestActivity.this,ResultTestActivity.class);
            startActivity(intent);
            TestActivity.this.finish();
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_cancel_test, null);

        Button btnCancel = view.findViewById(R.id.btn_Cancel_Dialog);
        Button btnConfirm = view.findViewById(R.id.btn_Exit_Test);

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //countDownTimer.cancel();
                alertDialog.cancel();

                TestActivity.this.finish();
            }
        });
        alertDialog.show();
    }
}