package hr.tvz.quiz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import hr.tvz.quiz.model.Statistic;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import hr.tvz.quiz.rest.STOMPwebScoket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.client.StompClient;

import static android.R.attr.id;


public class PlayModeActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

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

    private STOMPwebScoket socket;

    private APIClient client = APIClient.getInstance();
    private ConcurrentHashMap<String, String> usersMap = new ConcurrentHashMap<>();

    private STOMPwebScoket stomp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mode);

        initializeGetAllUsers();

        subject = (Subject) getIntent().getSerializableExtra("SUBJECT");
        user = (User) getIntent().getSerializableExtra("USER");

        stomp = STOMPwebScoket.getInstance(this, user);

        calculateCoverage();
        initializeViewElements();
    }

    /*public void buttonClickSendInvite(View view) {
        int examId = spinnerColoquium.getSelectedItemPosition();
        examId++;
        String message = String.valueOf(examId) +"/" + "f f";
        stomp.listenGetQuestions("Luka Lukina");
        stomp.unsubscribeGetQuestions();
    }*/

    @Override
    protected void onDestroy() {
        //socket.disconnect();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        stomp = STOMPwebScoket.getInstance(this, user);
    }

    private void initializeGetAllUsers(){
        Call<ConcurrentHashMap<String, String>> call = client.getApiService().getActiveUsers();
        call.enqueue(new Callback<ConcurrentHashMap<String, String>>() {
            @Override
            public void onResponse(Call<ConcurrentHashMap<String, String>> call, Response<ConcurrentHashMap<String, String>> response) {
                int statusCode = response.code();
                if (statusCode == 200) {
                    usersMap = response.body();
                    for (String i : usersMap.keySet()) {
                        if (i.equals(user.getName())) {
                            usersMap.remove(i);
                            continue;
                        }
                    }
                } else {
                    System.out.println("Objects not found");
                }
            }

            @Override
            public void onFailure(Call<ConcurrentHashMap<String, String>> call, Throwable t) {
                Log.w("Tomislav", t.getCause());
            }
        });
    }

    public void buttonClickMultiPlayer(View v){
        Intent intent = new Intent(this, SearchMenuActivity.class);

        intent.putExtra("USER", user);
        intent.putExtra("SUBJECT", subject);
        intent.putExtra("OPPONENTS", usersMap);
        intent.putExtra("EXAM_ID", spinnerColoquium.getSelectedItemPosition()+1);

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.startActivity(intent);
    }

    public void sendNotification(){
        createPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_settings_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_home_black_24dp))
                //.setColor(getResources().getColor(R.color.primary))
                .setContentTitle("Hello")
                .setContentIntent(createPendingIntent())
                .setContentText(String.format("Tekst", "Naslov"))
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, builder.build());
    }

    public PendingIntent createPendingIntent(){
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack
        stackBuilder.addParentStack(MainActivity.class);

// Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
// Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        return resultPendingIntent;
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_comedy:
                Toast.makeText(this, "Comedy Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_movies:
                Toast.makeText(this, "Movies Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_music:
                Toast.makeText(this, "Music Clicked", Toast.LENGTH_SHORT).show();
                return true;
        }

        return false;
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
                intent.putExtra("EXAM", spinnerColoquium.getSelectedItemPosition());
                intent.putExtra("SUBJECT", subject);

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

        if (!(user.getStatistics() == null)) {
            for (Statistic statistic : user.getStatistics()) {
                if (statistic.getSubjectId() == subjectId) {
                    questionCounter = subject.getQuestionCounter();
                    userQuestions = statistic.getQuestionsUser();
                    break;
                }
            }
        }

        String[] data = userQuestions.split(", ");
        questionUserCounter = data.length;

        this.questionCounter = questionCounter;
        this.questionUserCounter = questionUserCounter;
    }


}
