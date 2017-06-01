package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Report {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("complaint")
    @Expose
    public String complaint;
    @SerializedName("question_id")
    @Expose
    public int questionId;

    /**
     * No args constructor for use in serialization
     *
     */
    public Report() {
    }

    /**
     *
     * @param id
     * @param questionId
     * @param complaint
     */
    public Report(int id, String complaint, int questionId) {
        super();
        this.id = id;
        this.complaint = complaint;
        this.questionId = questionId;
    }

}