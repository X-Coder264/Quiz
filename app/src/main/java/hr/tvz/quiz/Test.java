package hr.tvz.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hr.tvz.quiz.model.UserT;
import hr.tvz.quiz.rest.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class Test extends AppCompatActivity{

    public static final String BASE_URL = "http://10.0.3.2:8080/";
    private UserT user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_login);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        System.out.println("pocni");


        APIInterface apiService = retrofit.create(APIInterface.class);

        Call<UserT> call = apiService.getUser(1);
        call.enqueue(new Callback<UserT>() {
            @Override
            public void onResponse(Call<UserT> call, Response<UserT> response) {
                System.out.println("pocni2");
                int statusCode = response.code();
                user = response.body();
                System.out.println(user.getName());
            }

            @Override
            public void onFailure(Call<UserT> call, Throwable t) {
                System.out.println(t.getCause());
            }
        });

    }

}
