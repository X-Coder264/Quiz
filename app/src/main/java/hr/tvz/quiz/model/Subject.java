package hr.tvz.quiz.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Subject implements Serializable
{

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("semester")
    @Expose
    public Integer semester;
    @SerializedName("question_counter")
    @Expose
    public Integer questionCounter;
    @SerializedName("exam")
    @Expose
    public List<Exam> exam = null;
    private final static long serialVersionUID = 2222180988070014151L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Subject() {
    }

    /**
     *
     * @param id
     * @param name
     * @param exam
     * @param semester
     * @param questionCounter
     */
    public Subject(Integer id, String name, Integer semester, List<Exam> exam, Integer questionCounter) {
        super();
        this.id = id;
        this.name = name;
        this.semester = semester;
        this.exam = exam;
        this.questionCounter = questionCounter;
    }

}