package hr.tvz.quiz;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hr.tvz.quiz.fragments.SinglePlayerQuestionFragment;
import hr.tvz.quiz.model.Exam;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;

public class SinglePlayerActivity extends AppCompatActivity implements SinglePlayerQuestionFragment.OnQuestionSelected{

    private User user;
    private Exam exam;
    private int questionsNumber;
    private List<Question> questions;
    private int questionCounter = 0;
    private int correctAnswers;
    private ArrayList<Integer> questionsId = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        exam = (Exam) getIntent().getSerializableExtra("EXAM");
        user = (User) getIntent().getSerializableExtra("USER");
        questionsNumber = (Integer) getIntent().getSerializableExtra("QUESTION_NUMBER");
        correctAnswers = 0;

        if (questionsNumber == 10)
            questions = pickNRandomQuestions(exam.getQuestions(),10);
        else{
            questions = exam.getQuestions();
            questionsNumber = 0;
            for (Question question : questions) {
                questionsNumber++;
            }
        }


        questionsId.add(questions.get(0).getId());
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_single_player_root, SinglePlayerQuestionFragment.newInstance(questions.get(0), 1, questionsNumber), "question1")
                    .commit();
        }
    }

    public static List<Question> pickNRandomQuestions(List<Question> questions, int n) {
        List<Question> copy = new LinkedList<Question>(questions);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }

    @Override
    public void OnQuestionSelected(String message, Boolean correct) {
        if (correct) correctAnswers++;
        questionsId.add(questions.get(questionCounter).getId());
        questionCounter++;
        if (!( questionCounter < questionsNumber)) {
            finish();
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
}
