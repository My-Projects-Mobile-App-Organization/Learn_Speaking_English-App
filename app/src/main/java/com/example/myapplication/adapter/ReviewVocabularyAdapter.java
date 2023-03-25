package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.VocabularyWord;
import com.example.myapplication.my_interface.IClickItemButtonTextToSpeechFastWPLListener;
import com.squareup.picasso.Picasso;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.List;

public class ReviewVocabularyAdapter extends RecyclerView.Adapter<ReviewVocabularyAdapter.ReviewVocabViewHolder>{

    Context mContext;
    List<VocabularyWord> mList;
    IClickItemButtonTextToSpeechFastWPLListener iClickItemButtonTextToSpeechFastWPLListener;

    public ReviewVocabularyAdapter(Context context, List<VocabularyWord> mList,
                                   IClickItemButtonTextToSpeechFastWPLListener listener) {
        this.mContext = context;
        this.mList = mList;
        this.iClickItemButtonTextToSpeechFastWPLListener = listener;
    }

    public void setData(List<VocabularyWord> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewVocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_vocab_layout,parent,false);

        return new ReviewVocabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewVocabViewHolder holder, @SuppressLint("RecyclerView") int position) {
        VocabularyWord vocabularyWord = mList.get(position);
        if(vocabularyWord==null)
            return;


        holder.txtTitleVocabFront.setText(vocabularyWord.getTitleVocab());
        holder.txtTitleVocabBack.setText(vocabularyWord.getTitleVocab());
        holder.txtTypeVocabBack.setText(vocabularyWord.getTypeVocab());
        holder.txtPronouVocabFront.setText(vocabularyWord.getPronounceVocab());
        holder.txtExampleVocab.setText("Example: " + "\n" + vocabularyWord.getExampleVocab() + "\n" + vocabularyWord.getTranslateExampleVocab());

        holder.btnLoudSpFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonTextToSpeechFastWPLListener.onClickItemBtnFastTextToSpeech(vocabularyWord.getTitleVocab());
            }
        });
        holder.btnLoudSpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemButtonTextToSpeechFastWPLListener.onClickItemBtnFastTextToSpeech(vocabularyWord.getTitleVocab());
            }
        });
//        holder.imgVocabFront.setImageResource(vocabularyWord.getLinkImgVocab());
        Picasso.with(mContext).load(vocabularyWord.getLinkImgVocab()).into(holder.imgVocabFront);
        if (holder.flipView.getCurrentFlipState() == EasyFlipView.FlipState.FRONT_SIDE && mList.get(
                position).isFlipped()) {
            holder.flipView.setFlipDuration(0);
            holder.flipView.flipTheView();
        } else if (holder.flipView.getCurrentFlipState() == EasyFlipView.FlipState.BACK_SIDE
                && !mList.get(position).isFlipped()) {
            holder.flipView.setFlipDuration(0);
            holder.flipView.flipTheView();
        }
        holder.flipView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(position).isFlipped()) {
                    mList.get(position).setFlipped(false);
                } else {
                    mList.get(position).setFlipped(true);
                }
                holder.flipView.setFlipDuration(700);
                holder.flipView.flipTheView();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mList!=null)return mList.size();
        return 0;
    }

    public class ReviewVocabViewHolder extends RecyclerView.ViewHolder{

        EasyFlipView flipView;
        LinearLayout btnLoudSpFront,btnLoudSpBack;
        TextView txtTitleVocabFront,txtPronouVocabFront,txtTitleVocabBack,txtTypeVocabBack,txtExampleVocab;
        ImageView imgVocabFront;

        public ReviewVocabViewHolder(@NonNull View itemView) {
            super(itemView);

            flipView = itemView.findViewById(R.id.flipview);
            btnLoudSpBack = itemView.findViewById(R.id.btnLoudReviewVocabBack);
            btnLoudSpFront = itemView.findViewById(R.id.btnLoudReviewVocabFront);
            imgVocabFront = itemView.findViewById(R.id.imgVocabReviewFront);
            txtTitleVocabFront = itemView.findViewById(R.id.txTitleVocabReviewFront);
            txtPronouVocabFront = itemView.findViewById(R.id.txtPhienam);
            txtTitleVocabBack = itemView.findViewById(R.id.txTitleVocabReviewBack);
            txtTypeVocabBack = itemView.findViewById(R.id.txtTypeVocabReview);
            txtExampleVocab = itemView.findViewById(R.id.txtExampleVocabReview);
        }
    }
}
