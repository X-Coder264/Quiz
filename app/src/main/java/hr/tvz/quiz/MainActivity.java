package hr.tvz.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import hr.tvz.quiz.model.User;

public class MainActivity extends AppCompatActivity {

    // UI references.
    private TextView mNameTextView;

    private UserLocalStore userLocalStore;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLocalStore = new UserLocalStore(this);

        user = userLocalStore.getLoggedInUser();

        mNameTextView = (TextView) findViewById(R.id.NametextView);

        if(user != null) {
            mNameTextView.setText(user.getUsername());
        }
    }
}
