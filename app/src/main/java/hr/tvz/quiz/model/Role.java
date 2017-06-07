
package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Role implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
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
    public Role(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

}