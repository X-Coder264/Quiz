package hr.tvz.quiz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hr.tvz.quiz.R;
import hr.tvz.quiz.StatisticsActivity;
import hr.tvz.quiz.model.Statistic;

public class StatisticsRecyclerViewAdapter extends RecyclerView.Adapter<StatisticsRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Statistic> statistics;
    private StatisticsActivity c;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView numberTextView, usernameTextView, userScoreTextView;

        public MyViewHolder(View view) {
            super(view);
            numberTextView = (TextView) view.findViewById(R.id.textView_student_ordinal_number);
            usernameTextView = (TextView) view.findViewById(R.id.textView_student_name);
            userScoreTextView = (TextView) view.findViewById(R.id.textView_student_score);
        }
    }


    public StatisticsRecyclerViewAdapter(ArrayList<Statistic> statistics, Context c) {
        this.statistics = statistics;
        this.c = (StatisticsActivity) c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_score_table, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Statistic statistic = statistics.get(position);

        int place = position + 1;
        holder.numberTextView.setText(place + ".");
        holder.usernameTextView.setText(statistic.getUsername());
        holder.userScoreTextView.setText(Integer.toString(statistic.getPoints()));

    }

    @Override
    public int getItemCount() {
        return statistics.size();
    }

}
