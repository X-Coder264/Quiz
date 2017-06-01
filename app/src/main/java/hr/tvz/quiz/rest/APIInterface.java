package hr.tvz.quiz.rest;

import hr.tvz.quiz.model.UserT;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIInterface {

    @GET("user/{id}")
    Call<UserT> getUser(@Path("id") int id);

/*
    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);


    @POST("users/new")
    Call<User> createUser(@Body User user);


    @GET("/report")
    Call<MultipleResource> doGetListResources();

    @POST("/question")
    Call<User> createUser(@Body User user);

    @GET("/report/?")
    Call<UserList> doGetUserList(@Query("page") String page);

    @FormUrlEncoded
    @POST("/users?")
    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);*/
}