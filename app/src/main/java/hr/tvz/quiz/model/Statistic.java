package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Statistic implements Serializable{

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("points")
    @Expose
    public Integer points;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("questions_user")
    @Expose
    public String questionsUser;
    @SerializedName("subject_id")
    @Expose
    public Integer subjectId;
    @SerializedName("username")
    @Expose
    public String username;

    /**
     * No args constructor for use in serialization
     *
     */
    public Statistic() {
    }

    /**
     *
     * @param id
     * @param questionsUser
     * @param subjectId
     * @param userId
     * @param points
     */
    public Statistic(Integer id, Integer points, Integer userId, String questionsUser, Integer subjectId) {
        super();
        this.id = id;
        this.points = points;
        this.userId = userId;
        this.questionsUser = questionsUser;
        this.subjectId = subjectId;
    }

}