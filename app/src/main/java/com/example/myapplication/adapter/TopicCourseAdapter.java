package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.LessonTestActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.TopicCourse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopicCourseAdapter extends RecyclerView.Adapter<TopicCourseAdapter.TopicCourseViewHolder> {

    private Context mContext;
    private List<TopicCourse> mListTopicCourse;

    public TopicCourseAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<TopicCourse> list){
        mListTopicCourse = list;
        //load dl vao adapter
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_course_item_layout,parent,false);

        return new TopicCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicCourseViewHolder holder, int position) {
        // set bin dl len cho adapter
        TopicCourse topicCourse = mListTopicCourse.get(position);
        if(topicCourse == null){
            return;
        }

        Picasso.with(mContext).load(topicCourse.getTopicImage()).into(holder.imgCourseTopic);
        holder.txtCourseTopic.setText(topicCourse.getTopicName());

        holder.btnItemCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LessonTestActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListTopicCourse != null)
        return mListTopicCourse.size();
        else return 0;
    }

    public class TopicCourseViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgCourseTopic;
        private TextView txtCourseTopic;
        private RelativeLayout btnItemCourse;

        public TopicCourseViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCourseTopic = itemView.findViewById(R.id.imgTopicCourse);
            txtCourseTopic = itemView.findViewById(R.id.txtTopicCourse);
            btnItemCourse = itemView.findViewById(R.id.layoutBtnItemCourse);
        }

    }
}
