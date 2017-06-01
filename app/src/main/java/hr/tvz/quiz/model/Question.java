package hr.tvz.quiz.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "question",
        "exam_id",
        "reports",
        "answers"
})
@Data
@NoArgsConstructor
public class Question {

    @JsonProperty("id")
    public int id;
    @JsonProperty("question")
    public String question;
    @JsonProperty("exam_id")
    public int examId;
    @JsonProperty("reports")
    public List<Report> reports = null;
    @JsonProperty("answers")
    public List<Answer> answers = null;

    public Question(int id, String question, int examId, List<Report> reports, List<Answer> answers) {
        this.id = id;
        this.question = question;
        this.examId = examId;
        this.reports = reports;
        this.answers = answers;
    }
}