package hr.tvz.quiz.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class Game implements Serializable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user1_poIntegers")
    @Expose
    public Integer user1PoIntegers;
    @SerializedName("user2_poIntegers")
    @Expose
    public Integer user2PoIntegers;
    @SerializedName("subject_id")
    @Expose
    public Integer subjectId;
    @SerializedName("user1_id")
    @Expose
    public Integer user1Id;
    @SerializedName("user2_id")
    @Expose
    public Integer user2Id;

    /**
     * No args constructor for use in serialization
     *
     */
    public Game() {
    }

    /**
     *
     * @param id
     * @param user2PoIntegers
     * @param user1Id
     * @param user2Id
     * @param subjectId
     * @param user1PoIntegers
     */
    public Game(Integer id, Integer user1PoIntegers, Integer user2PoIntegers, Integer subjectId, Integer user1Id, Integer user2Id) {
        super();
        this.id = id;
        this.user1PoIntegers = user1PoIntegers;
        this.user2PoIntegers = user2PoIntegers;
        this.subjectId = subjectId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    public Game(Integer user1PoIntegers, Integer subjectId, Integer user1Id) {
        super();
        this.user1PoIntegers = user1PoIntegers;
        this.subjectId = subjectId;
        this.user1Id = user1Id;
    }

}