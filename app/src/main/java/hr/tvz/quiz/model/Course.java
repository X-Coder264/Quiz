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
    @SerializedName("subjects")
    @Expose
    public List<Subject> subjects = null;
    @SerializedName("users")
    @Expose
    public List<User> users = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Course() {
    }

    /**
     *
     * @param id
     * @param users
     * @param subjects
     * @param name
     */
    public Course(int id, String name, List<Subject> subjects, List<User> users) {
        super();
        this.id = id;
        this.name = name;
        this.subjects = subjects;
        this.users = users;
    }

}