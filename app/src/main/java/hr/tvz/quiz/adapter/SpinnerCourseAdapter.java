package hr.tvz.quiz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hr.tvz.quiz.model.Course;

public class SpinnerCourseAdapter extends ArrayAdapter<Course> {

    private Context context;
    private List <Course> courses;

    public SpinnerCourseAdapter(Context context, int textViewResourceId,
                                List<Course> courses) {
        super(context, textViewResourceId, courses);
        this.context = context;
        this.courses = courses;
    }

    public int getCount(){
        return courses.size();
    }

    public Course getItem(int position){
        return courses.get(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but it's possible to reference a custom layout for each spinner item
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(courses.get(position).getName());

        // And finally return a dynamic (or custom) view for each spinner item
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(courses.get(position).getName());

        return label;
    }
}
