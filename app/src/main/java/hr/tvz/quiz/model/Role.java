
package hr.tvz.quiz.model;

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

    /**
     * No args constructor for use in serialization
     *
     */
    public Role() {
    }

    /**
     *
     * @param id
     * @param name
     */
    public Role(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

}