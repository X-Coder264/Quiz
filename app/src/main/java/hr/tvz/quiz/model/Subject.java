package hr.tvz.quiz.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "exam",
        "statistics",
        "games"
})
@Data
@NoArgsConstructor
public class Subject {

    @JsonProperty("id")
    public int id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("exam")
    public List<Exam> exam = null;
    @JsonProperty("statistics")
    public List<Statistic> statistics = null;
    @JsonProperty("games")
    public List<Game> games = null;

    public Subject(int id, String name, List<Exam> exam, List<Statistic> statistics, List<Game> games) {
        this.id = id;
        this.name = name;
        this.exam = exam;
        this.statistics = statistics;
        this.games = games;
    }
}