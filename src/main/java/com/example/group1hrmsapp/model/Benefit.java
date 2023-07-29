package com.example.group1hrmsapp.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity class representing an employee benefit within the HRMS app.
 */
@Entity
@Table(name = "benefits")
public class Benefit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String benefitName;
    @Column(name = "description")
    private String benefitDesc;
    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_type")
    private CoverageType coverageType;
    @Column(name = "coverage_start_date")
    private String startDate;
    @Column(name = "coverage_end_date")
    private String endDate;
    @Column(name = "cost")
    private double benefitCost;
    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private CoverageProvider coverageProvider;

    /**
     * Returns the ID of the benefit.
     * @return the benefit ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the benefit.
     * @param id the benefit ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the benefit.
     * @return the benefit name
     */
    public String getBenefitName() {
        return benefitName;
    }

    /**
     * Sets the name of the benefit.
     * @param benefitName the benefit name to set
     */
    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }

    /**
     * Returns the description of the benefit.
     * @return the benefit description
     */
    public String getBenefitDesc() {
        return benefitDesc;
    }

    /**
     * Sets the description of the benefit.
     * @param benefitDesc the benefit description to set
     */
    public void setBenefitDesc(String benefitDesc) {
        this.benefitDesc = benefitDesc;
    }

    /**
     * Returns the coverage type of the benefit.
     * @return the benefit coverage type
     */
    public CoverageType getCoverageType() {
        return coverageType;
    }

    /**
     * Sets the coverage type of the benefit.
     * @param coverageType the benefit coverage type to set
     */
    public void setCoverageType(CoverageType coverageType) {
        this.coverageType = coverageType;
    }

    /**
     * Returns the start date of the benefit coverage.
     * @return the benefit coverage start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the benefit coverage.
     * @param startDate the benefit coverage start date to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of the benefit coverage.
     * @return the benefit coverage end date
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the benefit coverage.
     * @param endDate the benefit coverage end date to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the cost of the benefit.
     * @return the benefit cost
     */
    public double getBenefitCost() {
        return benefitCost;
    }

    /**
     * Sets the cost of the benefit.
     * @param benefitCost the benefit cost to set
     */
    public void setBenefitCost(double benefitCost) {
        this.benefitCost = benefitCost;
    }

    /**
     * Returns the provider of the benefit coverage.
     * @return the benefit coverage provider
     */
    public CoverageProvider getCoverageProvider() {
        return coverageProvider;
    }

    /**
     * Sets the provider of the benefit coverage.
     * @param coverageProvider the benefit coverage provider to set
     */
    public void setCoverageProvider(CoverageProvider coverageProvider) {
        this.coverageProvider = coverageProvider;
    }
}
