package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.AudioWordAnsMatchFrag;
import com.example.myapplication.model.ContentWordAnsMatchFrag;
import com.example.myapplication.model.QuestionModel;
import com.example.myapplication.my_interface.IClickItemButtonNextQuestionListener;
import com.example.myapplication.my_interface.IClickItemButtonPauseRecordListener;
import com.example.myapplication.my_interface.IClickItemButtonRecordListener;
import com.example.myapplication.my_interface.IClickItemButtonTextToSpeechFastWPLListener;
import com.example.myapplication.my_interface.IClickItemButtonTextToSpeechSlowWPLListener;
import com.wefika.flowlayout.FlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<QuestionModel> mList;
    IClickItemButtonRecordListener iClickItemButtonRecordListener;
    IClickItemButtonPauseRecordListener iClickItemButtonPauseRecordListener;
    IClickItemButtonTextToSpeechFastWPLListener iClickItemButtonTextToSpeechFastWPLListener;
    IClickItemButtonTextToSpeechSlowWPLListener iClickItemButtonTextToSpeechSlowWPLListener;
    IClickItemButtonNextQuestionListener iClickItemButtonNextQuestionListener;
    public QuestionAdapter(Context context, IClickItemButtonRecordListener listener,
                           IClickItemButtonPauseRecordListener pauseRecordListener,
                           IClickItemButtonTextToSpeechFastWPLListener speechWPLListener,
                           IClickItemButtonTextToSpeechSlowWPLListener slowWPLListener,
                           IClickItemButtonNextQuestionListener nextQuestionListener) {
        this.mContext = context;
        this.iClickItemButtonRecordListener = listener;
        this.iClickItemButtonPauseRecordListener = pauseRecordListener;
        this.iClickItemButtonTextToSpeechFastWPLListener = speechWPLListener;
        this.iClickItemButtonTextToSpeechSlowWPLListener = slowWPLListener;
        this.iClickItemButtonNextQuestionListener = nextQuestionListener;
    }

    static Integer currentPosForQuesTypeMatch;
    static int curPosSelectedForQuesTypeMatch;

    Animation smallbigfont;

    public void setData(List<QuestionModel> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    private static int QUESTION_VIEW_TYPE_LISTENING = 1;
    private static int QUESTION_VIEW_TYPE_MATCH_W_AND_S = 2;
    private static int QUESTION_VIEW_TYPE_READING = 3;
    private static int QUESTION_VIEW_TYPE_WORD_PAIRING = 4;
    private static int QUESTION_VIEW_TYPE_WORD_PAIRING_LISTEN = 5;
    private static int QUESTION_VIEW_TYPE_WORD_PAIRING_VOCAB = 6;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(QUESTION_VIEW_TYPE_LISTENING == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_view_type_listening_word,parent,false);
            return new QuesTypeListeningViewHolder(view);
        }else if(QUESTION_VIEW_TYPE_MATCH_W_AND_S == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_view_type_match_w_and_s,parent,false);
            return new QuesTypeMatchViewHolder(view);
        } else if(QUESTION_VIEW_TYPE_READING == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_view_type_reading_word,parent,false);
            return new QuesTypeReadingViewHolder(view);
        } else if(QUESTION_VIEW_TYPE_WORD_PAIRING == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_view_type_word_pairing,parent,false);
            return new QuesTypeWordPairingViewHolder(view);
        } else if(QUESTION_VIEW_TYPE_WORD_PAIRING_LISTEN == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_view_type_word_pairing_listening,parent,false);
            return new QuesTypeWordPairingListenViewHolder(view);
        } else if(QUESTION_VIEW_TYPE_WORD_PAIRING_VOCAB == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_test_view_type_word_pairing_vocab,parent,false);
            return new QuesTypeWordPairingVocabViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        QuestionModel questionModel = mList.get(position);
        if(questionModel==null){
            return;
        }
        smallbigfont = AnimationUtils.loadAnimation(mContext, R.anim.smallbigfont);
        if(QUESTION_VIEW_TYPE_LISTENING == questionModel.getTypeQues()){
            handleFormListenLogic(holder,questionModel,position);

        }else if(QUESTION_VIEW_TYPE_MATCH_W_AND_S == questionModel.getTypeQues()){
            handleFormMatchLogic(holder,questionModel,position);

        } else if(QUESTION_VIEW_TYPE_READING == questionModel.getTypeQues()){
            handleFormReadingLogic(holder,questionModel,position);

        } else if(QUESTION_VIEW_TYPE_WORD_PAIRING == questionModel.getTypeQues()){
            handleFormWordPairingLogic(holder,questionModel,position);

        } else if(QUESTION_VIEW_TYPE_WORD_PAIRING_LISTEN == questionModel.getTypeQues()){
            handleFormWordPairingListeningLogic(holder,questionModel,position);

        } else if(QUESTION_VIEW_TYPE_WORD_PAIRING_VOCAB == questionModel.getTypeQues()){

        }
    }



    @Override
    public int getItemCount() {
        if(mList!=null)
        return mList.size();
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).getTypeQues() == 1){
            return QUESTION_VIEW_TYPE_LISTENING;
        }else if(mList.get(position).getTypeQues() == 2){
            return QUESTION_VIEW_TYPE_MATCH_W_AND_S;
        }else if(mList.get(position).getTypeQues() == 3){
            return QUESTION_VIEW_TYPE_READING;
        }else if(mList.get(position).getTypeQues() == 4){
            return QUESTION_VIEW_TYPE_WORD_PAIRING;
        }else if(mList.get(position).getTypeQues() == 5){
            return QUESTION_VIEW_TYPE_WORD_PAIRING_LISTEN;
        }else {
            return QUESTION_VIEW_TYPE_WORD_PAIRING_VOCAB;
        }
    }

    public class QuesTypeListeningViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout btnAudioQues;
        private Button btnAnsA,btnAnsB,btnAnsC,btnAnsD;
        private Button btnCheckTestLW;

        public QuesTypeListeningViewHolder(@NonNull View itemView) {
            super(itemView);

            btnAudioQues = itemView.findViewById(R.id.layout_btn_audio);
            btnAnsA = itemView.findViewById(R.id.btnAnsA);
            btnAnsB = itemView.findViewById(R.id.btnAnsB);
            btnAnsC = itemView.findViewById(R.id.btnAnsC);
            btnAnsD = itemView.findViewById(R.id.btnAnsD);
            btnCheckTestLW = itemView.findViewById(R.id.btnCheckTestLW);
        }
    }

    public class QuesTypeMatchViewHolder extends RecyclerView.ViewHolder{

        private TextView btnTextQues1, btnTextQues2, btnTextQues3, btnTextQues4;
        private LinearLayout btnAudAns1, btnAudAns2, btnAudAns3, btnAudAns4;
        ImageView imgIconLoud1, imgIconLoud2, imgIconLoud3, imgIconLoud4;
        ImageView imgIconWave1, imgIconWave2, imgIconWave3, imgIconWave4;
        private Button btnCheckTestMW;

        public QuesTypeMatchViewHolder(@NonNull View itemView) {
            super(itemView);

            btnTextQues1 = itemView.findViewById(R.id.txt1ChoiceMWAS);
            btnTextQues2 = itemView.findViewById(R.id.txt2ChoiceMWAS);
            btnTextQues3 = itemView.findViewById(R.id.txt3ChoiceMWAS);
            btnTextQues4 = itemView.findViewById(R.id.txt4ChoiceMWAS);
            btnAudAns1 = itemView.findViewById(R.id.btn1ChoiceMWAS);
            btnAudAns2 = itemView.findViewById(R.id.btn2ChoiceMWAS);
            btnAudAns3 = itemView.findViewById(R.id.btn3ChoiceMWAS);
            btnAudAns4 = itemView.findViewById(R.id.btn4ChoiceMWAS);

            imgIconLoud1 = itemView.findViewById(R.id.imgloudsp1MWAS);
            imgIconLoud2 = itemView.findViewById(R.id.imgloudsp2MWAS);
            imgIconLoud3 = itemView.findViewById(R.id.imgloudsp3MWAS);
            imgIconLoud4 = itemView.findViewById(R.id.imgloudsp4MWAS);
            imgIconWave1 = itemView.findViewById(R.id.imgwave1MWAS);
            imgIconWave2 = itemView.findViewById(R.id.imgwave2MWAS);
            imgIconWave3 = itemView.findViewById(R.id.imgwave3MWAS);
            imgIconWave4 = itemView.findViewById(R.id.imgwave4MWAS);

            btnCheckTestMW = itemView.findViewById(R.id.btnCheckTestMW);
        }
    }

    public class QuesTypeReadingViewHolder extends RecyclerView.ViewHolder{

        LinearLayout btnLoudSpeaker;
        LinearLayout btnMicRecord;
        TextView txtContent;
        ToggleButton toggleButton;

        Button btnCheckTestRW;

        String s;

        public QuesTypeReadingViewHolder(@NonNull View itemView) {
            super(itemView);

            btnLoudSpeaker = itemView.findViewById(R.id.ibtnLoudspRD);
            btnMicRecord = itemView.findViewById(R.id.ibtnMicRD);
            txtContent = itemView.findViewById(R.id.txtContentRD);
            toggleButton = itemView.findViewById(R.id.pauseRD);

            btnCheckTestRW = itemView.findViewById(R.id.btnCheckTestRW);
        }


    }

    public class QuesTypeWordPairingViewHolder extends RecyclerView.ViewHolder{

        TextView textScreen, textQuestion, textTitle;
        EditText editText;
        Button btnReplay;
        Animation smallbigfont;
        FlowLayout flowLayout;

        Button btnCheckTestWP;

        public QuesTypeWordPairingViewHolder(@NonNull View itemView) {
            super(itemView);

            textQuestion = itemView.findViewById(R.id.txtContentQuestionWP);
           // btnReplay = (Button) itemView.findViewById(R.id.btnResetWordPairing);
            editText = (EditText) itemView.findViewById(R.id.editTextAnsWP);
            flowLayout = itemView.findViewById(R.id.wordPairingFlowLayout);
//            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/FredokaOneRegular.ttf");

            btnCheckTestWP = itemView.findViewById(R.id.btnCheckTestWP);
        }
    }

    public class QuesTypeWordPairingListenViewHolder extends RecyclerView.ViewHolder{

        LinearLayout btnFastAudio, btnSlowAudio;
        EditText edtAnsWPL;
        FlowLayout flowLayout;

        Button btnCheckTestWPL;

        public QuesTypeWordPairingListenViewHolder(@NonNull View itemView) {
            super(itemView);

            btnFastAudio = itemView.findViewById(R.id.btnFastAudio);
            btnSlowAudio = itemView.findViewById(R.id.btnSlowAudio);
            edtAnsWPL = itemView.findViewById(R.id.editTextAnsWPL);
            flowLayout = itemView.findViewById(R.id.wordPairingLayoutWPL);

            btnCheckTestWPL = itemView.findViewById(R.id.btnCheckTestWPL);
        }
    }

    public class QuesTypeWordPairingVocabViewHolder extends RecyclerView.ViewHolder{

        TextView textScreen, textQuestion, textTitle;
        EditText editText;
        Button btnReplay;
        Animation smallbigfont;
        FlowLayout flowLayout;

        public QuesTypeWordPairingVocabViewHolder(@NonNull View itemView) {
            super(itemView);

            //btnReplay = (Button) itemView.findViewById(R.id.btnResetWordPairing);
            editText = (EditText) itemView.findViewById(R.id.editTextAnsWPV);
            flowLayout = itemView.findViewById(R.id.wordPairingLayoutWPV);
        }
    }

    private ArrayList<Button> CreateListButtonAnsForQuesTypeListen(QuesTypeListeningViewHolder holder) {
        ArrayList<Button> listBtnAns = new ArrayList<>();
        listBtnAns.add(holder.btnAnsA);
        listBtnAns.add(holder.btnAnsB);
        listBtnAns.add(holder.btnAnsC);
        listBtnAns.add(holder.btnAnsD);
        return listBtnAns;
    }

    private ArrayList<LinearLayout> CreateListAudioForQuesTypeMatch (QuesTypeMatchViewHolder holder) {
        ArrayList<LinearLayout> listBtnAudioChoice = new ArrayList<>();
        listBtnAudioChoice.add(holder.btnAudAns1);
        listBtnAudioChoice.add(holder.btnAudAns2);
        listBtnAudioChoice.add(holder.btnAudAns3);
        listBtnAudioChoice.add(holder.btnAudAns4);

        return listBtnAudioChoice;
    }

    private ArrayList<TextView> CreateListContentForQuesTypeMatch (QuesTypeMatchViewHolder holder) {
        ArrayList<TextView> listContentWordChoice = new ArrayList<>();
        listContentWordChoice.add(holder.btnTextQues1);
        listContentWordChoice.add(holder.btnTextQues2);
        listContentWordChoice.add(holder.btnTextQues3);
        listContentWordChoice.add(holder.btnTextQues4);

        return listContentWordChoice;
    }

    Integer idChoiceFormListen;

    private void handleFormListenLogic(RecyclerView.ViewHolder holder, QuestionModel questionModel, int posQues) {

        ArrayList<ContentWordAnsMatchFrag> listContentQues = questionModel.getListContentQues();
        ArrayList<AudioWordAnsMatchFrag> listChoiceQues = questionModel.getListChoiceQues();
        ArrayList<Integer> listAns =questionModel.getListAnswer();

        Collections.shuffle(listChoiceQues);

        QuesTypeListeningViewHolder quesTypeListeningViewHolder = (QuesTypeListeningViewHolder) holder;

        quesTypeListeningViewHolder.btnCheckTestLW.setEnabled(false);

        quesTypeListeningViewHolder.btnAudioQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MediaPlayer mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                try {
//                    mediaPlayer.setDataSource(questionModel.getListContentQues().get(0).getContent());
//                    mediaPlayer.prepareAsync();
//                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mediaPlayer) {
//                            mediaPlayer.start();
//                        }
//                    });
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
                iClickItemButtonTextToSpeechFastWPLListener.onClickItemBtnFastTextToSpeech(listContentQues.get(0).getContent());
            }
        });
        // code du phong
//            for (int i=0;i<listContentQues.size();i++){
//                int pos = i;
//                ibtnAudioQues.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        MediaPlayer mediaPlayer = new MediaPlayer();
//                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                        try {
//                            mediaPlayer.setDataSource(listContentQues.get(pos).getContent());
//                            mediaPlayer.prepareAsync();
//                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                                @Override
//                                public void onPrepared(MediaPlayer mediaPlayer) {
//                                    mediaPlayer.start();
//                                }
//                            });
//                        }catch (IOException e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
        ArrayList<Button> listBtnAns = CreateListButtonAnsForQuesTypeListen(quesTypeListeningViewHolder);
        for (int i=0;i<questionModel.getListChoiceQues().size();i++){
            listBtnAns.get(i).setText(questionModel.getListChoiceQues().get(i).getAudio());
            int pos = i;
            listBtnAns.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    idChoiceFormListen = new Integer(pos);
                    listBtnAns.get(pos).setBackgroundResource(R.drawable.background_shadow_click_board);
                    listBtnAns.get(pos).setTextColor(Color.parseColor("#0066FF"));
                    quesTypeListeningViewHolder.btnCheckTestLW.setBackgroundResource(R.drawable.background_button_check_green);
                    quesTypeListeningViewHolder.btnCheckTestLW.setTextColor(Color.parseColor("#FFFFFF"));
                    quesTypeListeningViewHolder.btnCheckTestLW.setEnabled(true);
                    for (int j = 0;j<listBtnAns.size();j++){
                        if(j==idChoiceFormListen){
                            continue;
                        }
                        listBtnAns.get(j).setBackgroundResource(R.drawable.background_shadow__board_button);
                        listBtnAns.get(j).setTextColor(Color.BLACK);

                    }
                }
            });
        }


        quesTypeListeningViewHolder.btnCheckTestLW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idChoiceFormListen != null) {
                    if (listChoiceQues.get(idChoiceFormListen).getIdAudio() == listAns.get(0)) {
                        // Toast.makeText(mContext, "đúng rồi", Toast.LENGTH_SHORT).show();
                        listBtnAns.get(idChoiceFormListen).setTextColor(Color.parseColor("#A1100F0F"));
                        SetStatusBGBtnFormListen("correct",listBtnAns.get(idChoiceFormListen));
                        ShowDialog("pass",posQues);
                        questionModel.setStatusQuestion(true);
                        MediaPlayer player = MediaPlayer.create(mContext,R.raw.right_answer_sound_effect);
                        player.start();
                    } else {
                        // Toast.makeText(mContext, "sai rồi", Toast.LENGTH_SHORT).show();
                        ShowDialog("failed",posQues);
                        MediaPlayer player1 = MediaPlayer.create(mContext,R.raw.wrong_answer_sound_effect);
                        player1.start();
                    }
                }
            }
        });


    }
    MediaPlayer mediaPlayerForTypeMatch;

    int countFormMatch = 0;
    private void handleFormMatchLogic(RecyclerView.ViewHolder holder, QuestionModel questionModel, int posQues) {

        ArrayList<ContentWordAnsMatchFrag> listContentQues = questionModel.getListContentQues();
        ArrayList<AudioWordAnsMatchFrag> listChoiceQues = questionModel.getListChoiceQues();
        ArrayList<Integer> listAns = questionModel.getListAnswer();

        QuesTypeMatchViewHolder quesTypeMatchViewHolder = (QuesTypeMatchViewHolder) holder;

        quesTypeMatchViewHolder.btnCheckTestMW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Collections.shuffle(listContentQues);
        Collections.shuffle(listChoiceQues);

        ArrayList<TextView> listTxtWordChoice = CreateListContentForQuesTypeMatch(quesTypeMatchViewHolder);
        ArrayList<LinearLayout> listLayoutAudioChoice = CreateListAudioForQuesTypeMatch(quesTypeMatchViewHolder);

        for (int i=0;i<listTxtWordChoice.size();i++) {
            // gán nội dung từ vựng cho textview
            listTxtWordChoice.get(i).setText(listContentQues.get(i).getContent());
            int pos = i;
            // bắt sự kiện
            listTxtWordChoice.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //nếu chọn lần đầu thì lấy cur pos nếu ko sẽ check match đáp án

                    if(currentPosForQuesTypeMatch != null)
                    {
                        Toast.makeText(mContext, listContentQues.get(pos).getIdContent() + " vs " +  listChoiceQues.get(currentPosForQuesTypeMatch).getIdAudio(), Toast.LENGTH_SHORT).show();
                        if (listContentQues.get(pos).getIdContent() == listChoiceQues.get(currentPosForQuesTypeMatch).getIdAudio()) {
                            listTxtWordChoice.get(pos).setTextColor(Color.parseColor("#A1100F0F"));

                            SetStatusBGTxt("correct",listTxtWordChoice.get(pos));
                            SetStatusBGBtn("correct",listLayoutAudioChoice.get(currentPosForQuesTypeMatch));

                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
                                    SetStatusBGTxt("default",listTxtWordChoice.get(pos));
                                    SetStatusBGBtn("default",listLayoutAudioChoice.get(currentPosForQuesTypeMatch));

                                    listLayoutAudioChoice.get(currentPosForQuesTypeMatch).setEnabled(false);
                                    listTxtWordChoice.get(pos).setEnabled(false);

                                    SetEnableBtn(true,curPosSelectedForQuesTypeMatch,listLayoutAudioChoice);
                                    SetStatusIcon("off",currentPosForQuesTypeMatch,quesTypeMatchViewHolder);
                                    currentPosForQuesTypeMatch = null;

//                                    Toast.makeText(mContext, "đúng rồi", Toast.LENGTH_SHORT).show();
                                    countFormMatch++;
                                    if(countFormMatch==4){
                                        countFormMatch=0;
                                        ShowDialog("pass",posQues);
                                        questionModel.setStatusQuestion(true);
                                        MediaPlayer player = MediaPlayer.create(mContext,R.raw.right_answer_sound_effect);
                                        player.start();
                                    }
//                                    if(mediaPlayerForTypeMatch!=null)
//                                    if (mediaPlayerForTypeMatch.isPlaying())
//                                        mediaPlayerForTypeMatch.stop();
                                }
                            }, 500);


                        } else {
                            SetStatusBGTxt("wrong",listTxtWordChoice.get(pos));
                            SetStatusBGBtn("wrong",listLayoutAudioChoice.get(currentPosForQuesTypeMatch));

                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
//                                    SetStatusBGTxt("default",listTxtWordChoice.get(pos));
//                                    SetStatusBGBtn("default",listLayoutAudioChoice.get(currentPosForQuesTypeMatch));

                                    SetEnableBtn(true,curPosSelectedForQuesTypeMatch,listLayoutAudioChoice);
                                    currentPosForQuesTypeMatch = null;

//                                    Toast.makeText(mContext, "sai rồi", Toast.LENGTH_SHORT).show();
                                    ShowDialog("failed",posQues);
//                                    if (mediaPlayerForTypeMatch.isPlaying())
//                                        mediaPlayerForTypeMatch.stop();

                                    MediaPlayer player1 = MediaPlayer.create(mContext,R.raw.wrong_answer_sound_effect);
                                    player1.start();
                                }
                            }, 500);
                        }
                    }
                }
            });
        }

        // xử lý logic form matching
        for (int i=0;i<listLayoutAudioChoice.size();i++) {
            //listBtnAudioChoice.get(i).setBackgroundResource(R.drawable.ic_loud_speaker);
            int pos1 = i;
            listLayoutAudioChoice.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mediaPlayerForTypeMatch = new MediaPlayer();
//                    mediaPlayerForTypeMatch.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    try {
//                        mediaPlayerForTypeMatch.setDataSource(listChoiceQues.get(pos).getAudio());
//                        mediaPlayerForTypeMatch.prepareAsync();
//                        mediaPlayerForTypeMatch.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                            @Override
//                            public void onPrepared(MediaPlayer mediaPlayer) {
//                                mediaPlayer.start();
//                            }
//                        });
//                    }catch (IOException e){
//                        e.printStackTrace();
//                    }
                    currentPosForQuesTypeMatch = new Integer(pos1);
                    Toast.makeText(mContext, "" + currentPosForQuesTypeMatch, Toast.LENGTH_SHORT).show();
                    SetStatusBGBtn("click", listLayoutAudioChoice.get(currentPosForQuesTypeMatch));
                    for (int j = 0;j<listLayoutAudioChoice.size();j++){
                        if(j==currentPosForQuesTypeMatch){
                            continue;
                        }
                        SetStatusBGBtn("default", listLayoutAudioChoice.get(j));
                    }
                    iClickItemButtonTextToSpeechFastWPLListener.onClickItemBtnFastTextToSpeech(listChoiceQues.get(pos1).getAudio());



//                    if(currentPosForQuesTypeMatch == null){

//                        SetEnableBtn(false, currentPosForQuesTypeMatch,listLayoutAudioChoice);
//                    }

                }
            });
        }
    }

    private void SetStatusIcon(String status, int posIcon, QuesTypeMatchViewHolder holder){
        if(status.equals("off")){
            switch (posIcon){
                case 0:
                    holder.imgIconLoud1.setImageResource(R.drawable.loudsp_off);
                    holder.imgIconWave1.setImageResource(R.drawable.soundwave_off);
                    return;
                case 1:
                    holder.imgIconLoud2.setImageResource(R.drawable.loudsp_off);
                    holder.imgIconWave2.setImageResource(R.drawable.soundwave_off);
                    return;
                case 2:
                    holder.imgIconLoud3.setImageResource(R.drawable.loudsp_off);
                    holder.imgIconWave3.setImageResource(R.drawable.soundwave_off);
                    return;
                case 3:
                    holder.imgIconLoud4.setImageResource(R.drawable.loudsp_off);
                    holder.imgIconWave4.setImageResource(R.drawable.soundwave_off);
                    return;
                default:
                    return;
            }
        }else if(status.equals("on")){
            switch (posIcon){
                case 0:
                    holder.imgIconLoud1.setImageResource(R.drawable.loudsp);
                    holder.imgIconWave1.setImageResource(R.drawable.soundwave);
                    return;
                case 1:
                    holder.imgIconLoud2.setImageResource(R.drawable.loudsp);
                    holder.imgIconWave2.setImageResource(R.drawable.soundwave);
                    return;
                case 2:
                    holder.imgIconLoud3.setImageResource(R.drawable.loudsp);
                    holder.imgIconWave3.setImageResource(R.drawable.soundwave);
                    return;
                case 3:
                    holder.imgIconLoud4.setImageResource(R.drawable.loudsp);
                    holder.imgIconWave4.setImageResource(R.drawable.soundwave);
                    return;
                default:
                    return;
            }
        }
    }

    private void SetStatusBGBtn(String check,LinearLayout layout){
        if(check.equals("click")){

            layout.setBackgroundResource(R.drawable.background_shadow_click_board);
        }else if(check.equals("default")){

            layout.setBackgroundResource(R.drawable.background_shadow_board);
        }else if(check.equals("correct")){

            layout.setBackgroundResource(R.drawable.background_shadow_correct_board);
        }else if(check.equals("wrong")){

            layout.setBackgroundResource(R.drawable.background_shadow_error_board);
        }
    }

    private void SetStatusBGBtnFormListen(String check,Button button){
        if(check.equals("click")){

            button.setBackgroundResource(R.drawable.background_shadow_click_board);
        }else if(check.equals("default")){

            button.setBackgroundResource(R.drawable.background_shadow_board);
        }else if(check.equals("correct")){

            button.setBackgroundResource(R.drawable.background_shadow_correct_board);
        }else if(check.equals("wrong")){

            button.setBackgroundResource(R.drawable.background_shadow_error_board);
        }
    }

    private void SetStatusBGTxt(String check,TextView txt){
        if(check.equals("click")){
            txt.setBackgroundResource(R.drawable.background_shadow_click_board);
            txt.setPadding(40,10,40,10);
        }else if(check.equals("default")){
            txt.setBackgroundResource(R.drawable.background_shadow_board);
            txt.setPadding(40,10,40,10);
        }else if(check.equals("correct")){
            txt.setBackgroundResource(R.drawable.background_shadow_correct_board);
            txt.setPadding(40,10,40,10);
        }else if(check.equals("wrong")){
            txt.setBackgroundResource(R.drawable.background_shadow_error_board);
            txt.setPadding(40,10,40,10);
        }
    }

    private void SetEnableBtn(boolean check, int posTxtCur,ArrayList<LinearLayout> listBtnAudioChoice){
        for (int i=0;i<listBtnAudioChoice.size();i++){
            if(i==posTxtCur){
                continue;
            }
            listBtnAudioChoice.get(i).setEnabled(check);
        }
    }

    private void SetEnableTRUEBtn(int posTxtCur, ArrayList<LinearLayout> listBtnAudioChoice){
        for (int i=0;i<listBtnAudioChoice.size();i++){
            if(i==posTxtCur){
                continue;
            }
            listBtnAudioChoice.get(i).setEnabled(true);
        }
    }

    boolean checkPause = true;
    private void handleFormReadingLogic(RecyclerView.ViewHolder holder, QuestionModel questionModel, int posQues) {

        ArrayList<ContentWordAnsMatchFrag> listContentQues = questionModel.getListContentQues();
        ArrayList<AudioWordAnsMatchFrag> listChoiceQues = questionModel.getListChoiceQues();
        ArrayList<Integer> listAns =questionModel.getListAnswer();

        QuesTypeReadingViewHolder quesTypeReadingViewHolder = (QuesTypeReadingViewHolder) holder;

        quesTypeReadingViewHolder.btnCheckTestRW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Collections.shuffle(listContentQues);
        Collections.shuffle(listChoiceQues);

        // check null or "" trước khi gán
        quesTypeReadingViewHolder.txtContent.setText(listContentQues.get(0).getContent());

        quesTypeReadingViewHolder.btnLoudSpeaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String content = quesTypeReadingViewHolder.txtContent.getText().toString();

//                MediaPlayer mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                try {
//                    mediaPlayer.setDataSource(questionModel.getListChoiceQues().get(0).getAudio());
//                    mediaPlayer.prepareAsync();
//                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mediaPlayer) {
//                            mediaPlayer.start();
//                        }
//                    });
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
                iClickItemButtonTextToSpeechFastWPLListener.onClickItemBtnFastTextToSpeech(questionModel.getListContentQues().get(0).getContent());

//                textToSpeech = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
//                    @Override
//                    public void onInit(int i) {
//                        if(i != TextToSpeech.ERROR){
//                            textToSpeech.setLanguage(Locale.ENGLISH);
//                            textToSpeech.speak(content,TextToSpeech.QUEUE_FLUSH,null);
//                        }
//                    }
//                });
            }
        });
//        quesTypeReadingViewHolder.setUiState(quesTypeReadingViewHolder.STATE_START);
//        LibVosk.setLogLevel(LogLevel.INFO);
//        quesTypeReadingViewHolder.initModel();
       // quesTypeReadingViewHolder.toggleButton.setOnCheckedChangeListener((view, isChecked) -> mTestAcitivity.pause(isChecked));
        quesTypeReadingViewHolder.btnMicRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickItemButtonRecordListener.onClickItemBtnRecord(listContentQues.get(0).getContent(),posQues);
            }
        });
        quesTypeReadingViewHolder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                iClickItemButtonPauseRecordListener.onClickItemPauseBtnRecord(isChecked);
            }
        });

    }

    private int presCounterForTypeWordPairing = 0;
    private int presCounterForTypeWordPairingListening = 0;
    private int maxPresCounterForTypeWordPairing;
    String textAnswer;
    String[] keys;
    private void handleFormWordPairingLogic(RecyclerView.ViewHolder holder, QuestionModel questionModel, int posQues) {
        ArrayList<ContentWordAnsMatchFrag> listContentQues = questionModel.getListContentQues();
        ArrayList<AudioWordAnsMatchFrag> listChoiceQues = questionModel.getListChoiceQues();
        ArrayList<Integer> listAns =questionModel.getListAnswer();


        textAnswer = listChoiceQues.get(0).getAudio();
        keys = textAnswer.split(" ");
        maxPresCounterForTypeWordPairing = keys.length;

        keys = shuffleArray(keys);
        Log.d("AAA",keys.length + "");

        QuesTypeWordPairingViewHolder quesTypeWordPairingViewHolder = (QuesTypeWordPairingViewHolder) holder;

        // set question content
        quesTypeWordPairingViewHolder.textQuestion.setText(listContentQues.get(0).getContent());

        quesTypeWordPairingViewHolder.btnCheckTestWP.setEnabled(false);

        quesTypeWordPairingViewHolder.btnCheckTestWP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doValidateWP(quesTypeWordPairingViewHolder,posQues,questionModel);
            }
        });

        SetupDataAdapterWordPairing(quesTypeWordPairingViewHolder);
    }

    private String[] shuffleArray(String[] keys) {
        Random rand = new Random();
        for (int i = keys.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            String a = keys[index];
            keys[index] = keys[i];
            keys[i] = a;
        }
        return keys;
    }

    private void addViewWP(FlowLayout viewParent, final String text, final EditText editText, QuesTypeWordPairingViewHolder holder) {
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                FlowLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,10);
        layoutParams.gravity = Gravity.CENTER;

        final TextView textView = new TextView(mContext);

        textView.setLayoutParams(layoutParams);
        textView.setBackground(mContext.getResources().getDrawable(R.drawable.background_shadow__board_button));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setPadding(40,40,40,40);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(18);

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/FredokaOneRegular.ttf");

//        textQuestion = (TextView) mView.findViewById(R.id.textQuestion);
//        textScreen = (TextView) mView.findViewById(R.id.textScreen);
//        textTitle = (TextView) mView.findViewById(R.id.textTitle);
//
//        textQuestion.setTypeface(typeface);
//        textScreen.setTypeface(typeface);
//        textTitle.setTypeface(typeface);
//        editText.setTypeface(typeface);
//        textView.setTypeface(typeface);

        textView.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (presCounterForTypeWordPairing < maxPresCounterForTypeWordPairing) {
                    if (presCounterForTypeWordPairing == 0)
                        editText.setText("");

                    editText.setText(editText.getText().toString() + text + " ");
                    textView.startAnimation(smallbigfont);
                    textView.animate().alpha(0).setDuration(300);
                    presCounterForTypeWordPairing++;
                    Log.d("AAA",presCounterForTypeWordPairing+"");
//                    if (presCounterForTypeWordPairing == maxPresCounterForTypeWordPairing) {
//                        doValidateWP(holder);
//                    }
                    if(!holder.btnCheckTestWP.isEnabled()){
                        holder.btnCheckTestWP.setTextColor(Color.parseColor("#FFFFFF"));
                        holder.btnCheckTestWP.setBackgroundResource(R.drawable.background_button_check_green);
                        holder.btnCheckTestWP.setEnabled(true);
                    }
                }
            }
        });


        viewParent.addView(textView);
    }
    private void addViewWPL(FlowLayout viewParent, final String text, final EditText editText, QuesTypeWordPairingListenViewHolder holder) {
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                FlowLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,10);
        layoutParams.gravity = Gravity.CENTER;

        final TextView textView = new TextView(mContext);

        textView.setLayoutParams(layoutParams);
        textView.setBackground(mContext.getResources().getDrawable(R.drawable.background_shadow__board_button));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setPadding(30,30,30,30);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(18);

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/FredokaOneRegular.ttf");

//        textQuestion = (TextView) mView.findViewById(R.id.textQuestion);
//        textScreen = (TextView) mView.findViewById(R.id.textScreen);
//        textTitle = (TextView) mView.findViewById(R.id.textTitle);
//
//        textQuestion.setTypeface(typeface);
//        textScreen.setTypeface(typeface);
//        textTitle.setTypeface(typeface);
//        editText.setTypeface(typeface);
//        textView.setTypeface(typeface);

        textView.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (presCounterForTypeWordPairing < maxPresCounterForTypeWordPairing) {
                    if (presCounterForTypeWordPairing == 0)
                        editText.setText("");

                    editText.setText(editText.getText().toString() + text + " ");
                    textView.startAnimation(smallbigfont);
                    textView.animate().alpha(0).setDuration(300);
                    presCounterForTypeWordPairing++;

//                    if (presCounterForTypeWordPairing == maxPresCounterForTypeWordPairing) {
//                        doValidateWPL(holder);
//                    }
                }

                if(!holder.btnCheckTestWPL.isEnabled()){
                    holder.btnCheckTestWPL.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.btnCheckTestWPL.setBackgroundResource(R.drawable.background_button_check_green);
                    holder.btnCheckTestWPL.setEnabled(true);
                }
            }
        });


        viewParent.addView(textView);
    }
    private void SetupDataAdapterWordPairing(QuesTypeWordPairingViewHolder holder) {
        if(holder.flowLayout == null){
            return;
        }
        presCounterForTypeWordPairing = 0;
        //FlowLayout layout = (FlowLayout) findViewById(R.id.wordPairingLayout);
        holder.flowLayout.removeAllViews();
        keys = shuffleArray(keys);
        holder.editText.setText("");

        if(holder.flowLayout != null && keys.length > 0){
            for (String key : keys){
                addViewWP(holder.flowLayout, key, holder.editText,holder);
            }
        }

    }
    private void SetupDataAdapterWordPairingListening(QuesTypeWordPairingListenViewHolder holder) {
        if(holder.flowLayout == null){
            return;
        }
        presCounterForTypeWordPairing = 0;
        //FlowLayout layout = (FlowLayout) findViewById(R.id.wordPairingLayout);
        holder.flowLayout.removeAllViews();
        keys = shuffleArray(keys);
        holder.edtAnsWPL.setText("");

        if(holder.flowLayout != null && keys.length > 0){
            for (String key : keys){
                addViewWPL(holder.flowLayout, key, holder.edtAnsWPL,holder);
            }
        }

    }
    private void doValidateWP(QuesTypeWordPairingViewHolder holder,int pos,QuestionModel questionModel){
        presCounterForTypeWordPairing = 0;

        if(holder.editText.getText().toString().equals(textAnswer+" ")){
//            Toast.makeText(mContext, "Correct", Toast.LENGTH_SHORT).show();
            ShowDialog("pass",pos);
            questionModel.setStatusQuestion(true);
            MediaPlayer player = MediaPlayer.create(mContext,R.raw.right_answer_sound_effect);
            player.start();
           // SetupDataAdapterWordPairing(holder);
        }else {
//            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            ShowDialog("failed",pos);

            MediaPlayer player1 = MediaPlayer.create(mContext,R.raw.wrong_answer_sound_effect);
            player1.start();
            //SetupDataAdapterWordPairing(holder);
        }

    }
    private void doValidateWPL(QuesTypeWordPairingListenViewHolder holder, int pos, QuestionModel questionModel){
        presCounterForTypeWordPairingListening = 0;

        if(holder.edtAnsWPL.getText().toString().equals(textAnswer+" ")){
//            Toast.makeText(mContext, "Correct", Toast.LENGTH_SHORT).show();
            ShowDialog("pass",pos);

            MediaPlayer player = MediaPlayer.create(mContext,R.raw.right_answer_sound_effect);
            player.start();
           // SetupDataAdapterWordPairingListening(holder);
        }else {
//            Toast.makeText(mContext, "Wrong", Toast.LENGTH_SHORT).show();
            ShowDialog("failed",pos);

            MediaPlayer player1 = MediaPlayer.create(mContext,R.raw.wrong_answer_sound_effect);
            player1.start();
          //  SetupDataAdapterWordPairingListening(holder);
        }

    }


    private void handleFormWordPairingListeningLogic(RecyclerView.ViewHolder holder, QuestionModel questionModel, int posQues) {
        ArrayList<ContentWordAnsMatchFrag> listContentQues = questionModel.getListContentQues();
        ArrayList<AudioWordAnsMatchFrag> listChoiceQues = questionModel.getListChoiceQues();
        ArrayList<Integer> listAns =questionModel.getListAnswer();

        QuesTypeWordPairingListenViewHolder quesTypeWordPairingListenViewHolder = (QuesTypeWordPairingListenViewHolder) holder;

        quesTypeWordPairingListenViewHolder.btnCheckTestWPL.setEnabled(false);

        textAnswer = listContentQues.get(0).getContent();
        keys = textAnswer.split(" ");
        maxPresCounterForTypeWordPairing = keys.length;

        keys = shuffleArray(keys);

         SetupDataAdapterWordPairingListening(quesTypeWordPairingListenViewHolder);

         quesTypeWordPairingListenViewHolder.btnCheckTestWPL.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 doValidateWPL(quesTypeWordPairingListenViewHolder,posQues,questionModel);
             }
         });

        quesTypeWordPairingListenViewHolder.btnFastAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonTextToSpeechFastWPLListener.onClickItemBtnFastTextToSpeech(listContentQues.get(0).getContent());
            }
        });
        quesTypeWordPairingListenViewHolder.btnSlowAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonTextToSpeechSlowWPLListener.onClickItemBtnSlowTextToSpeech(listContentQues.get(0).getContent());


            }
        });
    }


    private void ShowDialog(String choice, int pos){
        final Dialog dialog = new Dialog(mContext);
        dialog.setCanceledOnTouchOutside(false);
        switch (choice){
            case "pass":
                dialog.setContentView(R.layout.layout_dialog_pass_question);

                Button btnNextDialogPass = dialog.findViewById(R.id.btnNextQuestionDialog);
                btnNextDialogPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemButtonNextQuestionListener.iClickBtnNextQuestion(pos);
                        dialog.cancel();
                    }
                });

                break;
            case "failed":
                dialog.setContentView(R.layout.layout_dialog_failed_question);

                Button btnNextDialogFailed = dialog.findViewById(R.id.btnNextQuestionFailedDialog);
                btnNextDialogFailed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemButtonNextQuestionListener.iClickBtnNextQuestion(pos);
                        dialog.cancel();
                    }
                });

                break;
            case "passRes":
                dialog.setContentView(R.layout.layout_dialog_pass_question_show_result);

                Button btnNextDialogPassRes = dialog.findViewById(R.id.btnNextQuestionDialogRes);
                btnNextDialogPassRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemButtonNextQuestionListener.iClickBtnNextQuestion(pos);
                        dialog.cancel();
                    }
                });

                break;
            case "failedRes":
                dialog.setContentView(R.layout.layout_dialog_failed_question_show_result);

                Button btnNextDialogFailedRes = dialog.findViewById(R.id.btnNextQuestionFailedDialogRes);
                btnNextDialogFailedRes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iClickItemButtonNextQuestionListener.iClickBtnNextQuestion(pos);
                        dialog.cancel();
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

}
