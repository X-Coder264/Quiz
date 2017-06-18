package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class User implements Serializable
{

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("semester")
    @Expose
    public Integer semester;
    @SerializedName("role_id")
    @Expose
    public Integer roleId;
    @SerializedName("title_id")
    @Expose
    public Integer titleId;
    @SerializedName("course_id")
    @Expose
    public Integer courseId;
    @SerializedName("statistics")
    @Expose
    public List<Statistic> statistics = null;
    @SerializedName("role")
    @Expose
    public Role role;
    @SerializedName("title")
    @Expose
    public Title title;
    private final static long serialVersionUID = -860853089365295463L;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param id
     * @param title
     * @param email
     * @param name
     * @param role
     * @param image
     * @param courseId
     * @param titleId
     * @param password
     * @param statistics
     * @param roleId
     * @param semester
     */
    public User(Integer id, String name, String email, String password, String image, Integer semester, Integer roleId, Integer titleId, Integer courseId, List<Statistic> statistics, Role role, Title title) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.semester = semester;
        this.roleId = roleId;
        this.titleId = titleId;
        this.courseId = courseId;
        this.statistics = statistics;
        this.role = role;
        this.title = title;
    }

    /**
     *
     * @param email
     * @param password
     */
    public User(String email, String password) {
        super();
        this.email = email;
        this.password = password;
    }


    public User(Integer id, String name, String email, Integer role_id, Integer title_id, Integer course_id, Integer semester, String image) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.roleId = role_id;
        this.titleId = title_id;
        this.courseId = course_id;
        this.semester = semester;
        this.image = image;
    }

    public User(String name, String email, String password, Integer title, Integer courseId, Integer roleId, Integer semester) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.titleId = title;
        this.courseId = courseId;
        this.roleId = roleId;
        this.semester = semester;
    }
}