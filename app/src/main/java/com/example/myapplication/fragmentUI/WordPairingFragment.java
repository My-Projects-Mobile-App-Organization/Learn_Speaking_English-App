package com.example.myapplication.fragmentUI;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.helper.widget.Flow;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.wefika.flowlayout.FlowLayout;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WordPairingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordPairingFragment extends Fragment {

    private int presCounter = 0;
    private int maxPresCounter = 4;
    private String[] keys = {"Vietnam", "and", "American", "difference", "not"};
    private String textAnswer = "BIRD";
    TextView textScreen, textQuestion, textTitle;
    EditText editText;
    Button btnReplay;
    Animation smallbigfont;

    private View mView;
    private LinearLayout gvWordList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WordPairingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WordPairingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordPairingFragment newInstance(String param1, String param2) {
        WordPairingFragment fragment = new WordPairingFragment();
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
        mView = inflater.inflate(R.layout.fragment_word_pairing, container, false);
       // gvWordList = mView.findViewById(R.id.wordPairingLayout);
        smallbigfont = AnimationUtils.loadAnimation(getContext(), R.anim.smallbigfont);
        AnhXa();

        keys = shuffleArray(keys);

        for (String key : keys){
            addView((FlowLayout) mView.findViewById(R.id.wordPairingLayout), key, editText);
        }

        //SetupDataAdapterWordPairing();

        maxPresCounter = 4;

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SetupDataAdapterWordPairing();
            }
        });


        return mView;
    }

    private void SetupDataAdapterWordPairing() {
        if(gvWordList == null){
            return;
        }
        presCounter = 0;
        FlowLayout layout = (FlowLayout) mView.findViewById(R.id.wordPairingLayout);
        layout.removeAllViews();
        keys = shuffleArray(keys);
        editText.setText("");

        if(gvWordList != null && keys.length > 0){
         for (String key : keys){
            addView((FlowLayout) mView.findViewById(R.id.wordPairingLayout), key, editText);
        }
        }

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

    private void addView(FlowLayout viewParent, final String text, final EditText editText) {
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                FlowLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,10,10,10);
        layoutParams.gravity = Gravity.CENTER;

        final TextView textView = new TextView(getContext());

        textView.setLayoutParams(layoutParams);
        textView.setBackground(this.getResources().getDrawable(R.drawable.background_shadow__board_button));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setPadding(30,30,30,30);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(22);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/FredokaOneRegular.ttf");

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
                if (presCounter < maxPresCounter) {
                    if (presCounter == 0)
                        editText.setText("");

                    editText.setText(editText.getText().toString() + text + " ");
                    textView.startAnimation(smallbigfont);
                    textView.animate().alpha(0).setDuration(300);
                    presCounter++;

                    if (presCounter == maxPresCounter) {
                          doValidate();
                    }
                }
            }
        });


        viewParent.addView(textView);
    }

    private void AnhXa(){
        textQuestion = (TextView) mView.findViewById(R.id.textQuestion);
        textScreen = (TextView) mView.findViewById(R.id.textScreen);
        textTitle = (TextView) mView.findViewById(R.id.textTitle);
        btnReplay = (Button) mView.findViewById(R.id.btnResetWordPairing);
        editText = (EditText) mView.findViewById(R.id.editTextAns);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/FredokaOneRegular.ttf");

        textQuestion.setTypeface(typeface);
        textScreen.setTypeface(typeface);
        textTitle.setTypeface(typeface);
        editText.setTypeface(typeface);

        btnReplay.setTypeface(typeface);
    }

    private void clickItemWordPairing(EditText editText, String text, TextView textView) {
        if (presCounter < maxPresCounter) {
            if (presCounter == 0)
                editText.setText("");

            editText.setText(editText.getText().toString() + text);
            textView.startAnimation(smallbigfont);
            textView.animate().alpha(0).setDuration(300);
            presCounter++;

            if (presCounter == maxPresCounter) {
                  doValidate();
            }
        }
    }

    private void doValidate(){
        presCounter = 0;

        if(editText.getText().toString().equals(textAnswer)){
            Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
            SetupDataAdapterWordPairing();
        }else {
            Toast.makeText(getContext(), "Wrong", Toast.LENGTH_SHORT).show();
            SetupDataAdapterWordPairing();
        }

    }
}