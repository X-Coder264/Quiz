package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Statistic {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("points")
    @Expose
    public int points;
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
     */
    public Statistic(int id, int points, int userId, int subjectId) {
        super();
        this.id = id;
        this.points = points;
        this.userId = userId;
        this.subjectId = subjectId;
    }

}