package hr.tvz.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hr.tvz.quiz.adapter.AdminQuestionsAdapter;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminReportedQuestionsActivity extends AppCompatActivity {

    private APIClient client = APIClient.getInstance();

    private RecyclerView questionsRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private AdminQuestionsAdapter mAdapter;

    private ArrayList<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reported_questions);

        questionsRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_reported_question);

        Call<ArrayList<Question>> call = client.getApiService().getQuestionWhichHaveReports();
        call.enqueue(new Callback<ArrayList<Question>>() {
            @Override
            public void onResponse(Call<ArrayList<Question>> call, Response<ArrayList<Question>> response) {
                if (response.code() == 200) {
                    questions = response.body();
                    mAdapter = new AdminQuestionsAdapter(questions, AdminReportedQuestionsActivity.this);
                    mLayoutManager = new LinearLayoutManager(AdminReportedQuestionsActivity.this);
                    questionsRecyclerView.setLayoutManager(mLayoutManager);
                    questionsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    questionsRecyclerView.setAdapter(mAdapter);
                } else if (response.code() == 404) {
                    LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout);

                    TextView report = new TextView(AdminReportedQuestionsActivity.this);
                    report.setText("All reports have already been solved.");
                    report.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    layout.addView(report);
                } else {
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Question>> call, Throwable t) {
                Toast.makeText(AdminReportedQuestionsActivity.this, "An error happened. Please try again later.", Toast.LENGTH_LONG).show();
                System.out.println(t.toString());
            }
        });
    }
}
