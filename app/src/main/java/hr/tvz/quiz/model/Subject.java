package hr.tvz.quiz.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Subject {

    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("exam")
    @Expose
    public List<Exam> exam = null;
    @SerializedName("statistics")
    @Expose
    public List<Statistic> statistics = null;
    @SerializedName("games")
    @Expose
    public List<Game> games = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Subject() {
    }

    /**
     *
     * @param id
     * @param games
     * @param name
     * @param exam
     * @param statistics
     */
    public Subject(int id, String name, List<Exam> exam, List<Statistic> statistics, List<Game> games) {
        super();
        this.id = id;
        this.name = name;
        this.exam = exam;
        this.statistics = statistics;
        this.games = games;
    }

}