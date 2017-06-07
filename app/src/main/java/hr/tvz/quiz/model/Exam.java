package hr.tvz.quiz.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Exam implements Serializable
{

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("subject_id")
    @Expose
    public Integer subjectId;
    private final static long serialVersionUID = 2214809548773954574L;

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
     */
    public Exam(Integer id, String name, Integer subjectId) {
        super();
        this.id = id;
        this.name = name;
        this.subjectId = subjectId;
    }

}