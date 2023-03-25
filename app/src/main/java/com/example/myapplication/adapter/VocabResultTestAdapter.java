package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.VocabularyActivity;
import com.example.myapplication.model.VocabularyWord;

import java.util.List;

public class VocabResultTestAdapter extends RecyclerView.Adapter<VocabResultTestAdapter.VocabResultViewHolder> {

    private Context mContext;
    private List<VocabularyWord> list_vocab_result;

    public VocabResultTestAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<VocabularyWord> list){
        this.list_vocab_result = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VocabResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocab_result_layout,parent,false);
        return new VocabResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabResultViewHolder holder, int position) {
        VocabularyWord vocabularyWord = list_vocab_result.get(position);

        if(vocabularyWord== null)
            return;
        if(vocabularyWord.isStatusVocab())
            holder.imgStatusVocab.setImageResource(R.drawable.ic_correct);
        else holder.imgStatusVocab.setImageResource(R.drawable.ic_incorrect);

        holder.imgViewVocab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VocabularyActivity.class);
                intent.putExtra("vocabItemChoose",vocabularyWord);

                mContext.startActivity(intent);
            }
        });

        holder.txtVocabResult.setText(vocabularyWord.getTitleVocab());
        //holder.txtTypeVocabResult.setText(vocabularyWord.getTypeVocab());

    }

    @Override
    public int getItemCount() {
        if(list_vocab_result!= null)
            return list_vocab_result.size();
        else return 0;
    }

    public class VocabResultViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgStatusVocab, imgViewVocab;
        private TextView txtVocabResult, txtTypeVocabResult;

        public VocabResultViewHolder(@NonNull View itemView) {
            super(itemView);

            imgStatusVocab = itemView.findViewById(R.id.imgStatusVocab);
            imgViewVocab = itemView.findViewById(R.id.imgViewVocab);
            txtVocabResult = itemView.findViewById(R.id.txtVocabRes);
            txtTypeVocabResult = itemView.findViewById(R.id.txtTypeVocabResult);
        }
    }
}
