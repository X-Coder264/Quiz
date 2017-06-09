package hr.tvz.quiz;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hr.tvz.quiz.fragments.SinglePlayerQuestionFragment;
import hr.tvz.quiz.model.Exam;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SinglePlayerActivity extends AppCompatActivity implements SinglePlayerQuestionFragment.OnQuestionSelected{

    private User user;
    private Exam exam;
    private Subject subject;
    private int questionsNumber;
    private ArrayList<Question> questions;
    private int questionCounter = 0;
    private int correctAnswers;
    private ArrayList<Integer> answersPosition = new ArrayList<>();
    private int examId;

    private APIClient client = APIClient.getInstance();

    /**
     * It takes objects passed by PlayModeActivity and calls for initialization of screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        examId = (int) getIntent().getSerializableExtra("EXAM");
        this.subject = (Subject) getIntent().getSerializableExtra("SUBJECT");
        this.user = (User) getIntent().getSerializableExtra("USER");
        this.questionsNumber = (Integer) getIntent().getSerializableExtra("QUESTION_NUMBER");
        correctAnswers = 0;
        exam = subject.getExam().get(examId);

        initializeQuestions(savedInstanceState);
    }

    /**
     * Listens to the Question fragment, stores value from him, and starts fragments for new questions
     * @param answerPosition
     * @param correct
     */
    @Override
    public void OnQuestionSelected(int answerPosition, Boolean correct) {
        if (correct) correctAnswers++;
        answersPosition.add(answerPosition);

        questionCounter++;
        if (!( questionCounter < questionsNumber)) {
            calleEndActivity(correctAnswers, questionsNumber, questions, answersPosition);
        }
        else {
            final SinglePlayerQuestionFragment detailsFragment = SinglePlayerQuestionFragment.newInstance(questions.get(questionCounter), questionCounter+1, questionsNumber);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.activity_single_player_root, detailsFragment, "question1")
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * Fetches questions with retrofit from server.
     * Starts first question fragment.
     * @param savedInstanceState
     */
    private void initializeQuestions(final Bundle savedInstanceState) {
        Call<ArrayList<Question>> call;

        try {
            if (questionsNumber == 10)
                call = client.getApiService().get10RandomQuestionsForExam(exam.getId());
            else {
                call = client.getApiService().getAllQuestionsForExam(exam.getId());
            }

            call.enqueue(new Callback<ArrayList<Question>>() {
                @Override
                public void onResponse(Call<ArrayList<Question>> call, Response<ArrayList<Question>> response) {
                    int statusCode = response.code();
                    questions = response.body();

                    if (statusCode == 200) {
                        if (savedInstanceState == null) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.activity_single_player_root, SinglePlayerQuestionFragment.newInstance(questions.get(0), 1, questionsNumber), "question1")
                                    .commit();
                        }
                    }else{
                        Log.w("Tomislav", "No questions found!");
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Question>> call, Throwable t) {
                    Log.w("Tomislav", t.getMessage());
                }
            });

        } catch(Exception e){
            Log.w("Tomislav", e.getMessage());
        }
    }

    private void calleEndActivity(int correctCounter, int questionsCounter, ArrayList<Question> questions, ArrayList<Integer> answersPosition){
        Bundle bundle = new Bundle();
        bundle.putSerializable("QUESTIONS", questions);

        Intent intent = new Intent(SinglePlayerActivity.this, GameEndActivity.class);
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
}
