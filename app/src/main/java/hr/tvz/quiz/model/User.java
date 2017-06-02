package hr.tvz.quiz.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class User {

    @SerializedName("id")
    @Expose
    public int id;
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
    public int semester;
    @SerializedName("role_id")
    @Expose
    public int roleId;
    @SerializedName("title_id")
    @Expose
    public int titleId;
    @SerializedName("course_id")
    @Expose
    public int courseId;
    @SerializedName("statistics")
    @Expose
    public List<Statistic> statistics = null;
    @SerializedName("game1")
    @Expose
    public List<Game> game1 = null;
    @SerializedName("game2")
    @Expose
    public List<Game> game2 = null;
    @SerializedName("role")
    @Expose
    public Role role;
    @SerializedName("title")
    @Expose
    public Title title;

    /**
     * No args constructor for use in serialization
     *
     */
    public User() {
    }

    /**
     *
     * @param game2
     * @param image
     * @param courseId
     * @param password
     * @param statistics
     * @param id
     * @param title
     * @param email
     * @param game1
     * @param name
     * @param role
     * @param titleId
     * @param semester
     * @param roleId
     */
    public User(int id, String name, String email, String password, String image, int semester, int roleId, int titleId, int courseId, List<Statistic> statistics, List<Game> game1, List<Game> game2, Role role, Title title) {
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
        this.game1 = game1;
        this.game2 = game2;
        this.role = role;
        this.title = title;
    }

    /**
     *
     * @param email
     * @param password
     */
    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
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

    /**
     *
     * @param id
     * @param name
     * @param email
     * @param role
     */
    public User(int id, String name, String email, int role) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.roleId = role;
    }
}