package hr.tvz.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.model.Report;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminReportedQuestionActivity extends AppCompatActivity {

    private APIClient client = APIClient.getInstance();

    private Question question;
    private EditText questionEditText, answerOneEditText, answerTwoEditText, answerThreeEditText, answerFourEditText;
    private CheckBox answerOneCheckBox, answerTwoCheckBox, answerThreeCheckBox, answerFourCheckbox;
    private Button solveReportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_reported_question);

        question = (Question) getIntent().getSerializableExtra("QUESTION");

        questionEditText = (EditText) findViewById(R.id.EditText_question_report);

        answerOneEditText = (EditText) findViewById(R.id.EditText_answer_1);
        answerTwoEditText = (EditText) findViewById(R.id.EditText_answer_2);
        answerThreeEditText = (EditText) findViewById(R.id.EditText_answer_3);
        answerFourEditText = (EditText) findViewById(R.id.EditText_answer_4);

        answerOneCheckBox = (CheckBox) findViewById(R.id.checkBox_answer_1);
        answerTwoCheckBox = (CheckBox) findViewById(R.id.checkBox_answer_2);
        answerThreeCheckBox = (CheckBox) findViewById(R.id.checkBox_answer_3);
        answerFourCheckbox = (CheckBox) findViewById(R.id.checkBox_answer_4);

        solveReportButton = (Button) findViewById(R.id.button_solve_report);

        questionEditText.setText(question.getQuestion());

        answerOneEditText.setText(question.getAnswers().get(0).getAnswer());
        answerTwoEditText.setText(question.getAnswers().get(1).getAnswer());
        answerThreeEditText.setText(question.getAnswers().get(2).getAnswer());
        answerFourEditText.setText(question.getAnswers().get(3).getAnswer());

        if (question.getAnswers().get(0).isCorrect()) {
            answerOneCheckBox.setChecked(true);
        }

        if (question.getAnswers().get(1).isCorrect()) {
            answerTwoCheckBox.setChecked(true);
        }

        if (question.getAnswers().get(2).isCorrect()) {
            answerThreeCheckBox.setChecked(true);
        }

        if (question.getAnswers().get(3).isCorrect()) {
            answerFourCheckbox.setChecked(true);
        }

        LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout1);

        for (Report item : question.getReports()) {
            if (item.getResolved() == 0) {
                TextView report = new TextView(this);
                report.setText(item.getComplaint());
                report.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                layout.addView(report);
            }
        }

        solveReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                solveReport();
            }
        });
    }

    private void solveReport() {
        question.setQuestion(questionEditText.getText().toString());
        question.getAnswers().get(0).setAnswer(answerOneEditText.getText().toString());
        question.getAnswers().get(0).setCorrect(answerOneCheckBox.isChecked());
        question.getAnswers().get(1).setAnswer(answerTwoEditText.getText().toString());
        question.getAnswers().get(1).setCorrect(answerTwoCheckBox.isChecked());
        question.getAnswers().get(2).setAnswer(answerThreeEditText.getText().toString());
        question.getAnswers().get(2).setCorrect(answerThreeCheckBox.isChecked());
        question.getAnswers().get(3).setAnswer(answerFourEditText.getText().toString());
        question.getAnswers().get(3).setCorrect(answerFourCheckbox.isChecked());

        for (int i = 0; i < question.getReports().size(); i++) {
            question.getReports().get(i).setResolved(1);
        }

        Call<Question> call = client.getApiService().updateQuestion(question.getId(), question);
        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.code() >= 200 && response.code() <= 400) {
                    Toast.makeText(AdminReportedQuestionActivity.this, "The report(s) have been solved and the question was updated.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AdminReportedQuestionActivity.this, AdminReportedQuestionsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                Toast.makeText(AdminReportedQuestionActivity.this, "An error happened. Please try again later.", Toast.LENGTH_LONG).show();
                System.out.println(t.toString());
            }
        });

    }
}
