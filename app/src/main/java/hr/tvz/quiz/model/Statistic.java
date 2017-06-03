package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Statistic implements Serializable {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("points")
    @Expose
    public int points;
    @SerializedName("questions_user")
    @Expose
    public String questionsUser;
    @SerializedName("user_id")
    @Expose
    public int userId;
    @SerializedName("subject_id")
    @Expose
    public int subjectId;

    /**
     * No args constructor for use in serialization
     *
     */
    public Statistic() {
    }

    /**
     *
     * @param id
     * @param subjectId
     * @param userId
     * @param points
     * @param questionsUser
     */
    public Statistic(int id, int points, String questionsUser, int userId, int subjectId) {
        super();
        this.id = id;
        this.points = points;
        this.questionsUser = questionsUser;
        this.userId = userId;
        this.subjectId = subjectId;
    }

}