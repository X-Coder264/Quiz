package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Title implements Serializable {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("points")
    @Expose
    public int points;

    /**
     * No args constructor for use in serialization
     *
     */
    public Title() {
    }

    /**
     *
     * @param id
     * @param name
     * @param points
     */
    public Title(int id, String name, int points) {
        super();
        this.id = id;
        this.name = name;
        this.points = points;
    }

}