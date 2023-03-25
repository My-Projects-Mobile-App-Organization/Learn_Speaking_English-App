package com.example.myapplication.fragmentUI;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.AudioWordAnsMatchFrag;
import com.example.myapplication.model.ContentWordAnsMatchFrag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MatchWordSoundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchWordSoundFragment extends Fragment {

    ImageView imgIconLoud1, imgIconLoud2, imgIconLoud3, imgIconLoud4;
    ImageView imgIconWave1, imgIconWave2, imgIconWave3, imgIconWave4;

    LinearLayout layoutAdudioChoice1,layoutAdudioChoice2,layoutAdudioChoice3,layoutAdudioChoice4;
    TextView txtWordChoice1,txtWordChoice2,txtWordChoice3,txtWordChoice4;
    // audio
    ArrayList<LinearLayout> listBtnAudioChoice;
    //word
    ArrayList<TextView> listContentWordChoice;

    //word
    ArrayList<ContentWordAnsMatchFrag> listContentWord;
    //audio
    ArrayList<AudioWordAnsMatchFrag> listAudioWord;

//    Drawable[] listDrawbleCorrectBG = {getResources().getDrawable(R.drawable.background_shadow_click_board) ,getResources().getDrawable(R.drawable.background_shadow_correct_board),getResources().getDrawable(R.drawable.background_shadow_board)};
//    Drawable[] listDrawbleFaillBG = {getResources().getDrawable(R.drawable.background_shadow_click_board) ,getResources().getDrawable(R.drawable.background_shadow_error_board),getResources().getDrawable(R.drawable.background_shadow_board)};

    static Integer currentPos;
    static int curPosSelected;

    private Button btnNext;
    private View mView;
//    TestActivity mTestActivity;
//    StartUpDataFragment startUpDataFragment;
    MediaPlayer mediaPlayer;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MatchWordSoundFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchWordSoundFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchWordSoundFragment newInstance(String param1, String param2) {
        MatchWordSoundFragment fragment = new MatchWordSoundFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_match_word_sound, container, false);

        AnhXa();
        CreateListContentAndAudio();

        // tạo ánh xạ frag ban gốc

//        startUpDataFragment = (StartUpDataFragment) getActivity().getSupportFragmentManager().findFragmentByTag("fragmentInitData");
//        mTestActivity = (TestActivity) getActivity();

        // get data của list
//        Bundle bundleReceive = getArguments();
//        if(bundleReceive != null){
//            Question questionNumber = (Question) bundleReceive.getSerializable("obj ques");
//            if(questionNumber != null){
//                listContentQues = questionNumber.getListContentQues();
//                listChoiceQues = questionNumber.getListChoiceQues();
//            }
//            bundleReceive.remove("obj ques");
//        }
        CloneData();
        // trộn mảng
        Collections.shuffle(listContentWord);
        Collections.shuffle(listAudioWord);

        for (int i=0;i<listContentWordChoice.size();i++) {
            // gán nội dung từ vựng cho textview
            listContentWordChoice.get(i).setText(listContentWord.get(i).getContent());
            int pos = i;
            // bắt sự kiện
            listContentWordChoice.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //nếu chọn lần đầu thì lấy cur pos nếu ko sẽ check match đáp án

                    if(currentPos != null)
                    {
                        if (listContentWord.get(pos).getIdContent() == listAudioWord.get(currentPos).getIdAudio()) {
                            listContentWordChoice.get(pos).setTextColor(Color.parseColor("#A1100F0F"));

                            SetStatusBGTxt("correct",listContentWordChoice.get(pos));
                            SetStatusBGBtn("correct",listBtnAudioChoice.get(currentPos));

                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
                                    SetStatusBGTxt("default",listContentWordChoice.get(pos));
                                    SetStatusBGBtn("default",listBtnAudioChoice.get(currentPos));

                                    listBtnAudioChoice.get(currentPos).setEnabled(false);
                                    listContentWordChoice.get(pos).setEnabled(false);

                                    SetEnableBtn(true,curPosSelected);
                                    SetStatusIcon("off",currentPos);
                                    currentPos = null;

                                    Toast.makeText(mView.getContext(), "đúng rồi", Toast.LENGTH_SHORT).show();
                                    if (mediaPlayer.isPlaying())
                                        mediaPlayer.stop();
                                }
                            }, 500);


                        } else {
                            SetStatusBGTxt("wrong",listContentWordChoice.get(pos));
                            SetStatusBGBtn("wrong",listBtnAudioChoice.get(currentPos));

                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Do something after 100ms
                                    SetStatusBGTxt("default",listContentWordChoice.get(pos));
                                    SetStatusBGBtn("default",listBtnAudioChoice.get(currentPos));

                                    SetEnableBtn(true,curPosSelected);
                                    currentPos = null;

                                    Toast.makeText(mView.getContext(), "sai rồi", Toast.LENGTH_SHORT).show();
                                    if (mediaPlayer.isPlaying())
                                        mediaPlayer.stop();
                                }
                            }, 500);
                        }
                    }
                }
                });
            }

        // xử lý logic form matching
        for (int i=0;i<listBtnAudioChoice.size();i++) {
            //listBtnAudioChoice.get(i).setBackgroundResource(R.drawable.ic_loud_speaker);
            int pos = i;
            listBtnAudioChoice.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mediaPlayer.setDataSource(listAudioWord.get(pos).getAudio());
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    if(currentPos == null){
                        currentPos = pos;
                        SetStatusBGBtn("click", listBtnAudioChoice.get(currentPos));
                        SetEnableBtn(false, currentPos);
                    }

                }
            });
        }

        // check end of list ques
//        if(startUpDataFragment.indexOfList == startUpDataFragment.mListQuestion.size()){
//            btnNext.setText("End");
//            btnNext.setEnabled(false);
//        }

//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Integer typeQues =  Integer.valueOf(startUpDataFragment.;
//                //if(typeQues != null ){
//                startUpDataFragment.CheckFormQuestion(startUpDataFragment.mListQuestion.get(startUpDataFragment.indexOfList).getTypeQues());
//                // }
//                //Log.d("AAA",startUpDataFragment.mListQuestion.get(startUpDataFragment.indexOfList).getTypeQues()+"");
//            }
//        });


        return mView;
    }

    private void CloneData(){
        listContentWord = new ArrayList<>();
        listContentWord.add(new ContentWordAnsMatchFrag("a bybide",1));
        listContentWord.add(new ContentWordAnsMatchFrag("agreement",2));
        listContentWord.add(new ContentWordAnsMatchFrag("canelaytion",3));
        listContentWord.add(new ContentWordAnsMatchFrag("determine",4));

        listAudioWord = new ArrayList<>();
        listAudioWord.add(new AudioWordAnsMatchFrag("https://600tuvungtoeic.com/audio/abide_by.mp3",1));
        listAudioWord.add(new AudioWordAnsMatchFrag("https://600tuvungtoeic.com/audio/agreement.mp3",2));
        listAudioWord.add(new AudioWordAnsMatchFrag("https://600tuvungtoeic.com/audio/cancellation.mp3",3));
        listAudioWord.add(new AudioWordAnsMatchFrag("https://600tuvungtoeic.com/audio/determine.mp3",4));

    }

    private void SetStatusIcon(String status, int posIcon){
        if(status.equals("off")){
            switch (posIcon){
                case 0:
                    imgIconLoud1.setImageResource(R.drawable.loudsp_off);
                    imgIconWave1.setImageResource(R.drawable.soundwave_off);
                    return;
                case 1:
                    imgIconLoud2.setImageResource(R.drawable.loudsp_off);
                    imgIconWave2.setImageResource(R.drawable.soundwave_off);
                    return;
                case 2:
                    imgIconLoud3.setImageResource(R.drawable.loudsp_off);
                    imgIconWave3.setImageResource(R.drawable.soundwave_off);
                    return;
                case 3:
                    imgIconLoud4.setImageResource(R.drawable.loudsp_off);
                    imgIconWave4.setImageResource(R.drawable.soundwave_off);
                    return;
                default:
                    return;
            }
        }else if(status.equals("on")){
            switch (posIcon){
                case 0:
                    imgIconLoud1.setImageResource(R.drawable.loudsp);
                    imgIconWave1.setImageResource(R.drawable.soundwave);
                    return;
                case 1:
                    imgIconLoud2.setImageResource(R.drawable.loudsp);
                    imgIconWave2.setImageResource(R.drawable.soundwave);
                    return;
                case 2:
                    imgIconLoud3.setImageResource(R.drawable.loudsp);
                    imgIconWave3.setImageResource(R.drawable.soundwave);
                    return;
                case 3:
                    imgIconLoud4.setImageResource(R.drawable.loudsp);
                    imgIconWave4.setImageResource(R.drawable.soundwave);
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

    private void SetEnableBtn(boolean check, int posTxtCur){
        for (int i=0;i<listBtnAudioChoice.size();i++){
            if(i==posTxtCur){
                continue;
            }
            listBtnAudioChoice.get(i).setEnabled(check);
        }
    }

    private void SetEnableTRUEBtn(int posTxtCur){
        for (int i=0;i<listBtnAudioChoice.size();i++){
            if(i==posTxtCur){
                continue;
            }
            listBtnAudioChoice.get(i).setEnabled(true);
        }
    }

    private void CreateListContentAndAudio() {
        listBtnAudioChoice = new ArrayList<>();
        listBtnAudioChoice.add(layoutAdudioChoice1);
        listBtnAudioChoice.add(layoutAdudioChoice2);
        listBtnAudioChoice.add(layoutAdudioChoice3);
        listBtnAudioChoice.add(layoutAdudioChoice4);

        listContentWordChoice = new ArrayList<>();
        listContentWordChoice.add(txtWordChoice1);
        listContentWordChoice.add(txtWordChoice2);
        listContentWordChoice.add(txtWordChoice3);
        listContentWordChoice.add(txtWordChoice4);

    }

    private void AnhXa() {
        //btnNext = mView.findViewById(R.id.btnForm1Next);
        layoutAdudioChoice1 = mView.findViewById(R.id.btn1ChoiceMW);
        layoutAdudioChoice2 = mView.findViewById(R.id.btn2ChoiceMW);
        layoutAdudioChoice3 = mView.findViewById(R.id.btn3ChoiceMW);
        layoutAdudioChoice4 = mView.findViewById(R.id.btn4ChoiceMW);
        txtWordChoice1 = mView.findViewById(R.id.txt1ChoiceMW);
        txtWordChoice2 = mView.findViewById(R.id.txt2ChoiceMW);
        txtWordChoice3 = mView.findViewById(R.id.txt3ChoiceMW);
        txtWordChoice4 = mView.findViewById(R.id.txt4ChoiceMW);

        imgIconLoud1 = mView.findViewById(R.id.loudsp1MW);
        imgIconLoud2 = mView.findViewById(R.id.loudsp2MW);
        imgIconLoud3 = mView.findViewById(R.id.loudsp3MW);
        imgIconLoud4 = mView.findViewById(R.id.loudsp4MW);
        imgIconWave1 = mView.findViewById(R.id.wave1MW);
        imgIconWave2 = mView.findViewById(R.id.wave2MW);
        imgIconWave3 = mView.findViewById(R.id.wave3MW);
        imgIconWave4 = mView.findViewById(R.id.wave4MW);
    }
}

//                    if (currentPos == null){
//                        currentPos = pos;
//                        SetStatusBGTxt("click", listContentWordChoice.get(currentPos));
//                        SetEnableTxt(false, currentPos);
//                    }


//                    else {
//                        if(listAudioWord.get(currentPos).getIdAudio() == listContentWord.get(pos).getIdContent()){
//                            listContentWordChoice.get(currentPos).setTextColor(Color.parseColor("#A1100F0F"));
//
////                            listBtnAudioChoice.get(currentPos).setBackgroundResource(R.drawable.ic_loud_speaker_off);
//                            listContentWordChoice.get(pos).setEnabled(false);
//                            listBtnAudioChoice.get(currentPos).setEnabled(false);
//
//                            SetEnableBtn(true,currentPos);
//                            currentPos = null;
//                            Toast.makeText(mView.getContext(),"đúng rồi",Toast.LENGTH_SHORT).show();
//                            if (mediaPlayer.isPlaying())
//                                mediaPlayer.stop();
//                        }else {
//                            SetEnableBtn(true,currentPos);
//                            Toast.makeText(mView.getContext(),"sai rồi",Toast.LENGTH_LONG).show();
//                            if (mediaPlayer.isPlaying())
//                                mediaPlayer.stop();
//                            currentPos = null;
//                        }
//                    }