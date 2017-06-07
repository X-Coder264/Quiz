package hr.tvz.quiz.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import hr.tvz.quiz.R;
import hr.tvz.quiz.model.Question;

public class SinglePlayerQuestionFragment extends Fragment {

    private static final String QUESTION = "question";
    private static final String QUESTION_NUMBER = "question_number";
    private static final String MAX_QUESTION_NUMBER = "max_question_number";

    private TextView tTimer;
    private TextView tQuestion;
    private TextView tCurrentQuestionNumber;
    private Button buttonAnswer1;
    private Button buttonAnswer2;
    private Button buttonAnswer3;
    private Button buttonAnswer4;

    private OnQuestionSelected mListener;
    private Question question;
    private int questionNumber , maxQuestionNumber;

    private CountDownTimer timer;

    public static SinglePlayerQuestionFragment newInstance(Question question, int number, int number2) {

        final Bundle args = new Bundle();
        args.putSerializable(QUESTION, question);
        args.putInt(QUESTION_NUMBER, number);
        args.putInt(MAX_QUESTION_NUMBER, number2);
        final SinglePlayerQuestionFragment fragment = new SinglePlayerQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnQuestionSelected) {
            mListener = (OnQuestionSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnQuestionSelected.");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_single_player_question, container, false);

        final Bundle args = getArguments();

        question = (Question) args.getSerializable(QUESTION);
        questionNumber = args.getInt(QUESTION_NUMBER);
        maxQuestionNumber = args.getInt(MAX_QUESTION_NUMBER);

        timer = new CountDownTimer(20000, 1000) {
            public void onTick(long millisUntilFinished) {
                tTimer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                tTimer.setText("Out of time!");
                mListener.OnQuestionSelected("TimeRunOut!", false);

            }
        };


        initializeViewElements(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public interface OnQuestionSelected {
        void OnQuestionSelected(String question1, Boolean correct);
    }


    public void initializeViewElements(View view){
        tTimer = (TextView) view.findViewById(R.id.textView_single_player_question_timer);
        tQuestion = (TextView) view.findViewById(R.id.textView_single_player_question);
        tCurrentQuestionNumber = (TextView) view.findViewById(R.id.textView_current_question_single_player);

        tQuestion.setText(question.getQuestion());
        tCurrentQuestionNumber.setText(Integer.toString(questionNumber) + " / " + Integer.toString(maxQuestionNumber));

        buttonAnswer1 = (Button) view.findViewById(R.id.button_single_player_answer1);
        buttonAnswer2 = (Button) view.findViewById(R.id.button_single_player_answer2);
        buttonAnswer3 = (Button) view.findViewById(R.id.button_single_player_answer3);
        buttonAnswer4 = (Button) view.findViewById(R.id.button_single_player_answer4);

        buttonAnswer1.setText(question.getAnswers().get(0).getAnswer());
        buttonAnswer2.setText(question.getAnswers().get(1).getAnswer());
        buttonAnswer3.setText(question.getAnswers().get(2).getAnswer());
        buttonAnswer4.setText(question.getAnswers().get(3).getAnswer());

        buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                mListener.OnQuestionSelected("bla", question.getAnswers().get(0).isCorrect());
            }
        });

        buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                mListener.OnQuestionSelected("bla", question.getAnswers().get(0).isCorrect());
            }
        });

        buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                mListener.OnQuestionSelected("bla", question.getAnswers().get(0).isCorrect());
            }
        });

        buttonAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                mListener.OnQuestionSelected("bla", question.getAnswers().get(0).isCorrect());
            }
        });

        timer.start();

    }
}