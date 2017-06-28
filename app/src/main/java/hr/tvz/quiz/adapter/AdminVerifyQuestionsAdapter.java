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

import hr.tvz.quiz.AdminVerifyQuestionActivity;
import hr.tvz.quiz.AdminVerifyQuestionsActivity;
import hr.tvz.quiz.R;
import hr.tvz.quiz.model.Question;

public class AdminVerifyQuestionsAdapter extends RecyclerView.Adapter<AdminVerifyQuestionsAdapter.MyViewHolder> {

        private List<Question> questionList;
        private AdminVerifyQuestionsActivity c;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView question, test;
            public Button reportButton;

            public MyViewHolder(View view) {
                super(view);
                question = (TextView) view.findViewById(R.id.textView_verify_question);
                //test = (TextView) view.findViewById(R.id.textView_verify_admin_user_name);

                reportButton = (Button) view.findViewById(R.id.button_see_question);
            }
        }


        public AdminVerifyQuestionsAdapter(List<Question> questionList, Context c) {
            this.questionList = questionList;
            this.c = (AdminVerifyQuestionsActivity) c;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_questions_that_need_to_be_verified, parent, false);

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
                    Intent intent = new Intent(v.getContext(), AdminVerifyQuestionActivity.class);
                    System.out.println(question);
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
