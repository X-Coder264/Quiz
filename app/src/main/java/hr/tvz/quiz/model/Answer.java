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
        "answer",
        "correct",
        "question_id"
})
@Data
@NoArgsConstructor
public class Answer {

    @JsonProperty("id")
    public int id;
    @JsonProperty("answer")
    public String answer;
    @JsonProperty("correct")
    public boolean correct;
    @JsonProperty("question_id")
    public int questionId;

    public Answer(int id, String answer, boolean correct, int questionId) {
        this.id = id;
        this.answer = answer;
        this.correct = correct;
        this.questionId = questionId;
    }
}