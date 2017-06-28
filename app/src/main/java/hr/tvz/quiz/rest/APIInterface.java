package hr.tvz.quiz.rest;

import android.database.Observable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import hr.tvz.quiz.model.Course;
import hr.tvz.quiz.model.Game;
import hr.tvz.quiz.model.Question;
import hr.tvz.quiz.model.Report;
import hr.tvz.quiz.model.Statistic;
import hr.tvz.quiz.model.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIInterface {

    //User
    @GET("user/{id}")
    Call<User> getUser(@Path("id") int id);

    @POST("/user/login")
    Call<User> login(@Body User user);

    @POST("/user/logout")
    Call<User> logout(@Body User user);

    @POST("user")
    Call<User> createUser(@Body User user);

    @Multipart
    @POST("user/{id}/uploadImage")
    Call<User> updateUserPhoto(
            @Path("id") int id,
            @Part MultipartBody.Part image
    );

    //Questions
    @GET("question/random/{examId}")
    Call <ArrayList<Question>> get10RandomQuestionsForExam(@Path("examId") int examId);

    @GET("question/all/{examId}")
    Call <ArrayList<Question>> getAllQuestionsForExam(@Path("examId") int examId);

    @GET("question/reports")
    Call <ArrayList<Question>> getQuestionWhichHaveReports();

    @GET("question/toVerify")
    Call <ArrayList<Question>> getQuestionWhichNeedToBeVerified();

    @POST("question")
    Call <Question> createQuestion(@Body Question question);

    @PUT("question/{id}")
    Call <Question> updateQuestion(@Path("id") int id, @Body Question question);

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

    //Report
    @POST("report")
    Call<Report> postReport(@Body Report report);

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