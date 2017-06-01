package hr.tvz.quiz.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class UserT {

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
    public Object image;
    @SerializedName("role_id")
    @Expose
    public String roleId;
    @SerializedName("title_id")
    @Expose
    public String titleId;
    @SerializedName("course_id")
    @Expose
    public String courseId;
    @SerializedName("statistics")
    @Expose
    public List<Statistic> statistics = null;
    @SerializedName("game1")
    @Expose
    public List<Game> game1 = null;
    @SerializedName("game2")
    @Expose
    public List<Game> game2 = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserT() {
    }

    /**
     *
     * @param id
     * @param game2
     * @param game1
     * @param email
     * @param name
     * @param image
     * @param courseId
     * @param titleId
     * @param password
     * @param statistics
     * @param roleId
     */
    public UserT(int id, String name, String email, String password, Object image, String roleId, String titleId, String courseId, List<Statistic> statistics, List<Game> game1, List<Game> game2) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.roleId = roleId;
        this.titleId = titleId;
        this.courseId = courseId;
        this.statistics = statistics;
        this.game1 = game1;
        this.game2 = game2;
    }

}