package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.Benefit;
import org.springframework.data.domain.Page;

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
     * Retrieves a paginated list of Benefits sorted by a specific field and direction.
     * @param pageNo The page number to retrieve, starting from 1.
     * @param pageSize The number of Benefits per page.
     * @param sortField The field by which to sort the Benefits.
     * @param sortDirection The direction to sort the Benefits. This should be 'ASC' for ascending order or 'DESC' for descending order.
     * @return A Page of Benefits based on the given page number and size, sorted by the given field and direction.
     */
    Page<Benefit> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
