package hr.tvz.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.tvz.quiz.model.Game;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.model.Statistic;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameEndActivity extends AppCompatActivity {

    private TextView tCorrect;

    private Button buttonPlayAgain;
    private Button buttonFinish;

    private List<Question> questions;
    private User user;
    private Subject subject;
    private ArrayList<Integer> answeerPosition;
    private int correctCounter, questionsCounter;
    private APIClient client = APIClient.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_end_result);

        Bundle extra = getIntent().getBundleExtra("QUESTIONS");
        this.questions = (ArrayList<Question>) extra.getSerializable("QUESTIONS");
        this.user = (User) getIntent().getSerializableExtra("USER");
        this.subject = (Subject) getIntent().getSerializableExtra("SUBJECT");
        this.answeerPosition = (ArrayList<Integer>) getIntent().getSerializableExtra("ANSWERS_POSITION");
        this.correctCounter = (Integer) getIntent().getSerializableExtra("CORRECT");
        this.questionsCounter = (Integer) getIntent().getSerializableExtra("QUESTION_NUMBER");

        updateDatabase(savedInstanceState);
        initializeViewElements();



    }

    private void initializeViewElements(){
        tCorrect = (TextView) findViewById(R.id.textView_correct_answer_single_player_end_result);

        buttonPlayAgain = (Button) findViewById(R.id.button_play_again_single_player);
        buttonFinish = (Button) findViewById(R.id.button_exit_single_player);

        tCorrect.setText(String.valueOf(correctCounter) + " / "+ questionsCounter);

        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateDatabase(final Bundle savedInstanceState){
        Game game = new Game(this.correctCounter, subject.getId(), user.getId());
        Statistic statistic;

        statistic = setStatistic();
        Call<Statistic> callStatistic;
        try {
            callStatistic = client.getApiService().postStatistic(statistic);
            callStatistic.enqueue(new Callback<Statistic>() {
                @Override
                public void onResponse(Call<Statistic> call, Response<Statistic> response) {
                    int statusCode = response.code();
                    if (statusCode != 201)
                        Log.w("Tomislav", "Statistic isn't changed");
                }

                @Override
                public void onFailure(Call<Statistic> call, Throwable t) {
                    Log.w("Tomislav", t.getMessage());
                }
            });

        } catch(Exception e){
            Log.w("Tomislav", e.getMessage());
        }


        Call<Game> call;
        try {
            call = client.getApiService().postGame(game);
            call.enqueue(new Callback<Game>() {
                @Override
                public void onResponse(Call<Game> call, Response<Game> response) {
                    int statusCode = response.code();
                    if (statusCode != 201)
                        Log.w("Tomislav", "Game isn't added");
                }

                @Override
                public void onFailure(Call<Game> call, Throwable t) {
                    Log.w("Tomislav", t.getMessage());
                }
            });

        } catch(Exception e){
            Log.w("Tomislav", e.getMessage());
        }
    }

    private Statistic setStatistic(){
        Statistic statistic = new Statistic();
        String questionsUser = "";

        for (Statistic temp : user.getStatistics()) {
            System.out.println(temp.getSubjectId() + " " + subject.getId());
            if (temp.getSubjectId().equals(subject.getId())) {
                statistic = temp;
                break;
            }
        }

        if (statistic.getUserId() ==null){
            statistic.setPoints(correctCounter);
            statistic.setUserId(user.getId());
            statistic.setSubjectId(subject.getId());

            for (Question temp: questions) {
                questionsUser += temp.getId() + ", ";
            }
            questionsUser = questionsUser.substring(0, questionsUser.length() - 2);
            statistic.setQuestionsUser(questionsUser);

            //ToDo: user na pocetnom ekranu se ne izmjeni...
            List<Statistic> temp = user.getStatistics();
            temp.add(statistic);
            user.setStatistics(temp);
        }
        else{
            statistic.setPoints(statistic.getPoints()+correctCounter);

            String[] myData = statistic.getQuestionsUser().split(", ");
            String newQuestions = "";

            for (Question temp : questions){
                Boolean check = true;
                for (String s: myData) {
                    if (s.equals(String.valueOf(temp.getId())))
                        check = false;
                }
                if (check)
                    newQuestions += temp.getId() + ", ";
            }
            newQuestions = newQuestions.substring(0, newQuestions.length() - 2);
            if (!(newQuestions.isEmpty()))
                statistic.setQuestionsUser(statistic.getQuestionsUser() + ", " + newQuestions);
        }

        return statistic;
    }
}