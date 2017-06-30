package hr.tvz.quiz;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import hr.tvz.quiz.fragments.MultiPlayerQuestionFragment;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.model.QuestionsSTOMP;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import hr.tvz.quiz.rest.STOMPwebScoket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.client.StompClient;

public class MultiPlayerActivity extends AppCompatActivity implements STOMPwebScoket.OnGameStart, MultiPlayerQuestionFragment.OnQuestionSelected{

    private User user;
    private UserLocalStore userLocalStore;
    private String owner;
    private int examId;


    private StompClient mStompClient;
    private QuestionsSTOMP questionsSTOMP;

    private STOMPwebScoket stomp;
    private APIClient client = APIClient.getInstance();

    //Single
    private Subject subject;
    private String subjectId;
    private int questionsNumber;
    private int questionCounter = 0;
    private int correctAnswers;
    private ArrayList<Integer> answersPosition = new ArrayList<>();
    private ArrayList<Question> questions;

    private Bundle saved;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        saved = savedInstanceState;

        userLocalStore = new UserLocalStore(this);
        user = userLocalStore.getLoggedInUser();
        stomp = STOMPwebScoket.getInstance(this, user);

        owner = (String) getIntent().getSerializableExtra("OPPONENT");
        examId = (int) getIntent().getSerializableExtra("EXAM_ID");

        subject = (Subject) getIntent().getSerializableExtra("SUBJECT");
        subjectId = (String) getIntent().getSerializableExtra("SUBJECT_ID");

        if (subjectId != null){
            subjectId = subjectId.replace("\n", "");
            initializeSubject();
        }


        questionsNumber = 10;


        if (owner == null)
            owner = user.getName();

        stomp.listenGetQuestions(owner);

        stomp.sendGetQuestions(owner, String.valueOf(examId));

    }

    @Override
    public void OnGameStart(QuestionsSTOMP q) {
        questions = (ArrayList<Question>) q.getQuestions();
        if (saved == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_multi_player_root, MultiPlayerQuestionFragment.newInstance(questions.get(0), 1, questionsNumber), "question1")
                    .commit();
        }
      System.out.println("Jeeeeeeeeej uslo je");
    }

    @Override
    protected void onResume() {
        super.onResume();
        stomp = STOMPwebScoket.getInstance(this, user);
    }

    @Override
    public void OnQuestionSelected(int answerPosition, Boolean correct) {
        if (correct) correctAnswers++;
        answersPosition.add(answerPosition);


        questionCounter++;
        if (!( questionCounter < questionsNumber)) {
            //flag = false;
            calleEndActivity(correctAnswers, questionsNumber, questions, answersPosition);
        }
        else {
            final MultiPlayerQuestionFragment detailsFragment = MultiPlayerQuestionFragment.newInstance(questions.get(questionCounter), questionCounter+1, questionsNumber);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_multi_player_root, detailsFragment, "question1")
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void calleEndActivity(int correctCounter, int questionsCounter, ArrayList<Question> questions, ArrayList<Integer> answersPosition){
        stomp.unsubscribeGetQuestions();

        Bundle bundle = new Bundle();
        bundle.putSerializable("QUESTIONS", questions);

        Intent intent = new Intent(MultiPlayerActivity.this, GameEndActivity.class);
        intent.putExtra("QUESTION_NUMBER", questionsCounter);
        intent.putExtra("USER", user);
        intent.putExtra("EXAM", examId);
        intent.putExtra("SUBJECT", subject);
        intent.putExtra("CORRECT", correctCounter);
        intent.putExtra("QUESTIONS", bundle);
        intent.putExtra("ANSWERS_POSITION", answersPosition);
        startActivity(intent);

        finish();
    }

    private void initializeSubject(){
        Call<Subject> call = client.getApiService().getSubject(Integer.valueOf(subjectId));
        call.enqueue(new Callback<Subject>() {
            @Override
            public void onResponse(Call<Subject> call, Response<Subject> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    subject = response.body();
                } else {
                    System.out.println("Object not found");
                }
            }

            @Override
            public void onFailure(Call<Subject> call, Throwable t) {
                Log.w("Tomislav", t.getCause());
            }
        });
    }
}
