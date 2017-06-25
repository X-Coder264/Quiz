package hr.tvz.quiz;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.tvz.quiz.model.Answer;
import hr.tvz.quiz.model.Course;
import hr.tvz.quiz.model.Exam;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.model.Subject;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import hr.tvz.quiz.util.Drawer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddQuestionActivity extends AppCompatActivity {

    private UserLocalStore userLocalStore;
    private User user;
    private APIClient client = APIClient.getInstance();

    private List<Course> courses;
    private List<Subject> subjects;
    private List<Exam> exams;
    private Course course = new Course();
    private Subject subject = new Subject();
    List<String> spinnerArrayCourse = new ArrayList<String>();
    List<String> spinnerArraySubject = new ArrayList<String>();
    List<String> spinnerArrayExam = new ArrayList<String>();

    //Drawer
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private Drawer drawer;

    private Spinner spinnerSemester, spinnerCourse, spinnerSubject, spinnerExam;
    private EditText questionEditText, answerOneEditText, answerTwoEditText, answerThreeEditText, answerFourEditText;
    private CheckBox answerOneCheckBox, answerTwoCheckBox, answerThreeCheckBox, answerFourCheckbox;
    private Button addQuestionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        userLocalStore = new UserLocalStore(this);

        user = userLocalStore.getLoggedInUser();

        initializeDrawer();

        spinnerCourse = (Spinner) findViewById(R.id.spinner_course_add_question);
        spinnerSemester = (Spinner) findViewById(R.id.spinner_semester_add_question);
        spinnerSubject = (Spinner) findViewById(R.id.spinner_subject_add_question);
        spinnerExam = (Spinner) findViewById(R.id.spinner_exam_add_question);

        setSpinner(user.getSemester() - 1, Arrays.asList("1", "2", "3", "4", "5", "6"), spinnerSemester);

        initializeCourses();

        questionEditText = (EditText) findViewById(R.id.EditText_question);

        answerOneEditText = (EditText) findViewById(R.id.EditText_question_answer_1);
        answerTwoEditText = (EditText) findViewById(R.id.EditText_question_answer_2);
        answerThreeEditText = (EditText) findViewById(R.id.EditText_question_answer_3);
        answerFourEditText = (EditText) findViewById(R.id.EditText_question_answer_4);

        answerOneCheckBox = (CheckBox) findViewById(R.id.checkBox_question_answer_1);
        answerTwoCheckBox = (CheckBox) findViewById(R.id.checkBox_question_answer_2);
        answerThreeCheckBox = (CheckBox) findViewById(R.id.checkBox_question_answer_3);
        answerFourCheckbox = (CheckBox) findViewById(R.id.checkBox_question_answer_4);

        addQuestionButton = (Button) findViewById(R.id.button_add_question);

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestion();
            }
        });
    }

    //Drawer Starts
    private void initializeDrawer(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerList = (ListView)findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer = new Drawer(mDrawerList, mDrawerLayout, this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawer.getmDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawer.getmDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawer.getmDrawerToggle().onConfigurationChanged(newConfig);
    }

    //Drawer Ends

    private void addQuestion() {
        /*System.out.println(spinnerSemester.getSelectedItem().toString());
        System.out.println(spinnerCourse.getSelectedItem().toString());
        System.out.println(spinnerSubject.getSelectedItem().toString());*/
        Integer exam_id = 1;
        for (int i = 0; i < subject.getExam().size(); i++) {
            if (subject.getExam().get(i).getName().equals(spinnerExam.getSelectedItem().toString())) {
                exam_id = subject.getExam().get(i).getId();
                break;
            }
        }

        String question_string = questionEditText.getText().toString();

        String answer1_string = answerOneEditText.getText().toString();
        boolean correct1 = answerOneCheckBox.isChecked();
        Answer answer1 = new Answer(answer1_string, correct1);
        String answer2_string = answerTwoEditText.getText().toString();
        boolean correct2 = answerTwoCheckBox.isChecked();
        Answer answer2 = new Answer(answer2_string, correct2);
        String answer3_string = answerThreeEditText.getText().toString();
        boolean correct3 = answerThreeCheckBox.isChecked();
        Answer answer3 = new Answer(answer3_string, correct3);
        String answer4_string = answerFourEditText.getText().toString();
        boolean correct4 = answerFourCheckbox.isChecked();
        Answer answer4 = new Answer(answer4_string, correct4);

        boolean allEditTextsContainText = false;

        if (!question_string.equals("") && !answer1_string.equals("") && !answer2_string.equals("") && !answer3_string.equals("") && !answer4_string.equals("")) {
            allEditTextsContainText = true;
        }

        boolean isThereAtLeastOneCorrectAnswer = false;

        if (correct1 || correct2 || correct3 || correct4) {
            isThereAtLeastOneCorrectAnswer = true;
        }

        if (allEditTextsContainText && isThereAtLeastOneCorrectAnswer) {
            List<Answer> answers = new ArrayList<Answer>();
            answers.add(answer1);
            answers.add(answer2);
            answers.add(answer3);
            answers.add(answer4);

            Question question = new Question(question_string, exam_id, answers);

            Call<Question> call = client.getApiService().createQuestion(question);
            call.enqueue(new Callback<Question>() {
                @Override
                public void onResponse(Call<Question> call, Response<Question> response) {
                    if (response.code() == 201) {
                        Toast.makeText(AddQuestionActivity.this, "The question was successfully added.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddQuestionActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    } else {
                        Toast.makeText(AddQuestionActivity.this, "An error happened. Please try again later.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Question> call, Throwable t) {
                    Toast.makeText(AddQuestionActivity.this, "An error happened. Please try again later.", Toast.LENGTH_LONG).show();
                    System.out.println(t.toString());
                }
            });
        } else {
            Toast.makeText(AddQuestionActivity.this, "All fields are required and at least one answer has to be correct.", Toast.LENGTH_LONG).show();
        }

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
                            for (int j = 0; j < subjects.get(i).getExam().size(); j++) {
                                spinnerArrayExam.add(subjects.get(i).getExam().get(j).getName());
                            }
                        }
                    }

                    setSpinner(0, spinnerArraySubject, spinnerSubject);
                    setSpinner(0, spinnerArrayCourse, spinnerCourse);
                    setSpinner(0, spinnerArrayExam, spinnerExam);

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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AddQuestionActivity.this, R.layout.simple_spinner_item, spinnerArray);
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
                        for (int j = 0; j < subjects.get(i).getExam().size(); j++) {
                            spinnerArrayExam.add(subjects.get(i).getExam().get(j).getName());
                        }
                    }
                }
                subject = subjects.get(0);
                setSubjectOnSpinnerChange();
                setSpinner(0, spinnerArraySubject, spinnerSubject);
                setSpinner(0, spinnerArrayExam, spinnerExam);
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
        spinnerArrayExam.clear();
        for (int j = 0; j < subject.getExam().size(); j++) {
            spinnerArrayExam.add(subject.getExam().get(j).getName());
        }
        setSpinner(0, spinnerArrayExam, spinnerExam);
    }
}
