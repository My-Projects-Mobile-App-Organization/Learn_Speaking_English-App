package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.fragmentUI.ConversationFragment;
import com.example.myapplication.fragmentUI.CourseFragment;
import com.example.myapplication.fragmentUI.EditProfileFragment;
import com.example.myapplication.fragmentUI.HomeFragment;
import com.example.myapplication.fragmentUI.MatchWordSoundFragment;
import com.example.myapplication.fragmentUI.ProfileFragment;
import com.example.myapplication.fragmentUI.ReadingWordFragment;
import com.example.myapplication.fragmentUI.WordPairingFragment;

public class MainActivity extends AppCompatActivity {

    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.layout_main,new WordPairingFragment(),"frag");
//        fragmentTransaction.commit();

        final LinearLayout homeLayout = findViewById(R.id.homeLayout);
        final LinearLayout courseLayout = findViewById(R.id.courseLayout);
        final LinearLayout conversationLayout = findViewById(R.id.conversationLayout);
        final LinearLayout profileLayout = findViewById(R.id.profileLayout);

        final ImageView homeImage = findViewById(R.id.homeImg);
        final ImageView courseImage = findViewById(R.id.courseImg);
        final ImageView conversationImage = findViewById(R.id.conversationImg);
        final ImageView profileImage = findViewById(R.id.profileImg);

        final TextView homeText = findViewById(R.id.homeTxt);
        final TextView courseText = findViewById(R.id.courseTxt);
        final TextView conversationText = findViewById(R.id.conversationTxt);
        final TextView profileText = findViewById(R.id.profileTxt);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, HomeFragment.class,null)
                .commit();

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedTab!=1){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, HomeFragment.class,null)
                            .commit();

                    courseText.setVisibility(View.GONE);
                    conversationText.setVisibility(View.GONE);
                    profileText.setVisibility(View.GONE);

                    courseImage.setImageResource(R.drawable.ic_pencil);
                    conversationImage.setImageResource(R.drawable.ic_bookop);
                    profileImage.setImageResource(R.drawable.ic_profile);

                    courseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    conversationLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    homeImage.setImageResource(R.drawable.ic_home_selected);
                    homeText.setVisibility(View.VISIBLE);
                    homeLayout.setBackgroundResource(R.drawable.round_back_home_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    homeLayout.startAnimation(scaleAnimation);

                    selectedTab = 1;
                }
            }
        });

        conversationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedTab!=3){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ConversationFragment.class,null)
                            .commit();

                    courseText.setVisibility(View.GONE);
                    homeText.setVisibility(View.GONE);
                    profileText.setVisibility(View.GONE);

                    courseImage.setImageResource(R.drawable.ic_pencil);
                    homeImage.setImageResource(R.drawable.ic_home);
                    profileImage.setImageResource(R.drawable.ic_profile);

                    courseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    conversationImage.setImageResource(R.drawable.ic_bookop_selected);
                    conversationText.setVisibility(View.VISIBLE);
                    conversationLayout.setBackgroundResource(R.drawable.round_back_conversation_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    conversationLayout.startAnimation(scaleAnimation);

                    selectedTab = 3;
                }
            }
        });

        courseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedTab!=2){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, CourseFragment.class,null)
                            .commit();

                    homeText.setVisibility(View.GONE);
                    conversationText.setVisibility(View.GONE);
                    profileText.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.ic_home);
                    conversationImage.setImageResource(R.drawable.ic_bookop);
                    profileImage.setImageResource(R.drawable.ic_profile);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    conversationLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profileLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    courseImage.setImageResource(R.drawable.ic_pencil_selected);
                    courseText.setVisibility(View.VISIBLE);
                    courseLayout.setBackgroundResource(R.drawable.round_back_course_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    courseLayout.startAnimation(scaleAnimation);

                    selectedTab = 2;
                }
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedTab!=4){

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragmentContainer, ProfileFragment.class,null)
                            .commit();

                    homeText.setVisibility(View.GONE);
                    conversationText.setVisibility(View.GONE);
                    courseText.setVisibility(View.GONE);

                    homeImage.setImageResource(R.drawable.ic_home);
                    conversationImage.setImageResource(R.drawable.ic_bookop);
                    courseImage.setImageResource(R.drawable.ic_pencil);

                    homeLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    conversationLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    courseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));

                    profileImage.setImageResource(R.drawable.ic_profile_selected);
                    profileText.setVisibility(View.VISIBLE);
                    profileLayout.setBackgroundResource(R.drawable.round_back_profile_100);

                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    profileLayout.startAnimation(scaleAnimation);

                    selectedTab = 4;
                }
            }
        });
    }

    public void goToEditProfileFragment(){
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, EditProfileFragment.class,null)
                .addToBackStack(EditProfileFragment.TAG)
                .commit();
    }
}