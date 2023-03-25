package com.example.myapplication;

import com.example.myapplication.model.ConversationModel;
import com.example.myapplication.model.TopicConversation;
import com.example.myapplication.model.VocabularyWord;
import com.example.myapplication.model.AudioWordAnsMatchFrag;
import com.example.myapplication.model.ContentWordAnsMatchFrag;
import com.example.myapplication.model.LessonTest;
import com.example.myapplication.model.ProfileModel;
import com.example.myapplication.model.QuestionModel;
import com.example.myapplication.model.TopicCourse;
import com.example.myapplication.my_interface.MyCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class DbQuery {

    public static List<TopicCourse> g_topicList =new ArrayList<>();
    public static int g_selected_topic_index = 0;

    public static List<LessonTest> g_lessonList =new ArrayList<>();
    public static int g_selected_lesson_index = 0;

    public static List<QuestionModel> g_quesList =new ArrayList<>();
    public static List<QuestionModel> g_quesList_old =new ArrayList<>();

    public static List<TopicConversation> g_topicConversationList =new ArrayList<>();
    public static int g_selected_topic_conversation_index = 0;

    public static List<ConversationModel> g_ConversationList =new ArrayList<>();

    static int tmp;
    public static List<Integer> listIntFormListening = new ArrayList<>();

    public static ProfileModel profileModel =new ProfileModel("NA",null,null);

    public static void createUserData(){

    }

    public static void saveProfileData(){

    }

    public static void getUserData(){

    }

    public static void saveResult(){

    }

    public static void loadTopicCourse(){

    }

    public static void loadQuestion( List<LessonTest> list){
        g_quesList.clear();
        listIntFormListening.clear();

        list = g_lessonList;
        int numberOfListeningQuestionType = 1;
        int numberOfMatchingQuestionType = 1;
        int numberOfReadingQuestionType = 1;
        int numberOfWordPairingQuestionType = 1;
        int numberOfWordPairingListenQuestionType = 1;

        for (int j = 0; j < numberOfReadingQuestionType; j++){
            // Type Reading
            String vocabReadingType = list.get(g_selected_lesson_index).getListVocab().get(j).getTitleVocab();
            String audioVocabReadingType = list.get(g_selected_lesson_index).getListVocab().get(j).getAudioVocab();
            ArrayList<ContentWordAnsMatchFrag> list3a = new ArrayList<>();
            list3a.add(new ContentWordAnsMatchFrag(vocabReadingType,j));
            ArrayList<AudioWordAnsMatchFrag> list3b = new ArrayList<>();
            list3b.add(new AudioWordAnsMatchFrag(audioVocabReadingType,j));
            ArrayList<Integer> list3c = new ArrayList<>();

            g_quesList.add(new QuestionModel(3,"Ques 3",list3a,list3b,list3c));
        }

        for (int i = 0; i <  numberOfListeningQuestionType; i++){
            String audioVocabListenType = list.get(g_selected_lesson_index).getListVocab().get(i).getAudioVocab();
            String vocab1ListenType = list.get(g_selected_lesson_index).getListVocab().get(i).getTitleVocab();

            List<Integer> mListInt = myRandom(i);
            String vocab2ListenType = list.get(g_selected_lesson_index).getListVocab().get(mListInt.get(0)).getTitleVocab();
            String vocab3ListenType = list.get(g_selected_lesson_index).getListVocab().get(mListInt.get(1)).getTitleVocab();
            String vocab4ListenType = list.get(g_selected_lesson_index).getListVocab().get(mListInt.get(2)).getTitleVocab();


            // Type Listening
            ArrayList<ContentWordAnsMatchFrag> list1a = new ArrayList<>();
            list1a.add(new ContentWordAnsMatchFrag(vocab1ListenType,i));
            ArrayList<AudioWordAnsMatchFrag> list1b = new ArrayList<>();
            list1b.add(new AudioWordAnsMatchFrag(vocab1ListenType,i));
            list1b.add(new AudioWordAnsMatchFrag(vocab2ListenType,mListInt.get(0)));
            list1b.add(new AudioWordAnsMatchFrag(vocab3ListenType,mListInt.get(1)));
            list1b.add(new AudioWordAnsMatchFrag(vocab4ListenType,mListInt.get(2)));
            ArrayList<Integer> list1c = new ArrayList<>();
            list1c.add(i);
            g_quesList.add(new QuestionModel(1,"Ques 1",list1a,list1b,list1c));

        }
//

//
        for (int k = 0; k < numberOfWordPairingQuestionType; k++){
            // Type WordPairing
            ArrayList<ContentWordAnsMatchFrag> list4a = new ArrayList<>();
            list4a.add(new ContentWordAnsMatchFrag(list.get(g_selected_lesson_index).getListVocab().get(k).getTranslateExampleVocab(),k));
            ArrayList<AudioWordAnsMatchFrag> list4b = new ArrayList<>();

            list4b.add(new AudioWordAnsMatchFrag(list.get(g_selected_lesson_index).getListVocab().get(k).getExampleVocab(),k));

            ArrayList<Integer> list4c = new ArrayList<>();

            g_quesList.add(new QuestionModel(4,"Ques 4",list4a,list4b,list4c));

        }

        for (int l = 0; l < numberOfWordPairingListenQuestionType; l++){
            String vocabListenType = list.get(g_selected_lesson_index).getListVocab().get(l).getExampleVocab();
            // Type WordPairing Listening
            ArrayList<ContentWordAnsMatchFrag> list5a = new ArrayList<>();
            list5a.add(new ContentWordAnsMatchFrag(vocabListenType,l));
            ArrayList<AudioWordAnsMatchFrag> list5b = new ArrayList<>();

            ArrayList<Integer> list5c = new ArrayList<>();

            g_quesList.add(new QuestionModel(5,"Ques 5",list5a,list5b,list5c));
        }

        // Type Matching
        ArrayList<ContentWordAnsMatchFrag> list2a = new ArrayList<>();
        list2a.add(new ContentWordAnsMatchFrag(list.get(g_selected_lesson_index).getListVocab().get(0).getTitleVocab(),1));
        list2a.add(new ContentWordAnsMatchFrag(list.get(g_selected_lesson_index).getListVocab().get(1).getTitleVocab(),2));
        list2a.add(new ContentWordAnsMatchFrag(list.get(g_selected_lesson_index).getListVocab().get(2).getTitleVocab(),3));
        list2a.add(new ContentWordAnsMatchFrag(list.get(g_selected_lesson_index).getListVocab().get(3).getTitleVocab(),4));
        ArrayList<AudioWordAnsMatchFrag> list2b = new ArrayList<>();
        list2b.add(new AudioWordAnsMatchFrag(list.get(g_selected_lesson_index).getListVocab().get(0).getTitleVocab(),1));
        list2b.add(new AudioWordAnsMatchFrag(list.get(g_selected_lesson_index).getListVocab().get(1).getTitleVocab(),2));
        list2b.add(new AudioWordAnsMatchFrag(list.get(g_selected_lesson_index).getListVocab().get(2).getTitleVocab(),3));
        list2b.add(new AudioWordAnsMatchFrag(list.get(g_selected_lesson_index).getListVocab().get(3).getTitleVocab(),4));
        ArrayList<Integer> list2c = new ArrayList<>();

        g_quesList.add(new QuestionModel(2,"Ques 2",list2a,list2b,list2c));


    }

    public static void loadLessonTest(final MyCompleteListener myCompleteListener){
        g_lessonList.clear();

        List<VocabularyWord> list = new ArrayList<>();
        list.add(new VocabularyWord(1,"abide by","https://600tuvungtoeic.com/template/english/images/study/abide_by.jpg","/ə'baid/",
                "(v): tôn trọng, tuân theo, giữ (lời)","The two parties agreed to abide by the judge's decision","Hai bên đã đồng ý tuân theo quyết định của tòa án.","https://600tuvungtoeic.com/audio/abide_by.mp3",false));
        list.add(new VocabularyWord(2,"agreement","https://600tuvungtoeic.com/template/english/images/study/agreement.jpg","/ə'baid/",
                "(v): tôn trọng, tuân theo, giữ (lời)","The sales associate gave his assurance that the missing keyboard would be replaced the next day.","Đối tác bán hàng cam đoan rằng ngày mai bàn phím lỗi sẽ được thay.","",false));
        list.add(new VocabularyWord(3,"cancellation","https://600tuvungtoeic.com/template/english/images/study/assurance.jpg","/ə'baid/",
                "(v): tôn trọng, tuân theo, giữ (lời)","The cancelation of her flight caused her problems for the rest of the week","Việc hủy chuyến bay đã gây cho cô ấy nhiều vấn đề trong những ngày còn lại của tuần.","",false));
        list.add(new VocabularyWord(4,"determine","https://600tuvungtoeic.com/template/english/images/study/cancellation.jpg","/ə'baid/",
                "(v): tôn trọng, tuân theo, giữ (lời)","After reading the contract, I was still unable to detemine if our company was liable for back wages.","Sau khi đọc hợp đồng, tôi vẫn không thể biết được liệu rằng công ty của chúng tôi có phải chịu trách nhiệm hoàn trả lại lương không","",false));
        list.add(new VocabularyWord(5,"engage","https://600tuvungtoeic.com/template/english/images/study/determine.jpg","/ə'baid/",
                "(v): tôn trọng, tuân theo, giữ (lời)","He engaged us in a fascinating discussion about current business law","Anh ấy cùng chúng tôi thảo luận sôi nổi về luật kinh doanh hiện hành","",false));

        g_lessonList.add(new LessonTest("1","Family","Family",5,list));
    }

    public static void loadVocabulary(){

    }

    public static void loadData(){

    }

    public static void loadConversation(){

    }

    public static void loadTopicConversation(){

    }

    public static List<Integer> myRandom(int n){
//        List<Integer> list = new ArrayList<>();
//
//        Random random = new Random();
//        for (int i=0;i<3;i++) {
//            int number;
//            do {
//                number = random.nextInt(4);
//            } while (number == n);
//            list.add(number);
//        }

        List<Integer> list = new ArrayList<>();
        for (int i=0;i<4;i++) {
            if(i==n)
                continue;
            list.add(i);
        }
        //0 2 3
        return list;
    }
}
