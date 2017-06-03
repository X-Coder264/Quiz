package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Answer implements Serializable {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("answer")
    @Expose
    public String answer;
    @SerializedName("correct")
    @Expose
    public boolean correct;
    @SerializedName("question_id")
    @Expose
    public int questionId;

    /**
     * No args constructor for use in serialization
     *
     */
    public Answer() {
    }

    /**
     *
     * @param id
     * @param questionId
     * @param correct
     * @param answer
     */
    public Answer(int id, String answer, boolean correct, int questionId) {
        super();
        this.id = id;
        this.answer = answer;
        this.correct = correct;
        this.questionId = questionId;
    }

}