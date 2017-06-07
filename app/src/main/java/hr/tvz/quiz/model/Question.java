package hr.tvz.quiz.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Question implements Serializable {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("question")
    @Expose
    public String question;
    @SerializedName("exam_id")
    @Expose
    public int examId;
    @SerializedName("reports")
    @Expose
    public List<Report> reports = null;
    @SerializedName("answers")
    @Expose
    public List<Answer> answers = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Question() {
    }

    /**
     *
     * @param id
     * @param reports
     * @param answers
     * @param examId
     * @param question
     */
    public Question(int id, String question, int examId, List<Report> reports, List<Answer> answers) {
        super();
        this.id = id;
        this.question = question;
        this.examId = examId;
        this.reports = reports;
        this.answers = answers;
    }

}