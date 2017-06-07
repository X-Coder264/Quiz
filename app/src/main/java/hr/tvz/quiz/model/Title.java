package hr.tvz.quiz.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Title implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("poIntegers")
    @Expose
    public Integer poIntegers;

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
     * @param poIntegers
     */
    public Title(Integer id, String name, Integer poIntegers) {
        super();
        this.id = id;
        this.name = name;
        this.poIntegers = poIntegers;
    }

}