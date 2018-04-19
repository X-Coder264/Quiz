package hr.tvz.quiz.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.tvz.quiz.GameEndActivity;
import hr.tvz.quiz.R;
import hr.tvz.quiz.fragments.ReportQuestionFragment;
import hr.tvz.quiz.model.Question;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.MyViewHolder> {

    private List<Question> questionList;
    private GameEndActivity c;
    private FragmentManager f_manager;
    private ArrayList<Integer> answerPosition;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView question, answer1, answer2, answer3, answer4;
        public Button reportButton;

        public MyViewHolder(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.textView_question);
            answer1 = (TextView) view.findViewById(R.id.textView_answer_1);
            answer2 = (TextView) view.findViewById(R.id.textView_answer_2);
            answer3 = (TextView) view.findViewById(R.id.textView_answer_3);
            answer4 = (TextView) view.findViewById(R.id.textView_answer_4);

            reportButton = (Button) view.findViewById(R.id.button_report_question);
        }
    }


    public QuestionsAdapter(List<Question> questionList, ArrayList<Integer> answerPosition, Context c) {
        this.questionList = questionList;
        this.answerPosition = answerPosition;
        this.c = (GameEndActivity)c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_question_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Question question = questionList.get(position);
        holder.answer1.setBackgroundColor(ContextCompat.getColor(c, R.color.white));
        holder.answer2.setBackgroundColor(ContextCompat.getColor(c, R.color.white));
        holder.answer3.setBackgroundColor(ContextCompat.getColor(c, R.color.white));
        holder.answer4.setBackgroundColor(ContextCompat.getColor(c, R.color.white));

        ArrayList<Integer> a = answerPosition;
        if (answerPosition.get(position) == 1 && !question.getAnswers().get(0).isCorrect()) {
            holder.answer1.setBackgroundColor(ContextCompat.getColor(c, R.color.red));
        }
        if (answerPosition.get(position) == 2 && !question.getAnswers().get(1).isCorrect()) {
            holder.answer1.setBackgroundColor(ContextCompat.getColor(c, R.color.red));
        }
        if (answerPosition.get(position) == 3 && !question.getAnswers().get(2).isCorrect()) {
            holder.answer1.setBackgroundColor(ContextCompat.getColor(c, R.color.red));
        }
        if (answerPosition.get(position) == 4 && !question.getAnswers().get(3).isCorrect()) {
            holder.answer1.setBackgroundColor(ContextCompat.getColor(c, R.color.red));
        }

        if (question.getAnswers().get(0).isCorrect()) {
            holder.answer1.setBackgroundColor(ContextCompat.getColor(c, R.color.green));
        }
        else if(question.getAnswers().get(1).isCorrect()) {
            holder.answer2.setBackgroundColor(ContextCompat.getColor(c, R.color.green));
        }
        else if(question.getAnswers().get(2).isCorrect()) {
            holder.answer3.setBackgroundColor(ContextCompat.getColor(c, R.color.green));
        }
        else {
            holder.answer4.setBackgroundColor(ContextCompat.getColor(c, R.color.green));
        }

        holder.question.setText(question.getQuestion());
        holder.answer1.setText(question.getAnswers().get(0).getAnswer());
        holder.answer2.setText(question.getAnswers().get(1).getAnswer());
        holder.answer3.setText(question.getAnswers().get(2).getAnswer());
        holder.answer4.setText(question.getAnswers().get(3).getAnswer());

        holder.reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    /*c.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.activity_single_player_end_result, detailsFragment, "question1")
                            .addToBackStack(null)
                            .commit();*/

                c.getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("reportFragment")
                        .add(R.id.activity_single_player_end_result_root, ReportQuestionFragment.newInstance(question), "question")
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

}
