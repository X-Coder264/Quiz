package hr.tvz.quiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import hr.tvz.quiz.model.User;

public class SignUpActivity extends AppCompatActivity {

    // UI references.
    private EditText mNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private static final String BASE_URL = "http://vps411407.ovh.net/api/";
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userLocalStore = new UserLocalStore(this);

        // Set up the sign up form.
        mNameView = (EditText) findViewById(R.id.signup_name);
        mEmailView = (EditText) findViewById(R.id.signup_email);

        mPasswordView = (EditText) findViewById(R.id.signup_password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        mLoginFormView = findViewById(R.id.email_signup_form);
        mProgressView = findViewById(R.id.signup_progress);
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

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
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

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("name", name);
            params.put("email", email);
            params.put("password", password);

            JsonObjectRequest request = new JsonObjectRequest
                    (BASE_URL + "register", new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int user_id = response.getInt("id");
                                String name = response.getString("name");
                                String email = response.getString("email");
                                int role = response.getInt("role_id");
                                User user = new User(user_id, name, email, role);
                                logUserIn(user);
                            } catch (JSONException e) {
                                System.out.println(e.toString());
                                onSignUpFailed();
                            }
                            showProgress(false);
                            onSignUpSuccess();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showProgress(false);
                            error.printStackTrace();
                            System.out.println(error.getMessage());
                            onSignUpFailed();
                        }
                    });

            request.setShouldCache(false);
            //Set retry policy timeout to 10 seconds, otherwise the server
            //doesn't manage to respond in time
            request.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(this).addToRequestQueue(request);
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
