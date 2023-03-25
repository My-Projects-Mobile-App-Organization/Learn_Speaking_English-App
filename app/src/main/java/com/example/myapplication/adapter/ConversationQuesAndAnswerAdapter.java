package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.ConversationModel;
import com.example.myapplication.model.QuestionModel;
import com.example.myapplication.model.StringChoiceConversation;
import com.example.myapplication.my_interface.IClickItemButtonNextQuestionListener;
import com.example.myapplication.my_interface.IClickItemButtonRecordListener;
import com.example.myapplication.my_interface.IClickItemButtonTextToSpeechSlowWPLListener;

import java.util.ArrayList;
import java.util.Random;

public class ConversationQuesAndAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context mContext;
    ArrayList<ConversationModel> mList;

    IClickItemButtonTextToSpeechSlowWPLListener iClickItemButtonTextToSpeechSlowWPLListener;
    IClickItemButtonRecordListener iClickItemButtonRecordListener;
    IClickItemButtonNextQuestionListener iClickItemButtonNextQuestionListener;

    public ConversationQuesAndAnswerAdapter(Context mContext, IClickItemButtonTextToSpeechSlowWPLListener slowWPLListener,
                                            IClickItemButtonRecordListener recordListener,
                                            IClickItemButtonNextQuestionListener nextQuestionListener) {
        this.mContext = mContext;
        this.iClickItemButtonTextToSpeechSlowWPLListener = slowWPLListener;
        this.iClickItemButtonRecordListener = recordListener;
        this.iClickItemButtonNextQuestionListener = nextQuestionListener;
    }

    public void setData(ArrayList<ConversationModel> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    private static int CONVERSATION_VIEW_TYPE_QUESTION = 1;
    private static int CONVERSATION_VIEW_TYPE_ANSWER_READING = 2;
    private static int CONVERSATION_VIEW_TYPE_ANSWER_LISTENING = 3;
    private static int CONVERSATION_VIEW_TYPE_ANSWER_DEFAULT = 4;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(CONVERSATION_VIEW_TYPE_QUESTION == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_question_layout,parent,false);
            return new ConversationQuesAndAnswerAdapter.ConversationTypeQuestionViewHolder(view);
        }else if(CONVERSATION_VIEW_TYPE_ANSWER_READING == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_answer_form_reading_layout,parent,false);
            return new ConversationQuesAndAnswerAdapter.ConversationTypeAnswerReadingViewHolder(view);
        } else if(CONVERSATION_VIEW_TYPE_ANSWER_LISTENING == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_answer_form_listening_layout,parent,false);
            return new ConversationQuesAndAnswerAdapter.ConversationTypeAnswerListeingViewHolder(view);
        }else if(CONVERSATION_VIEW_TYPE_ANSWER_DEFAULT == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation_answer_default_layout,parent,false);
            return new ConversationQuesAndAnswerAdapter.ConversationTypeAnswerDefaultViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ConversationModel conversationModel = mList.get(position);
        if(conversationModel==null){
            return;
        }

        if(CONVERSATION_VIEW_TYPE_QUESTION == conversationModel.getConversationType()){
            handleConversationQuestionLogic(holder,conversationModel,position);

        }else if(CONVERSATION_VIEW_TYPE_ANSWER_READING == conversationModel.getConversationType()){
            handleConversationAnswerTypeReadingLogic(holder,conversationModel,position);

        } else if(CONVERSATION_VIEW_TYPE_ANSWER_LISTENING == conversationModel.getConversationType()){
            handleConversationAnswerTypeListeningLogic(holder,conversationModel,position);

        }else if(CONVERSATION_VIEW_TYPE_ANSWER_DEFAULT == conversationModel.getConversationType()){
            handleConversationAnswerTypeDefaultLogic(holder,conversationModel,position);

        }
    }

    private void handleConversationAnswerTypeDefaultLogic(RecyclerView.ViewHolder holder, ConversationModel conversationModel, int position) {
        ConversationTypeAnswerDefaultViewHolder conversationTypeAnswerDefaultViewHolder = (ConversationTypeAnswerDefaultViewHolder) holder;

        if(conversationModel.getConversationType()==4) {
            conversationTypeAnswerDefaultViewHolder.imgCharacterAnsDefault.setImageResource(R.drawable.ic__boys_2);
        }
        conversationTypeAnswerDefaultViewHolder.txtContentChatBoxAnsDefault.setText(conversationModel.getContentConversation());
        conversationTypeAnswerDefaultViewHolder.imgCharacterAnsDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonTextToSpeechSlowWPLListener.onClickItemBtnSlowTextToSpeech(conversationModel.getContentConversation());
            }
        });
    }

    private void handleConversationAnswerTypeListeningLogic(RecyclerView.ViewHolder holder, ConversationModel conversationModel, int position) {
        ConversationTypeAnswerListeingViewHolder conversationTypeAnswerListeingViewHolder = (ConversationTypeAnswerListeingViewHolder) holder;

        conversationTypeAnswerListeingViewHolder.btnAnswerA.setVisibility(View.VISIBLE);
        conversationTypeAnswerListeingViewHolder.btnAnswerB.setVisibility(View.VISIBLE);
        conversationTypeAnswerListeingViewHolder.btnAnswerC.setVisibility(View.VISIBLE);

        iClickItemButtonTextToSpeechSlowWPLListener.onClickItemBtnSlowTextToSpeech(conversationModel.getContentConversationToSpeak());

        if(conversationModel.getConversationType()==3) {
            conversationTypeAnswerListeingViewHolder.imgCharacterAnsListen.setImageResource(R.drawable.ic__boys_2);
        }

        conversationTypeAnswerListeingViewHolder.imgCharacterAnsListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonTextToSpeechSlowWPLListener.onClickItemBtnSlowTextToSpeech(conversationModel.getContentConversationToSpeak());
            }
        });
        ArrayList<Button> listBtnAnswer = CreateListButtonAnsForAnsTypeListen(conversationTypeAnswerListeingViewHolder);
        for (int i=0;i<listBtnAnswer.size();i++){
            listBtnAnswer.get(i).setBackgroundResource(R.drawable.background_shadow_board);
            listBtnAnswer.get(i).setTextColor(Color.BLACK);
        }

        String contentConversation = conversationModel.getContentConversation().trim();
        if(contentConversation==null || contentConversation=="")
            return;
        String[] keys = contentConversation.split(" ");

        ArrayList<StringChoiceConversation> listChoice = new ArrayList<>();
        listChoice.add(new StringChoiceConversation("The",false));
        listChoice.add(new StringChoiceConversation("near",false));

        int idAnswer;
        if(keys.length>0) {
            Random random = new Random();
            idAnswer = random.nextInt(keys.length-1);
            listChoice.add(new StringChoiceConversation(keys[idAnswer],true));
            keys[idAnswer] = "_______";
        }

        String contentConversationNew = convertStringArrayToString(keys," ");
        conversationTypeAnswerListeingViewHolder.txtContentChatBoxAnsListen.setText(contentConversationNew);



        if(keys.length>0) {
            for (int i = 0; i < listChoice.size(); i++) {
                listBtnAnswer.get(i).setText(listChoice.get(i).getContentChoice());
                int pos = i;
                listBtnAnswer.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (listChoice.get(pos).isAnswer()) {
                            listBtnAnswer.get(pos).setTextColor(Color.parseColor("#A1100F0F"));
                            SetStatusBGBtnFormListen("correct",listBtnAnswer.get(pos));

                            MediaPlayer player = MediaPlayer.create(mContext,R.raw.right_answer_sound_effect);
                            player.start();

                            for (int j=0;j<listBtnAnswer.size();j++){
                                if(listBtnAnswer.get(j).isEnabled() == false){
                                    conversationModel.setStatusConversation(false);
                                    break;
                                }
                                conversationModel.setStatusConversation(true);
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    conversationTypeAnswerListeingViewHolder.btnAnswerA.setVisibility(View.GONE);
                                    conversationTypeAnswerListeingViewHolder.btnAnswerB.setVisibility(View.GONE);
                                    conversationTypeAnswerListeingViewHolder.btnAnswerC.setVisibility(View.GONE);

                                    conversationTypeAnswerListeingViewHolder.txtContentChatBoxAnsListen.setText(contentConversation);

                                    iClickItemButtonNextQuestionListener.iClickBtnNextQuestion(position);
                                }
                            }, 1000);

                        } else {
                            listBtnAnswer.get(pos).setTextColor(Color.parseColor("#EEEEEE"));
                            listBtnAnswer.get(pos).setEnabled(false);
                            MediaPlayer player1 = MediaPlayer.create(mContext,R.raw.wrong_answer_sound_effect);
                            player1.start();
                        }
                    }
                });
            }
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

    private static String convertStringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }

    private ArrayList<Button> CreateListButtonAnsForAnsTypeListen(ConversationQuesAndAnswerAdapter.ConversationTypeAnswerListeingViewHolder holder) {
        ArrayList<Button> listBtnAns = new ArrayList<>();
        listBtnAns.add(holder.btnAnswerA);
        listBtnAns.add(holder.btnAnswerB);
        listBtnAns.add(holder.btnAnswerC);
        return listBtnAns;
    }

    private void handleConversationAnswerTypeReadingLogic(RecyclerView.ViewHolder holder, ConversationModel conversationModel, int position) {
        ConversationTypeAnswerReadingViewHolder conversationTypeAnswerReadingViewHolder = (ConversationTypeAnswerReadingViewHolder) holder;

        iClickItemButtonTextToSpeechSlowWPLListener.onClickItemBtnSlowTextToSpeech(conversationModel.getContentConversationToSpeak());

        if(conversationModel.getConversationType()==2) {
            conversationTypeAnswerReadingViewHolder.imgCharacterAnsRead.setImageResource(R.drawable.ic__boys_2);
        }
        conversationTypeAnswerReadingViewHolder.txtContentChatBoxAnsRead.setText(conversationModel.getContentConversation());

        String contentCompare = conversationModel.getContentConversation().trim().replaceAll("[-+.^:,]","");

        conversationTypeAnswerReadingViewHolder.llBtnLoudSpAnsRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonRecordListener.onClickItemBtnRecord(contentCompare,position);
            }
        });

        conversationTypeAnswerReadingViewHolder.imgCharacterAnsRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonTextToSpeechSlowWPLListener.onClickItemBtnSlowTextToSpeech(conversationModel.getContentConversationToSpeak());
            }
        });
    }

    private void handleConversationQuestionLogic(RecyclerView.ViewHolder holder, ConversationModel conversationModel, int position) {
        ConversationTypeQuestionViewHolder conversationTypeQuestionViewHolder = (ConversationTypeQuestionViewHolder) holder;

        if(position==mList.size())
            return;
        iClickItemButtonTextToSpeechSlowWPLListener.onClickItemBtnSlowTextToSpeech(conversationModel.getContentConversationToSpeak());

        if(conversationModel.getConversationType()==1) {
            conversationTypeQuestionViewHolder.imgCharacter.setImageResource(R.drawable.ic_boy_comunicator);
        }
        conversationTypeQuestionViewHolder.txtContentChatBox.setText(conversationModel.getContentConversation());

        conversationTypeQuestionViewHolder.imgCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonTextToSpeechSlowWPLListener.onClickItemBtnSlowTextToSpeech(conversationModel.getContentConversationToSpeak());
            }
        });


    }

    @Override
    public int getItemCount() {
        if(mList!=null)
            return mList.size();
        else return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).getConversationType() == 2){
            return CONVERSATION_VIEW_TYPE_ANSWER_READING;
        }else if(mList.get(position).getConversationType() == 3){
            return CONVERSATION_VIEW_TYPE_ANSWER_LISTENING;
        }else if(mList.get(position).getConversationType() == 4){
            return CONVERSATION_VIEW_TYPE_ANSWER_DEFAULT;
        }else {
            return CONVERSATION_VIEW_TYPE_QUESTION;
        }
    }

    public class ConversationTypeQuestionViewHolder extends RecyclerView.ViewHolder{

        ImageView imgCharacter;
        TextView txtContentChatBox;

        public ConversationTypeQuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCharacter = itemView.findViewById(R.id.imgCharacter);
            txtContentChatBox = itemView.findViewById(R.id.txtContentConversationQues);
        }
    }

    public class ConversationTypeAnswerReadingViewHolder extends RecyclerView.ViewHolder{

        ImageView imgCharacterAnsRead;
        TextView txtContentChatBoxAnsRead;
        LinearLayout llBtnLoudSpAnsRead;

        public ConversationTypeAnswerReadingViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCharacterAnsRead = itemView.findViewById(R.id.imgCharacterQuesTypeReading);
            txtContentChatBoxAnsRead = itemView.findViewById(R.id.txtContentConversationQuesTypeReading);
            llBtnLoudSpAnsRead = itemView.findViewById(R.id.ibtnMicConverFormRead);
        }
    }

    public class ConversationTypeAnswerDefaultViewHolder extends RecyclerView.ViewHolder{

        ImageView imgCharacterAnsDefault;
        TextView txtContentChatBoxAnsDefault;

        public ConversationTypeAnswerDefaultViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCharacterAnsDefault = itemView.findViewById(R.id.imgCharacterQuesTypeDefault);
            txtContentChatBoxAnsDefault = itemView.findViewById(R.id.txtContentConversationQuesTypeDefault);
        }
    }

    public class ConversationTypeAnswerListeingViewHolder extends RecyclerView.ViewHolder{

        ImageView imgCharacterAnsListen;
        TextView txtContentChatBoxAnsListen;
        Button btnAnswerA, btnAnswerB, btnAnswerC;

        public ConversationTypeAnswerListeingViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCharacterAnsListen = itemView.findViewById(R.id.imgCharacterQuesTypeListening);
            txtContentChatBoxAnsListen = itemView.findViewById(R.id.txtContentConversationQuesTypeListening);
            btnAnswerA = itemView.findViewById(R.id.btnAnsAConver);
            btnAnswerB = itemView.findViewById(R.id.btnAnsBConver);
            btnAnswerC = itemView.findViewById(R.id.btnAnsCConver);
        }
    }
}
