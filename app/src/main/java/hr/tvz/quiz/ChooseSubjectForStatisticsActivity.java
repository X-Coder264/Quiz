package hr.tvz.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.tvz.quiz.model.Course;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseSubjectForStatisticsActivity extends AppCompatActivity {

    private UserLocalStore userLocalStore;
    private User user;
    private APIClient client = APIClient.getInstance();

    private List<Course> courses;
    private List<Subject> subjects;
    private Course course = new Course();
    private Subject subject = new Subject();
    List<String> spinnerArrayCourse = new ArrayList<String>();
    List<String> spinnerArraySubject = new ArrayList<String>();

    private Spinner spinnerSemester, spinnerCourse, spinnerSubject;
    private Button seeStatisticsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_subject_for_statistics);

        userLocalStore = new UserLocalStore(this);

        user = userLocalStore.getLoggedInUser();

        spinnerCourse = (Spinner) findViewById(R.id.spinner_statistics_course);
        spinnerSemester = (Spinner) findViewById(R.id.spinner_statistics_semester);
        spinnerSubject = (Spinner) findViewById(R.id.spinner_statistics_subject);

        seeStatisticsButton = (Button) findViewById(R.id.button_view_statistics);

        setSpinner(user.getSemester() - 1, Arrays.asList("1", "2", "3", "4", "5", "6"), spinnerSemester);

        initializeCourses();

        seeStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseSubjectForStatisticsActivity.this, StatisticsActivity.class);
                System.out.println(subject);
                intent.putExtra("SUBJECT", subject);
                startActivity(intent);
            }
        });
    }

    private void initializeCourses() {
        Call<List<Course>> call = client.getApiService().getCourses();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                int statusCode = response.code();
                courses = response.body();
                if (statusCode == 200) {
                    for (int i = 0; i < courses.size(); i++) {
                        spinnerArrayCourse.add(courses.get(i).getName());
                        course = courses.get(0);
                    }

                    subjects = course.getSubjects();
                    subject = subjects.get(0);
                    for (int i = 0; i < subjects.size(); i++) {
                        if (subjects.get(i).getSemester() == 1) {
                            spinnerArraySubject.add(subjects.get(i).getName());
                        }
                    }

                    setSpinner(0, spinnerArraySubject, spinnerSubject);
                    setSpinner(0, spinnerArrayCourse, spinnerCourse);

                    spinnerLogic();

                } else {
                    System.out.println("Objects not found");
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.w("Tomislav", t.getCause());
            }
        });
    }

    public void setSpinner(int defaultSpinnerIndex, List<String> spinnerArray, Spinner spinner) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ChooseSubjectForStatisticsActivity.this, R.layout.simple_spinner_item, spinnerArray);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(defaultSpinnerIndex);
    }

    public void spinnerLogic(){
        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                for (int i = 0; i < courses.size(); i++){
                    if (courses.get(i).getName() == spinnerCourse.getSelectedItem()){
                        course = courses.get(i);
                        break;
                    }
                }

                subjects = course.getSubjects();
                spinnerArraySubject.clear();
                for (int i=0; i<subjects.size();i++) {
                    if (subjects.get(i).getSemester() == Integer.parseInt(spinnerSemester.getSelectedItem().toString())) {
                        spinnerArraySubject.add(subjects.get(i).getName());
                    }
                }
                subject = subjects.get(0);
                setSubjectOnSpinnerChange();
                setSpinner(0, spinnerArraySubject, spinnerSubject);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerSemester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                List<String> spinnerArraySubject = new ArrayList<String>();

                subjects = course.getSubjects();
                for (int i = 0; i < subjects.size(); i++){
                    if (subjects.get(i).getSemester() == Integer.parseInt(spinnerSemester.getSelectedItem().toString()))
                        spinnerArraySubject.add(subjects.get(i).getName());
                }
                setSubjectOnSpinnerChange();
                setSpinner(0, spinnerArraySubject, spinnerSubject);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setSubjectOnSpinnerChange();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setSubjectOnSpinnerChange(){
        for (int i = 0; i < course.getSubjects().size(); i++) {
            if (course.getSubjects().get(i).getName().equals(spinnerSubject.getSelectedItem().toString())) {
                subject = course.getSubjects().get(i);
                break;
            }
        }
    }
}
