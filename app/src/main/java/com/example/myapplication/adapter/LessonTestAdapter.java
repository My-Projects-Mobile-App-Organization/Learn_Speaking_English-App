package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.StandbyScreenActivity;
import com.example.myapplication.TestActivity;
import com.example.myapplication.model.LessonTest;
import com.example.myapplication.model.TopicCourse;

import java.util.List;

public class LessonTestAdapter extends RecyclerView.Adapter<LessonTestAdapter.LessonViewHolder> {

    private Context mContext;
    private List<LessonTest> list_Lesson;

    public LessonTestAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<LessonTest> list){
        this.list_Lesson = list;
        //load dl vao adapter
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LessonTestAdapter.LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
         = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_test_item_layout,parent,false);

        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonTestAdapter.LessonViewHolder holder, int position) {
        LessonTest lessonTest = list_Lesson.get(position);

        if(lessonTest==null)
            return;
        holder.txtTopic.setText(lessonTest.getTopicLesson());
        holder.txtTitle.setText(lessonTest.getTitleLesson());
        holder.txtNumberVocab.setText(lessonTest.getNumberVocab() + " vocabulary");

        holder.layoutTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StandbyScreenActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list_Lesson != null)
            return list_Lesson.size();
        else return 0;
    }

    public class LessonViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layoutTestBtn;
        private TextView txtTitle, txtTopic, txtNumberVocab;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitleLessonItem);
            txtTopic = (TextView) itemView.findViewById(R.id.txtTopicLessonItem);
            txtNumberVocab = (TextView) itemView.findViewById(R.id.txtNumberVocab);
            layoutTestBtn = itemView.findViewById(R.id.btnItemLesson);

        }

    }
}
