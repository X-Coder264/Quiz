package hr.tvz.quiz.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import hr.tvz.quiz.R;
import hr.tvz.quiz.adapter.StatisticsRecyclerViewAdapter;
import hr.tvz.quiz.model.Statistic;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableScoreFragment extends Fragment {

    private APIClient client = APIClient.getInstance();

    private ArrayList<Statistic> statistics;
    private Subject subject;
    private RecyclerView tableScoreRecyclerView;
    private StatisticsRecyclerViewAdapter mAdapter;

    public static TableScoreFragment newInstance(Subject subject) {
        TableScoreFragment fragment = new TableScoreFragment();
        Bundle args = new Bundle();

        args.putSerializable("SUBJECT", subject);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_table_tab, container, false);
        final Bundle args = getArguments();
        subject = (Subject) args.getSerializable("SUBJECT");

        tableScoreRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_score_table);

        Call<ArrayList<Statistic>> call = client.getApiService().getSubjectScoreTable(subject.getId());
        call.enqueue(new Callback<ArrayList<Statistic>>() {
            @Override
            public void onResponse(Call<ArrayList<Statistic>> call, Response<ArrayList<Statistic>> response) {
                if (response.code() == 200) {
                    statistics = response.body();
                    mAdapter = new StatisticsRecyclerViewAdapter(statistics, getActivity());
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    tableScoreRecyclerView.setLayoutManager(mLayoutManager);
                    tableScoreRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    tableScoreRecyclerView.setAdapter(mAdapter);
                } else {
                    System.out.println("error");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Statistic>> call, Throwable t) {
                Toast.makeText(getActivity(), "An error happened. Please try again later.", Toast.LENGTH_LONG).show();
                System.out.println(t.toString());
            }
        });

        return view;
    }
}
