package com.example.group1hrmsapp.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entity class representing a Training Module in an HRMS application.
 * Each Training Module has an ID, a name, module info, and a quiz with questions and answers.
 */
@Entity
@Table(name = "training_modules")
public class TrainingModule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String moduleName;
    @Column(name = "module_info", columnDefinition = "TEXT")
    private String moduleInfo;
    @ElementCollection
    @MapKeyColumn(name="question")
    @Column(name="answer")
    @CollectionTable(name="quiz", joinColumns=@JoinColumn(name="module_id"))
    private Map<String, String> quiz = new HashMap<>();
    @Transient
    private List<String> questions = new ArrayList<>();
    @Transient
    private List<String> answers = new ArrayList<>();

    /**
     * Default constructor.
     */
    public TrainingModule() {}

    /**
     * Retrieves the ID of the Training Module.
     * @return A Long representing the ID of the Training Module.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the Training Module.
     * @param id A Long containing the ID of the Training Module.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the Training Module.
     * @return A String representing the name of the Training Module.
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * Sets the name of the Training Module.
     * @param moduleName A String containing the name of the Training Module.
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * Retrieves the information of the Training Module.
     * @return A String representing the information of the Training Module.
     */
    public String getModuleInfo() {
        return moduleInfo;
    }

    /**
     * Sets the information of the Training Module.
     * @param moduleInfo A String containing the information of the Training Module.
     */
    public void setModuleInfo(String moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    /**
     * Retrieves the quiz of the Training Module.
     * @return A Map containing the quiz of the Training Module.
     */
    public Map<String, String> getQuiz() {
        return quiz;
    }

    /**
     * Sets the quiz of the Training Module.
     * @param quiz A Map containing the quiz of the Training Module.
     */
    public void setQuiz(Map<String, String> quiz) {
        this.quiz = quiz;
    }

    /**
     * Adds a question and its corresponding answer to the quiz of the Training Module.
     * @param question A String containing the question to be added to the quiz.
     * @param answer A String containing the answer to the question.
     */
    public void addQuestionAnswer(String question, String answer) {
        this.quiz.put(question, answer);
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }
}
