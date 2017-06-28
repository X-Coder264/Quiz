package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Question implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("question")
    @Expose
    public String question;
    @SerializedName("exam_id")
    @Expose
    public Integer examId;
    @SerializedName("verified")
    @Expose
    public boolean verified;
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
    public Question(Integer id, String question, Integer examId, List<Report> reports, List<Answer> answers) {
        super();
        this.id = id;
        this.question = question;
        this.examId = examId;
        this.reports = reports;
        this.answers = answers;
    }

    /**
     *
     * @param id
     * @param reports
     * @param answers
     * @param examId
     * @param question
     */
    public Question(Integer id, String question, Integer examId, List<Report> reports, List<Answer> answers, boolean verified) {
        super();
        this.id = id;
        this.question = question;
        this.examId = examId;
        this.reports = reports;
        this.answers = answers;
        this.verified = verified;
    }

    /**
     *
     * @param answers
     * @param examId
     * @param question
     */
    public Question(String question, Integer examId, List<Answer> answers) {
        super();
        this.question = question;
        this.examId = examId;
        this.answers = answers;
    }

    /**
     *
     * @param answers
     * @param question
     * @param verified
     */
    public Question(String question, List<Answer> answers, boolean verified) {
        super();
        this.question = question;
        this.answers = answers;
        this.verified = verified;
    }

    /**
     *
     * @param answers
     * @param examId
     * @param question
     * @param verified
     */
    public Question(String question, Integer examId, List<Answer> answers, boolean verified) {
        super();
        this.question = question;
        this.examId = examId;
        this.answers = answers;
        this.verified = verified;
    }

}