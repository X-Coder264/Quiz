package hr.tvz.quiz;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hr.tvz.quiz.model.Course;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // UI references.
    //private TextView mNameTextView;

    //private UserLocalStore userLocalStore;
    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView tNameMain;
    private TextView tTitleMain;
    private TextView tSemesterMain;
    private TextView tCourseMain;

    private Spinner spinnerSemester;
    private Spinner spinnerCourse;
    private Spinner spinnerSubject;


    private View mProgressView;
    private View mLoginFormView;

    private APIClient client = APIClient.getInstance();
    private User user;
    private List <Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        initializeUser();

/*
        userLocalStore = new UserLocalStore(this);

        user = userLocalStore.getLoggedInUser();

        mNameTextView = (TextView) findViewById(R.id.NametextView);

        if(user != null) {
            mNameTextView.setText(user.getUsername());
        }*/
    }

    private void initialize (){
        tNameMain = (TextView) findViewById(R.id.textView_name_main);
        tTitleMain = (TextView) findViewById(R.id.textView_title_main);
        tSemesterMain = (TextView) findViewById(R.id.textView_semester_main);
        tCourseMain = (TextView) findViewById(R.id.textView_course_main);
        spinnerCourse = (Spinner) findViewById(R.id.spinner_course_main);
        spinnerSemester = (Spinner) findViewById(R.id.spinner_semester_main);
        spinnerSubject = (Spinner) findViewById(R.id.spinner_subject_main);

    }

    private void initializeUser(){
        Call<User> call = client.getApiService().getUser(1);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int statusCode = response.code();
                user = response.body();
                if (statusCode == 200) {
                    tNameMain.setText(user.getName());
                    tTitleMain.setText(user.getTitle().getName());
                    tSemesterMain.setText(user.getSemester() + ". godina");

                    List<String> spinnerArrayCourse =  new ArrayList<String>();
                    spinnerArrayCourse.add("1");spinnerArrayCourse.add("2");spinnerArrayCourse.add("3");spinnerArrayCourse.add("4");spinnerArrayCourse.add("5");spinnerArrayCourse.add("6");
                    setSpinner(user.getSemester()-1, spinnerArrayCourse, spinnerSemester);

                    initializeCourses();
                }else{
                    System.out.println("Object not found");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.w("Tomislav", t.getCause());
            }
        });
    }

    private void initializeCourses(){
        Call<List <Course>> call = client.getApiService().getCourses();
        call.enqueue(new Callback<List <Course>>() {
            @Override
            public void onResponse(Call<List <Course>> call, Response<List <Course>> response) {
                int statusCode = response.code();
                courses = response.body();
                if (statusCode == 200) {
                    Course course = new Course();
                    List<String> spinnerArrayCourse =  new ArrayList<String>();
                    List<String> spinnerArraySubject =  new ArrayList<String>();

                    int defaultSpinnerIndex=0;

                    for (int i = 0; i < courses.size(); i++){
                        if(courses.get(i).getSemester() == user.getSemester()) {
                            spinnerArrayCourse.add(courses.get(i).getName());
                        }

                        if (courses.get(i).getId() == user.getCourseId()){
                            course = courses.get(i);
                            defaultSpinnerIndex = i;
                        }
                    }

                    List<Subject> subjects = course.getSubjects();
                    for (int i=0; i<subjects.size();i++){
                        spinnerArraySubject.add(subjects.get(i).getName());
                    }

                    tCourseMain.setText(course.getName());
                    setSpinner(0, spinnerArraySubject, spinnerSubject);
                    setSpinner(defaultSpinnerIndex, spinnerArrayCourse, spinnerCourse);

                }else{
                    System.out.println("Objects not found");
                }
            }

            @Override
            public void onFailure(Call<List <Course>> call, Throwable t) {
                Log.w("Tomislav", t.getCause());
            }
        });
    }

    public void setSpinner(int defaultSpinnerIndex, List<String> spinnerArray, Spinner spinner){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.simple_spinner_item, spinnerArray);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(defaultSpinnerIndex);
    }

}
