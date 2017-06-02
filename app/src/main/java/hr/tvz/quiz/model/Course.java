package hr.tvz.quiz.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Course {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("semester")
    @Expose
    public Object semester;
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
     * @param semester
     */
    public Course(int id, String name, Object semester, List<Subject> subjects) {
        super();
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.subjects = subjects;
    }

}