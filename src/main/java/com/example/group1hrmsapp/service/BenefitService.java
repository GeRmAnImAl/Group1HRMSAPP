package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Service interface for managing Benefit entities.
 * Declares methods for standard CRUD operations and additional operations such as pagination and sorting.
 */
public interface BenefitService {
    /**
     * Retrieves all Benefit Objects.
     * @return a list of Benefit Objects.
     */
    List<Benefit> getAllBenefits();

    /**
     * Creates and stores a new Benefit entry into the database.
     * Before adding the benefit, the method checks if the logged-in user
     * has the necessary privileges to create a benefit by checking their special type.
     * Only users with the special type "HR" are allowed to create new benefits.
     *
     * @param benefit The benefit object to be saved into the database.
     * @throws RuntimeException If no user is currently logged in or if the logged-in user
     *                          doesn't have the HR special type.
     */
    void createBenefit(Benefit benefit);

    /**
     * Retrieves a Benefit by a specific ID number.
     * @param id Long representing the Benefit in the database.
     * @return an Benefit Object.
     */
    Benefit getBenefitById(Long id);

    /**
     * Saves a Benefit to the database.
     * @param benefit Benefit Object to be saved to the database.
     */
    void saveBenefit(Benefit benefit);

    /**
     * Updates an existing Benefit in the database.
     * @param benefit Benefit Object to be updated in the database.
     */
    void updateBenefit(Benefit benefit);

    /**
     * Deletes a Benefit from the database.
     * @param id Long representing the Benefit in the database.
     */
    void deleteBenefitById(Long id);

    /**
     * Enrolls an employee to a specific benefit using the benefit's ID.
     * @param benefitId The unique identifier of the benefit to enroll in.
     * @return true if the enrollment was successful, false otherwise.
     */
    boolean enrollInBenefit(Long benefitId);

    /**
     * Withdraws an employee from a specific benefit using the benefit's ID.
     * @param benefitId The unique identifier of the benefit to withdraw from.
     * @return true if the withdrawal was successful, false otherwise.
     */
    boolean withdrawFromBenefit(Long benefitId);

    /**
     * Retrieves a paginated list of Benefits sorted by a specific field and direction.
     * @param pageNo The page number to retrieve, starting from 1.
     * @param pageSize The number of Benefits per page.
     * @param sortField The field by which to sort the Benefits.
     * @param sortDirection The direction to sort the Benefits. This should be 'ASC' for ascending order or 'DESC' for descending order.
     * @return A Page of Benefits based on the given page number and size, sorted by the given field and direction.
     */
    Page<Benefit> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    /**
     * Retrieves a paginated list of benefits that meet the given specification criteria.
     * @param spec      The specification criteria used to filter benefits.
     * @param pageable  The pagination information including page size and sort order.
     * @return A paginated list of benefits that meet the given specification criteria.
     */
    Page<Benefit> findFilteredAndPaginated(Specification<Benefit> spec, Pageable pageable);

    /**
     * Prepares a specification for filtering Benefit based on the provided parameters.
     * @param coverageType     The type of coverage (e.g. HEALTH, DENTAL, VISION, etc.).
     * @param benefitName      The name of the benefit.
     * @param cost             The cost associated with the benefit.
     * @param coverageProvider The provider offering the coverage (e.g. AETNA, HUMANA, etc.).
     * @return A specification that can be used to filter benefits based on the given criteria.
     */
    Specification<Benefit> prepareSpecification(String coverageType, String benefitName, Double cost, String coverageProvider);

    /**
     * Retrieves the currently logged-in Employee.
     * @return An Employee object representing the currently logged-in user.
     */
    Employee getLoggedInUser();
}
