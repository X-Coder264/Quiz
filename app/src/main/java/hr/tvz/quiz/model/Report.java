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
    @SerializedName("complaInteger")
    @Expose
    public String complaInteger;
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
     * @param complaInteger
     */
    public Report(Integer id, String complaInteger, Integer questionId) {
        super();
        this.id = id;
        this.complaInteger = complaInteger;
        this.questionId = questionId;
    }

}