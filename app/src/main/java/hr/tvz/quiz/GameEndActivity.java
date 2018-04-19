package hr.tvz.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.tvz.quiz.adapter.QuestionsAdapter;
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
    private RecyclerView recycleReviewList;
    private QuestionsAdapter mAdapter;

    private User user;
    private Subject subject;
    private ArrayList<Integer> answerPosition;
    private int correctCounter, questionsCounter;
    private APIClient client = APIClient.getInstance();
    public int examId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player_end_result);

        Bundle extra = getIntent().getBundleExtra("QUESTIONS");
        this.questions = (ArrayList<Question>) extra.getSerializable("QUESTIONS");
        this.user = (User) getIntent().getSerializableExtra("USER");
        this.subject = (Subject) getIntent().getSerializableExtra("SUBJECT");
        this.answerPosition = (ArrayList<Integer>) getIntent().getSerializableExtra("ANSWERS_POSITION");
        this.correctCounter = (Integer) getIntent().getSerializableExtra("CORRECT");
        this.questionsCounter = (Integer) getIntent().getSerializableExtra("QUESTION_NUMBER");
        this.examId = (Integer) getIntent().getSerializableExtra("EXAM");

        updateDatabase(savedInstanceState);
        initializeViewElements();



    }

    private void initializeViewElements(){
        tCorrect = (TextView) findViewById(R.id.textView_correct_answer_single_player_end_result);

        buttonPlayAgain = (Button) findViewById(R.id.button_play_again_single_player);
        buttonFinish = (Button) findViewById(R.id.button_exit_single_player);

        tCorrect.setText(String.valueOf(correctCounter) + " / "+ questionsCounter);

        recycleReviewList = (RecyclerView) findViewById(R.id.recyclerView_question_list);

        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameEndActivity.this, SinglePlayerActivity.class);
                intent.putExtra("EXAM", examId);
                intent.putExtra("SUBJECT", subject);
                intent.putExtra("QUESTION_NUMBER", questions.size());  //ToDo: Insert new questions on play again not the same
                intent.putExtra("USER", user);
                startActivity(intent);
                finish();
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Recycler view
        mAdapter = new QuestionsAdapter(questions, answerPosition, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycleReviewList.setLayoutManager(mLayoutManager);
        recycleReviewList.setItemAnimator(new DefaultItemAnimator());
        recycleReviewList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recycleReviewList.setAdapter(mAdapter);

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

        if ( user.getStatistics() != null) {
            for (Statistic temp : user.getStatistics()) {
                if (temp.getSubjectId().equals(subject.getId())) {
                    statistic = temp;
                    break;
                }
            }
        }

        if (statistic.getUserId() == null){
            statistic.setPoints(correctCounter);
            statistic.setUserId(user.getId());
            statistic.setSubjectId(subject.getId());

            for (Question temp: questions) {
                questionsUser += temp.getId() + ", ";
            }
            questionsUser = questionsUser.substring(0, questionsUser.length() - 2);
            statistic.setQuestionsUser(questionsUser);

            //ToDo: user na pocetnom ekranu se ne izmjeni...
            List<Statistic> temp = new ArrayList<>();
            if ( user.getStatistics() != null)
                temp = user.getStatistics();
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

            if (!(newQuestions.isEmpty()))
                newQuestions = newQuestions.substring(0, newQuestions.length() - 2);

            if (!(newQuestions.isEmpty()))
                statistic.setQuestionsUser(statistic.getQuestionsUser() + ", " + newQuestions);
        }

        return statistic;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStackImmediate();
        else
            super.onBackPressed();
    }

    /*
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    */
}