package hr.tvz.quiz.rest;


import android.util.Log;

import java.io.IOException;

import hr.tvz.quiz.model.UserT;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    private UserT user;
    private APIClient client = APIClient.getInstance();



    public void setUser(UserT user) {
        this.user = user;
    }

    public UserT getUser(int id) {
        Call<UserT> call = client.getApiService().getUser(id);


        call.enqueue(new Callback<UserT>() {
            @Override
            public void onResponse(Call<UserT> call, Response<UserT> response) {
                int statusCode = response.code();
                user = response.body();

                System.out.println(user.getName());
            }

            @Override
            public void onFailure(Call<UserT> call, Throwable t) {
                Log.w("Tomislav", t.getCause());
            }
        });

        return user;
    }


}
