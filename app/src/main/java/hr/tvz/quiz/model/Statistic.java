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
        "points",
        "user_id",
        "subject_id"
})
@Data
@NoArgsConstructor
public class Statistic {

    @JsonProperty("id")
    public int id;
    @JsonProperty("points")
    public int points;
    @JsonProperty("user_id")
    public int userId;
    @JsonProperty("subject_id")
    public int subjectId;

    public Statistic(int id, int points, int userId, int subjectId) {
        this.id = id;
        this.points = points;
        this.userId = userId;
        this.subjectId = subjectId;
    }

}