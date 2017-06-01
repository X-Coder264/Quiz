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
        "email",
        "password",
        "image",
        "role_id",
        "title_id",
        "course_id",
        "statistics",
        "game1",
        "game2",
})
@Data
@NoArgsConstructor
public class UserT {

    @JsonProperty("id")
    public int id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("email")
    public String email;
    @JsonProperty("password")
    public String password;
    @JsonProperty("image")
    public Object image;
    @JsonProperty("role_id")
    public String roleId;
    @JsonProperty("title_id")
    public String titleId;
    @JsonProperty("course_id")
    public String courseId;
    @JsonProperty("statistics")
    public List<Statistic> statistics = null;
    @JsonProperty("game1")
    public List<Game> game1 = null;
    @JsonProperty("game2")
    public List<Game> game2 = null;

    public UserT(int id, String name, String email, String password, Object image, String roleId, String titleId, String courseId, List<Statistic> statistics, List<Game> game1, List<Game> game2) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.roleId = roleId;
        this.titleId = titleId;
        this.courseId = courseId;
        this.statistics = statistics;
        this.game1 = game1;
        this.game2 = game2;
    }

}