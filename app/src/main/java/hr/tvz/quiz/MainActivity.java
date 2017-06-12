package hr.tvz.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.tvz.quiz.model.Course;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import hr.tvz.quiz.util.Key;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView tNameMain;
    private TextView tTitleMain;
    private TextView tSemesterMain;
    private TextView tCourseMain;

    private Spinner spinnerSemester;
    private Spinner spinnerCourse;
    private Spinner spinnerSubject;

    private Button buttonPlay;

    private UserLocalStore userLocalStore;

    private Integer user_id;


    private View mProgressView;
    private View mLoginFormView;

    private APIClient client = APIClient.getInstance();
    private User user;
    private List<Course> courses;

    private Course course = new Course();
    private Subject subject = new Subject();

    //Map<Integer, Subject> mapSubjects = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLocalStore = new UserLocalStore(this);

        user = userLocalStore.getLoggedInUser();
        user_id = user.getId();
        initializeViewElements();
        initializeUser();

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (course != null && spinnerSubject.getSelectedItem() != null) {
                    Intent intent = new Intent(MainActivity.this, PlayModeActivity.class);
                    intent.putExtra("SUBJECT", subject);
                    intent.putExtra("USER", user);
                    System.out.println(subject.getName());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No subject, play is not posiible!", Toast.LENGTH_LONG).show();
                    //ToDo: mora imati odabrane sva 3 spinnera
                }
            }
        });
    }

    private void initializeViewElements() {
        tNameMain = (TextView) findViewById(R.id.textView_name_main);
        tTitleMain = (TextView) findViewById(R.id.textView_title_main);
        tSemesterMain = (TextView) findViewById(R.id.textView_semester_main);
        tCourseMain = (TextView) findViewById(R.id.textView_course_main);
        spinnerCourse = (Spinner) findViewById(R.id.spinner_course_main);
        spinnerSemester = (Spinner) findViewById(R.id.spinner_semester_main);
        spinnerSubject = (Spinner) findViewById(R.id.spinner_subject_main);
        buttonPlay = (Button) findViewById(R.id.button_play);

    }

    private void initializeUser() {
        Call<User> call = client.getApiService().getUser(user_id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int statusCode = response.code();
                user = response.body();
                if (statusCode == 200) {
                    tNameMain.setText(user.getName());
                    tTitleMain.setText(user.getTitle().getName());
                    tSemesterMain.setText(user.getSemester() + ". godina");

                    setSpinner(user.getSemester() - 1, Arrays.asList("1", "2", "3", "4", "5", "6"), spinnerSemester);

                    initializeCourses();
                } else {
                    System.out.println("Object not found");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w("Tomislav", t.getCause());
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
                    List<String> spinnerArrayCourse = new ArrayList<String>();
                    List<String> spinnerArraySubject = new ArrayList<String>();
                    int defaultSpinnerIndex = 0;

                    for (int i = 0; i < courses.size(); i++) {
                        spinnerArrayCourse.add(courses.get(i).getName());

                        if (courses.get(i).getId() == user.getCourseId()) {
                            course = courses.get(i);
                            defaultSpinnerIndex = i;
                        }
                    }

                    List<Subject> subjects = course.getSubjects();
                    for (int i = 0; i < subjects.size(); i++) {
                        if (subjects.get(i).getSemester() == user.getSemester())
                            spinnerArraySubject.add(subjects.get(i).getName());
                    }

                    tCourseMain.setText(course.getName());
                    setSpinner(0, spinnerArraySubject, spinnerSubject);
                    setSpinner(defaultSpinnerIndex, spinnerArrayCourse, spinnerCourse);

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.simple_spinner_item, spinnerArray);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(defaultSpinnerIndex);
    }

    public void spinnerLogic(){
        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                List<String> spinnerArraySubject = new ArrayList<String>();
                for (int i = 0; i < courses.size(); i++){
                    if (courses.get(i).getName() == spinnerCourse.getSelectedItem()){
                        course = courses.get(i);
                        break;
                    }
                }

                List<Subject> subjects = course.getSubjects();
                for (int i=0; i<subjects.size();i++){
                    //System.out.println(subjects.get(i).getSemester() + " " + spinnerSemester.getSelectedItem());
                    if (subjects.get(i).getSemester() == Integer.parseInt(spinnerSemester.getSelectedItem().toString()))
                        spinnerArraySubject.add(subjects.get(i).getName());
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

                List<Subject> subjects = course.getSubjects();
                for (int i=0; i<subjects.size();i++){
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
        for (int i=0; i<course.getSubjects().size();i++){
            if (course.getSubjects().get(i).getName().equals(spinnerSemester.getSelectedItem().toString())){
                subject = course.getSubjects().get(i);
                break;
            }
        }
    }
}

/*public void spinnerLogic(){
        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                List<String> spinnerArraySubject = new ArrayList<String>();

                for (int i = 0; i < courses.size(); i++){
                    if (courses.get(i).getName() == spinnerCourse.getSelectedItem()){
                        course = courses.get(i);
                        break;
                    }
                }

                for(Integer key: mapSubjects.keySet()) {
                    if (key.equals(new Key(Integer.parseInt(spinnerSemester.getSelectedItem().toString()), course.getId()).hashCode())){
                            spinnerArraySubject.add(mapSubjects.get(key).getName());
                    }
                }

                //setSpinner(0, spinnerArraySubject, spinnerSubject);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }*/
