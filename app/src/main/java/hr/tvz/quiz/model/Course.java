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
        "subjects",
        "users"
})
@Data
@NoArgsConstructor
public class Course {

    @JsonProperty("id")
    public int id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("subjects")
    public List<Subject> subjects = null;
    @JsonProperty("users")
    public List<User> users = null;

    public Course(int id, String name, List<Subject> subjects, List<User> users) {
        this.id = id;
        this.name = name;
        this.subjects = subjects;
        this.users = users;
    }
}