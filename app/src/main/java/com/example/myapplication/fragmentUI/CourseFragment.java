package com.example.myapplication.fragmentUI;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TopicCourseAdapter;
import com.example.myapplication.model.TopicCourse;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    private RecyclerView rcvTopicCourse;
    private TopicCourseAdapter topicCourseAdapter;

    private View mView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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
        mView = inflater.inflate(R.layout.fragment_course, container, false);

        rcvTopicCourse = mView.findViewById(R.id.rcv_list_course_topic);
        topicCourseAdapter = new TopicCourseAdapter(mView.getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mView.getContext(), RecyclerView.VERTICAL, false);
        rcvTopicCourse.setLayoutManager(linearLayoutManager);

        topicCourseAdapter.setData(getListTopic());
        rcvTopicCourse.setAdapter(topicCourseAdapter);

        return mView;
    }

    private List<TopicCourse> getListTopic() {
        List<TopicCourse> list = new ArrayList<>();
        list.add(new TopicCourse("1","Contracts","https://600tuvungtoeic.com/template/english/images/lesson/contracts.jpg","https://600tuvungtoeic.com/template/english/images/lesson/contracts.jpg"));
        list.add(new TopicCourse("2","Marketing","https://600tuvungtoeic.com/template/english/images/lesson/marketing.jpg","https://600tuvungtoeic.com/template/english/images/lesson/contracts.jpg"));
        list.add(new TopicCourse("3","Warranties","https://600tuvungtoeic.com/template/english/images/lesson/warranties.jpg","https://600tuvungtoeic.com/template/english/images/lesson/contracts.jpg"));

        return list;
    }
}