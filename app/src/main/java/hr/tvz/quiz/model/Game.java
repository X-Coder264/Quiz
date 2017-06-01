package hr.tvz.quiz.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "score",
        "user1_points",
        "user2_points",
        "subject_id",
        "user1_id",
        "user2_id"
})
@Data
@NoArgsConstructor
public class Game {

    @JsonProperty("id")
    public int id;
    @JsonProperty("score")
    public String score;
    @JsonProperty("user1_points")
    public int user1Points;
    @JsonProperty("user2_points")
    public int user2Points;
    @JsonProperty("subject_id")
    public int subjectId;
    @JsonProperty("user1_id")
    public int user1Id;
    @JsonProperty("user2_id")
    public int user2Id;

    public Game(int id, String score, int user1Points, int user2Points, int subjectId, int user1Id, int user2Id) {
        this.id = id;
        this.score = score;
        this.user1Points = user1Points;
        this.user2Points = user2Points;
        this.subjectId = subjectId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }
}