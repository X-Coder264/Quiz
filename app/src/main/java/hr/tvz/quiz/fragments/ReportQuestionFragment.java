package hr.tvz.quiz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hr.tvz.quiz.GameEndActivity;
import hr.tvz.quiz.R;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.model.Report;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportQuestionFragment extends Fragment {

    private TextView tQuestion;
    private TextView tAnswer1;
    private TextView tAnswer2;
    private TextView tAnswer3;
    private TextView tAnswer4;

    private EditText eComplaint;

    private Button buttonReport;
    private APIClient client = APIClient.getInstance();


    private static final String QUESTION = "question";

    private Question question;

    private GameEndActivity c;

    public static ReportQuestionFragment newInstance(Question question) {

        final Bundle args = new Bundle();
        args.putSerializable(QUESTION, question);


        final ReportQuestionFragment fragment = new ReportQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_question_report, container, false);

        final Bundle args = getArguments();

        question = (Question) args.getSerializable(QUESTION);

        initializeViewElements(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void initializeViewElements(View view){

        tQuestion = (TextView) view.findViewById(R.id.textView_question_report);
        tAnswer1 = (TextView) view.findViewById(R.id.button_question_report_answer1);
        tAnswer2 = (TextView) view.findViewById(R.id.button_question_report_answer2);
        tAnswer3 = (TextView) view.findViewById(R.id.button_question_report_answer3);
        tAnswer4 = (TextView) view.findViewById(R.id.button_question_report_answer4);

        tQuestion.setText(question.getQuestion());

        tAnswer1.setText(question.getAnswers().get(0).getAnswer());
        tAnswer2.setText(question.getAnswers().get(1).getAnswer());
        tAnswer3.setText(question.getAnswers().get(2).getAnswer());
        tAnswer4.setText(question.getAnswers().get(3).getAnswer());

        eComplaint = (EditText) view.findViewById(R.id.editText_user_report_text);

        buttonReport = (Button) view.findViewById(R.id.button_send_report);

        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            sendReport();
            }
        });

    }

    public void sendReport(){
        Report report = new Report();
        report.setQuestionId(question.getId());
        report.setComplaint(eComplaint.getText().toString());

        Call<Report> call;
        try {
            call = client.getApiService().postReport(report);
            call.enqueue(new Callback<Report>() {
                @Override
                public void onResponse(Call<Report> call, Response<Report> response) {
                    int statusCode = response.code();
                    if (statusCode == 201) {
                        getActivity().onBackPressed();
                        Toast.makeText(c, "Thank you for your report!", Toast.LENGTH_LONG).show();
                    }else{
                        Log.w("Tomislav", "No questions found!");
                    }

                }

                @Override
                public void onFailure(Call<Report> call, Throwable t) {
                    Log.w("Tomislav", t.getMessage());
                }
            });

        } catch(Exception e){
            Log.w("Tomislav", e.getMessage());
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.c = (GameEndActivity) context;
    }

}
