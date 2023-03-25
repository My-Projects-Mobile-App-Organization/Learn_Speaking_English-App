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

import com.example.myapplication.ConversationQuesAndAnswerActivity;
import com.example.myapplication.LessonTestActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.TopicConversation;
import com.example.myapplication.model.TopicCourse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopicConversationAdapter  extends RecyclerView.Adapter<TopicConversationAdapter.TopicConversationViewHolder> {

    private Context mContext;
    private List<TopicConversation> mListTopicConversation;

    public TopicConversationAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<TopicConversation> list){
        mListTopicConversation = list;
        //load dl vao adapter
        notifyDataSetChanged();
    }



    @Override
    public void onBindViewHolder(@NonNull TopicConversationAdapter.TopicConversationViewHolder holder, int position) {
        // set bin dl len cho adapter
        TopicConversation topicConversation = mListTopicConversation.get(position);
        if(topicConversation == null){
            return;
        }

        holder.txtConversationTopic.setText(topicConversation.getContentTopicConversation());
        holder.imgConversationTopic.setImageResource(topicConversation.getImgTopicConversation());

        holder.imgConversationTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ConversationQuesAndAnswerActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public TopicConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_conversation_item_layout,parent,false);

        return new TopicConversationViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(mListTopicConversation != null)
            return mListTopicConversation.size();
        else return 0;
    }

    public class TopicConversationViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgConversationTopic;
        private TextView txtConversationTopic;

        public TopicConversationViewHolder(@NonNull View itemView) {
            super(itemView);

            imgConversationTopic = itemView.findViewById(R.id.imgItemConversation);
            txtConversationTopic = itemView.findViewById(R.id.txtItemConversation);
        }

    }
}