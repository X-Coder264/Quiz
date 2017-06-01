package hr.tvz.quiz.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Exam {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("subject_id")
    @Expose
    public int subjectId;
    @SerializedName("questions")
    @Expose
    public List<Question> questions = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Exam() {
    }

    /**
     *
     * @param id
     * @param subjectId
     * @param name
     * @param questions
     */
    public Exam(int id, String name, int subjectId, List<Question> questions) {
        super();
        this.id = id;
        this.name = name;
        this.subjectId = subjectId;
        this.questions = questions;
    }

}