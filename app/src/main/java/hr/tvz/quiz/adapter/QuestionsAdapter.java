package hr.tvz.quiz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hr.tvz.quiz.R;
import hr.tvz.quiz.model.Question;

/**
 * Created by Tomi PC on 8.6.2017..
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.MyViewHolder> {

        private List<Question> questionList;
        private Context c;

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


        public QuestionsAdapter(List<Question> questionList, Context c) {
            this.questionList = questionList;
            this.c = c;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_question_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Question question = questionList.get(position);

            holder.question.setText(question.getQuestion());

            holder.answer1.setText(question.getAnswers().get(0).getAnswer());
            holder.answer2.setText(question.getAnswers().get(1).getAnswer());
            holder.answer3.setText(question.getAnswers().get(2).getAnswer());
            holder.answer4.setText(question.getAnswers().get(3).getAnswer());

            holder.reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        @Override
        public int getItemCount() {
            return questionList.size();
        }

}
