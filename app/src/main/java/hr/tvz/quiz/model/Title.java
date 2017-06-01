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
        "points",
        "users"
})
@Data
@NoArgsConstructor
public class Title {

    @JsonProperty("id")
    public int id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("points")
    public int points;
    @JsonProperty("users")
    public List<User> users = null;

    public Title(int id, String name, int points, List<User> users) {
        this.id = id;
        this.name = name;
        this.points = points;
        this.users = users;
    }
}