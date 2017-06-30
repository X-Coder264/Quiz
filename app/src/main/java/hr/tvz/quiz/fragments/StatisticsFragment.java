package hr.tvz.quiz.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import hr.tvz.quiz.R;
import hr.tvz.quiz.StatisticsActivity;
import hr.tvz.quiz.UserLocalStore;
import hr.tvz.quiz.model.Statistic;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StatisticsFragment extends Fragment {

    private User user;
    private UserLocalStore userLocalStore;
    private Subject subject;
    private Statistic statistic;
    private PieChart pieChart;
    private TextView subjectTextView;
    private APIClient client = APIClient.getInstance();
    private int total_number_of_user_covered_questions = 0;

    private StatisticsActivity c;

    public static StatisticsFragment newInstance(Subject subject) {

        final Bundle args = new Bundle();
        args.putSerializable("SUBJECT", subject);


        final StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics_tab, container, false);
        //TextView textView = new TextView(getActivity());
        final Bundle args = getArguments();

        subject = (Subject) args.getSerializable("SUBJECT");

        userLocalStore = new UserLocalStore(getContext());

        user = userLocalStore.getLoggedInUser();

        pieChart = (PieChart) view.findViewById(R.id.pie_chart_for_coverage);

        subjectTextView = (TextView) view.findViewById(R.id.textView11111);
        subjectTextView.setText(subject.getName());

        Call<Statistic> call = client.getApiService().getUserSubjectStatistics(user.getId(), subject.getId());
        call.enqueue(new Callback<Statistic>() {
            @Override
            public void onResponse(Call<Statistic> call, Response<Statistic> response) {
                if (response.code() == 200) {
                    statistic = response.body();
                    System.out.println(statistic);
                    String[] split = statistic.getQuestionsUser().split(",");
                    System.out.println("str br elemenata " + split.length);
                    total_number_of_user_covered_questions = split.length;
                    setUpTheChart();
                }  else {
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<Statistic> call, Throwable t) {
                Toast.makeText(getActivity(), "An error happened. Please try again later2.", Toast.LENGTH_LONG).show();
                System.out.println(t.toString());
            }
        });

        return view;
    }

    private void setUpTheChart() {
        // configure pie chart
        pieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("Questions coverage pie chart");
        pieChart.setDescription(description);

        // enable hole and configure
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(7);
        pieChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);

        List<PieEntry> entries = new ArrayList<>();



        entries.add(new PieEntry(total_number_of_user_covered_questions, "Covered"));
        entries.add(new PieEntry(subject.getQuestionCounter() - total_number_of_user_covered_questions, "Non-covered"));

        PieDataSet set = new PieDataSet(entries, "Questions coverage");
        set.setSliceSpace(3);
        set.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        /*for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);*/

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        set.setColors(colors);
        PieData data = new PieData(set);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.RED);
        pieChart.setData(data);
        pieChart.highlightValues(null);
        pieChart.invalidate(); // refresh
    }
}
