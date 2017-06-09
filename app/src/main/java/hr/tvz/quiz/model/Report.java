package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Report implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("complaint")
    @Expose
    public String complaint;
    @SerializedName("question_id")
    @Expose
    public Integer questionId;

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
    public Report(Integer id, String complaint, Integer questionId) {
        super();
        this.id = id;
        this.complaint = complaint;
        this.questionId = questionId;
    }

}