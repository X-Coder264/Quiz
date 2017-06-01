package hr.tvz.quiz.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Role {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("users")
    @Expose
    public List<User> users = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Role() {
    }

    /**
     *
     * @param id
     * @param users
     * @param name
     */
    public Role(int id, String name, List<User> users) {
        super();
        this.id = id;
        this.name = name;
        this.users = users;
    }

}