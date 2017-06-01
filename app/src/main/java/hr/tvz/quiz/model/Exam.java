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
        "name",
        "subject_id",
        "questions"
})
@Data
@NoArgsConstructor
public class Exam {

    @JsonProperty("id")
    public int id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("subject_id")
    public int subjectId;
    @JsonProperty("questions")
    public List<Question> questions = null;


    public Exam(int id, String name, int subjectId, List<Question> questions) {
        this.id = id;
        this.name = name;
        this.subjectId = subjectId;
        this.questions = questions;
    }
}