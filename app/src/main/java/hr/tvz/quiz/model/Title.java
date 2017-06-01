package hr.tvz.quiz.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Title {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("points")
    @Expose
    public int points;
    @SerializedName("users")
    @Expose
    public List<User> users = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Title() {
    }

    /**
     *
     * @param id
     * @param users
     * @param name
     * @param points
     */
    public Title(int id, String name, int points, List<User> users) {
        super();
        this.id = id;
        this.name = name;
        this.points = points;
        this.users = users;
    }

}