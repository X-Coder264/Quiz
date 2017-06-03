package hr.tvz.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.tvz.quiz.model.Exam;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.model.Statistic;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;


public class PlayModeActivity extends AppCompatActivity {

    private TextView tName;
    private TextView tCoverage;

    private Spinner spinnerColoquium;
    private Spinner spinnerQuestionNumber;

    private Button buttonSinglePlayer;

    private Subject subject;
    private User user;

    private ArrayList<Integer> questionsId = new ArrayList<>();
    private ArrayList<Integer> questionsUser = new ArrayList<>();

    private int questionCounter;
    private int questionUserCounter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mode);

        subject = (Subject) getIntent().getSerializableExtra("SUBJECT");
        user = (User) getIntent().getSerializableExtra("USER");
        calculateCoverage();
        initializeViewElements();


    }

    private void initializeViewElements(){
        List<String> spinnerArrayExam = new ArrayList<String>();

        tName = (TextView) findViewById(R.id.textView_mode_name);
        tCoverage = (TextView) findViewById(R.id.textView_mode_coverage);
        spinnerColoquium = (Spinner) findViewById(R.id.spinner_colloquium_exam__play_mode);
        spinnerQuestionNumber = (Spinner) findViewById(R.id.spinner_number_questions_play_mode);
        buttonSinglePlayer = (Button) findViewById(R.id.button_single_player);

        buttonSinglePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayModeActivity.this, SinglePlayerActivity.class);
                intent.putExtra("EXAM", subject.getExam().get(spinnerColoquium.getSelectedItemPosition()));

                int numberOfQuestions=0;
                if (spinnerQuestionNumber.getSelectedItem().toString().equals("All"))
                    numberOfQuestions = questionCounter;
                else
                    numberOfQuestions = 10;

                intent.putExtra("QUESTION_NUMBER", numberOfQuestions);
                intent.putExtra("USER", user);
                startActivity(intent);
                finish();
            }
        });

        tName.setText(subject.getName());
        tCoverage.setText(questionUserCounter + " / " + questionCounter);

        for (int i = 1; i<=subject.getExam().size();i++)
            spinnerArrayExam.add("Kolokvij "+i);

        setSpinner(0, Arrays.asList("10", "All"),spinnerQuestionNumber);
        setSpinner(0, spinnerArrayExam,spinnerColoquium);
    }

    public void setSpinner(int defaultSpinnerIndex, List<String> spinnerArray, Spinner spinner) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PlayModeActivity.this, R.layout.simple_spinner_item, spinnerArray);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(defaultSpinnerIndex);
    }

    private void calculateCoverage(){
        int questionCounter = 0, questionUserCounter = 0;
        int subjectId = subject.getId();
        int userId = user.getId();
        String userQuestions = "";

        for (Exam exam : subject.getExam()) {
            for (Question question : exam.getQuestions()) {
                questionsId.add(question.getId());
                questionCounter++;
            }
        }

        for (Statistic statistic : user.getStatistics()) {
            if (statistic.getUserId() == userId && statistic.getSubjectId() == subjectId) {
                System.out.println(statistic.getQuestionsUser());
                userQuestions = statistic.getQuestionsUser();
                break;
            }
        }

        String number = "";
        for (int i=0; i< userQuestions.length(); i++){
            if (userQuestions.charAt(i) == ','){
                questionsUser.add(Integer.parseInt(number));
                questionUserCounter++;
                number = "";
                i++;
            }
            else{
                number += userQuestions.charAt(i);
            }
        }

        questionUserCounter++;
        questionsUser.add(Integer.parseInt(number));

        this.questionCounter = questionCounter;
        this.questionUserCounter = questionUserCounter;
    }

    /*
    for(int i = 0; i < questionCounter; i++){
            for(int j = 0; j < questionUserCounter; j++)
                if (questionsId[i] == questionsUser[j]){

                }
        }
     */


}
