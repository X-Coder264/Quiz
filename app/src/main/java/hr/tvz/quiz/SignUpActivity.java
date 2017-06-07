package hr.tvz.quiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hr.tvz.quiz.adapter.SpinnerCourseAdapter;
import hr.tvz.quiz.model.Course;
import hr.tvz.quiz.model.User;
import hr.tvz.quiz.rest.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // UI references.
    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmationView;
    private Spinner SemesterSpinner;
    private Spinner CourseSpinner;
    private Button mSignInButton;
    private View mProgressView;
    private View mLoginFormView;

    //private static final String BASE_URL = "http://vps411407.ovh.net/api/";
    private APIClient client = APIClient.getInstance();
    private User user;
    private UserLocalStore userLocalStore;
    private List <Course> courses;

    private int course_id;
    private int semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userLocalStore = new UserLocalStore(this);

        // Set up the sign up form.
        mNameView = (EditText) findViewById(R.id.editText_name_registration);
        mEmailView = (EditText) findViewById(R.id.editText_email_registration);

        mPasswordView = (EditText) findViewById(R.id.editText_password_registration);

        mPasswordConfirmationView = (EditText) findViewById(R.id.editText_password_repeat_registration);

        SemesterSpinner = (Spinner) findViewById(R.id.spinner_semester_registration);
        SemesterSpinner.setOnItemSelectedListener(this);
        CourseSpinner = (Spinner) findViewById(R.id.spinner_course_registration);
        CourseSpinner.setOnItemSelectedListener(this);

        initializeSpinners();

        mSignInButton = (Button) findViewById(R.id.button_sign_up);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        mLoginFormView = findViewById(R.id.scrollView_registration_form);
        mProgressView = findViewById(R.id.progressBar_registration_progress);
    }

    /**
     * Attempts to register the account.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual register attempt is made.
     */
    private void attemptSignUp() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String password_confirmation = mPasswordConfirmationView.getText().toString();
        // a new user gets the "User" role by default
        int role_id = 1;
        int title_id = 1;

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check if the entered passwords match.
        if (!password.equals(password_confirmation)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check if a name was given.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt to register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user register attempt.
            showProgress(true);

            user = new User(name, email, password, title_id, course_id, role_id, semester);
            Call<User> call = client.getApiService().createUser(user);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    showProgress(false);
                    if (response.code() >= 200 && response.code() <= 202) {
                        user = response.body();
                        logUserIn(user);
                        Toast.makeText(SignUpActivity.this, "You have successfully logged in.", Toast.LENGTH_LONG).show();
                        onSignUpSuccess();
                    } else {
                        onSignUpFailed();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "An error happened. Please try again later.", Toast.LENGTH_LONG).show();
                    System.out.println(t.toString());
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@tvz.hr") && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    public void onSignUpSuccess() {
        Toast.makeText(SignUpActivity.this, "You have successfully registered and logged in.", Toast.LENGTH_LONG).show();
        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void onSignUpFailed() {
        Toast.makeText(SignUpActivity.this, "An error happened. Please try again later.", Toast.LENGTH_LONG).show();
    }

    private void logUserIn(User user) {
        userLocalStore.storeUserData(user);
        userLocalStore.setUserLoggedIn(true);
    }

    private void initializeSpinners(){
        List<Integer> spinnerArraySemester =  new ArrayList<Integer>();
        for (int i = 1; i < 7; i++) {
            spinnerArraySemester.add(i);
        }

        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(SignUpActivity.this, R.layout.simple_spinner_item, spinnerArraySemester);
        SemesterSpinner.setAdapter(dataAdapter);
        SemesterSpinner.setSelection(0);

        Call<List <Course>> call = client.getApiService().getCourses();
        call.enqueue(new Callback<List <Course>>() {
            @Override
            public void onResponse(Call<List <Course>> call, Response<List <Course>> response) {
                int statusCode = response.code();
                courses = response.body();
                if (statusCode == 200) {
                    SpinnerCourseAdapter adapter = new SpinnerCourseAdapter(SignUpActivity.this,
                            android.R.layout.simple_spinner_item,
                            courses);
                    CourseSpinner.setAdapter(adapter);
                    // the first course is selected by default
                    CourseSpinner.setSelection(0);
                    course_id = courses.get(0).getId();

                } else {
                    System.out.println("Objects not found");
                }
            }

            @Override
            public void onFailure(Call<List <Course>> call, Throwable t) {
                Log.e("Course Error", t.toString());
            }
        });
    }

    // onItemSelected for spinner
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinner_semester_registration) {
            semester = (int) spinner.getSelectedItem();
        } else if(spinner.getId() == R.id.spinner_course_registration) {
            Course course = (Course) spinner.getSelectedItem();
            course_id = course.getId();
        }
    }

    // onNothingSelected for spinner
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * Shows the progress UI and hides the sign up form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}