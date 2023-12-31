package com.example.group1hrmsapp.model;

import javax.persistence.*;
import java.io.Serializable;

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
}
