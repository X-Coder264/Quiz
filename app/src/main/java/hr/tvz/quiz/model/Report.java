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
        "complaint",
        "question_id"
})
@Data
@NoArgsConstructor
public class Report {

    @JsonProperty("id")
    public int id;
    @JsonProperty("complaint")
    public String complaint;
    @JsonProperty("question_id")
    public int questionId;

    public Report(int id, String complaint, int questionId) {
        this.id = id;
        this.complaint = complaint;
        this.questionId = questionId;
    }
}