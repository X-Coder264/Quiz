package hr.tvz.quiz.rest;

import android.support.annotation.Nullable;

import hr.tvz.quiz.model.Game;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.model.Statistic;
import hr.tvz.quiz.model.User;

import java.util.ArrayList;
import java.util.List;

import hr.tvz.quiz.model.Course;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface APIInterface {

    //User
    @GET("user/{id}")
    Call<User> getUser(@Path("id") int id);

    @POST("/user/login")
    Call<User> login(@Body User user);

    @POST("user")
    Call<User> createUser(@Body User user);

    //Questions
    @GET("question/random/{examId}")
    Call <ArrayList<Question>> get10RandomQuestionsForExam(@Path("examId") int examId);

    @GET("question/all/{examId}")
    Call <ArrayList<Question>> getAllQuestionsForExam(@Path("examId") int examId);

    //Game
    @POST("game")
    @Nullable
    Call<Game> postGame(@Body Game game);

    //Statistics
    @POST("statistic")
    @Nullable
    Call<Statistic> postStatistic(@Body Statistic statistic);

    //Course
    @GET("course")
    Call<List<Course>> getCourses();

    @GET("course")
    Observable<List<Course>> getCoursesRx();

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