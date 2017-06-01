package hr.tvz.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import hr.tvz.quiz.model.UserT;
import hr.tvz.quiz.rest.APIClient;
import hr.tvz.quiz.rest.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // UI references.
    //private TextView mNameTextView;

    //private UserLocalStore userLocalStore;
    private APIClient client = APIClient.getInstance();
    private UserT user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Call<UserT> call = client.getApiService().getUser(1);
        call.enqueue(new Callback<UserT>() {
            @Override
            public void onResponse(Call<UserT> call, Response<UserT> response) {
                int statusCode = response.code();
                user = response.body();
                if (statusCode == 200) {
                    System.out.println(user.getName());
                }else{
                    System.out.println("Object not found");
                }
            }

            @Override
            public void onFailure(Call<UserT> call, Throwable t) {
                Log.w("Tomislav", t.getCause());
            }
        });




/*
        userLocalStore = new UserLocalStore(this);

        user = userLocalStore.getLoggedInUser();

        mNameTextView = (TextView) findViewById(R.id.NametextView);

        if(user != null) {
            mNameTextView.setText(user.getUsername());
        }*/
    }

}
