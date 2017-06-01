package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Game {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("score")
    @Expose
    public String score;
    @SerializedName("user1_points")
    @Expose
    public int user1Points;
    @SerializedName("user2_points")
    @Expose
    public int user2Points;
    @SerializedName("subject_id")
    @Expose
    public int subjectId;
    @SerializedName("user1_id")
    @Expose
    public int user1Id;
    @SerializedName("user2_id")
    @Expose
    public int user2Id;

    /**
     * No args constructor for use in serialization
     *
     */
    public Game() {
    }

    /**
     *
     * @param id
     * @param user2Points
     * @param user1Id
     * @param user2Id
     * @param subjectId
     * @param score
     * @param user1Points
     */
    public Game(int id, String score, int user1Points, int user2Points, int subjectId, int user1Id, int user2Id) {
        super();
        this.id = id;
        this.score = score;
        this.user1Points = user1Points;
        this.user2Points = user2Points;
        this.subjectId = subjectId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

}