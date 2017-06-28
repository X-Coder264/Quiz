package hr.tvz.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hr.tvz.quiz.model.Answer;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminVerifyQuestionActivity extends AppCompatActivity {

    private APIClient client = APIClient.getInstance();

    private Question question;
    private EditText questionEditText, answerOneEditText, answerTwoEditText, answerThreeEditText, answerFourEditText;
    private CheckBox answerOneCheckBox, answerTwoCheckBox, answerThreeCheckBox, answerFourCheckbox;
    private Button verifyQuestionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verify_question);

        question = (Question) getIntent().getSerializableExtra("QUESTION");

        questionEditText = (EditText) findViewById(R.id.EditText_question_verify);

        answerOneEditText = (EditText) findViewById(R.id.EditText_verify_answer_1);
        answerTwoEditText = (EditText) findViewById(R.id.EditText_verify_answer_2);
        answerThreeEditText = (EditText) findViewById(R.id.EditText_verify_answer_3);
        answerFourEditText = (EditText) findViewById(R.id.EditText_verify_answer_4);

        answerOneCheckBox = (CheckBox) findViewById(R.id.checkBox_verify_answer_1);
        answerTwoCheckBox = (CheckBox) findViewById(R.id.checkBox_verify_answer_2);
        answerThreeCheckBox = (CheckBox) findViewById(R.id.checkBox_verify_answer_3);
        answerFourCheckbox = (CheckBox) findViewById(R.id.checkBox_verify_answer_4);

        verifyQuestionButton = (Button) findViewById(R.id.button_verify_question);

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

        verifyQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyQuestion();
            }
        });
    }

    private void verifyQuestion() {
        String question_string = questionEditText.getText().toString();

        String answer1_string = answerOneEditText.getText().toString();
        boolean correct1 = answerOneCheckBox.isChecked();
        Answer answer1 = new Answer(answer1_string, correct1);
        String answer2_string = answerTwoEditText.getText().toString();
        boolean correct2 = answerTwoCheckBox.isChecked();
        Answer answer2 = new Answer(answer2_string, correct2);
        String answer3_string = answerThreeEditText.getText().toString();
        boolean correct3 = answerThreeCheckBox.isChecked();
        Answer answer3 = new Answer(answer3_string, correct3);
        String answer4_string = answerFourEditText.getText().toString();
        boolean correct4 = answerFourCheckbox.isChecked();
        Answer answer4 = new Answer(answer4_string, correct4);

        boolean allEditTextsContainText = false;

        if (!question_string.equals("") && !answer1_string.equals("") && !answer2_string.equals("") && !answer3_string.equals("") && !answer4_string.equals("")) {
            allEditTextsContainText = true;
        }

        boolean isThereAtLeastOneCorrectAnswer = false;

        if (correct1 || correct2 || correct3 || correct4) {
            isThereAtLeastOneCorrectAnswer = true;
        }

        if (allEditTextsContainText && isThereAtLeastOneCorrectAnswer) {
            List<Answer> answers = new ArrayList<Answer>();
            answers.add(answer1);
            answers.add(answer2);
            answers.add(answer3);
            answers.add(answer4);

            Question final_question = new Question(question_string, answers, true);

            Call<Question> call = client.getApiService().updateQuestion(question.getId(), final_question);
            call.enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    if (response.code() == 200) {
                        Toast.makeText(AdminVerifyQuestionActivity.this, "The question was successfully verified.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AdminVerifyQuestionActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AdminVerifyQuestionActivity.this, "An error happened. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Toast.makeText(AdminVerifyQuestionActivity.this, "An error happened. Please try again later.", Toast.LENGTH_LONG).show();
                    System.out.println(t.toString());
                }
            });
        } else {
            Toast.makeText(AdminVerifyQuestionActivity.this, "All fields are required and at least one answer has to be correct.", Toast.LENGTH_LONG).show();
        }
    }
}
