package hr.tvz.quiz.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hr.tvz.quiz.AdminReportedQuestionActivity;
import hr.tvz.quiz.AdminReportedQuestionsActivity;
import hr.tvz.quiz.R;
import hr.tvz.quiz.model.Question;

public class AdminQuestionsAdapter extends RecyclerView.Adapter<AdminQuestionsAdapter.MyViewHolder> {

        private List<Question> questionList;
        private AdminReportedQuestionsActivity c;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView question, test;
            public Button reportButton;

            public MyViewHolder(View view) {
                super(view);
                question = (TextView) view.findViewById(R.id.textView_reported_question);
                //test = (TextView) view.findViewById(R.id.textView_admin_user_name);

                reportButton = (Button) view.findViewById(R.id.button_see_report_question);
            }
        }


        public AdminQuestionsAdapter(List<Question> questionList, Context c) {
            this.questionList = questionList;
            this.c = (AdminReportedQuestionsActivity) c;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_reported_question, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Question question = questionList.get(position);

            holder.question.setText(question.getQuestion());
            //holder.test.setText("test");

            holder.reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AdminReportedQuestionActivity.class);
                    intent.putExtra("QUESTION", question);
                    c.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return questionList.size();
        }

}
