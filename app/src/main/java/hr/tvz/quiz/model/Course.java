package hr.tvz.quiz.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Course implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("subjects")
    @Expose
    public List<Subject> subjects = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Course() {
    }

    /**
     *
     * @param id
     * @param subjects
     * @param name
     */
    public Course(Integer id, String name, List<Subject> subjects) {
        super();
        this.id = id;
        this.name = name;
        this.subjects = subjects;
    }

}