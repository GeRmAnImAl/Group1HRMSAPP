package com.example.group1hrmsapp.service;

import com.example.group1hrmsapp.model.Employee;
import com.example.group1hrmsapp.model.WorkedHours;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

/**
 * The WorkedHoursService interface defines the standard operations
 * to be performed on a WorkedHours Object.
 */
public interface WorkedHoursService {
    /**
     * Returns all worked hours in the system.
     * @return a list of WorkedHours objects.
     */
    List<WorkedHours> getAllWorkedHours();

    /**
     * Creates new worked hours.
     * @param workedHours the WorkedHours object to be saved.
     */
    void createWorkedHours(WorkedHours workedHours);

    /**
     * Cancels worked hours.
     * @param workedHoursId the id of the WorkedHours to be cancelled.
     * @return the cancelled WorkedHours object.
     */
    WorkedHours cancelWorkedHours(Long workedHoursId);

    /**
     * Approves worked hours.
     * @param workedHoursId the id of the WorkedHours to be approved.
     * @param managerId the id of the manager who approves the hours.
     * @return the approved WorkedHours object.
     */
    WorkedHours approveWorkedHours(Long workedHoursId, Long managerId);

    /**
     * Rejects worked hours.
     * @param workedHoursId the id of the WorkedHours to be rejected.
     * @param managerId the id of the manager who rejects the hours.
     * @return the rejected WorkedHours object.
     */
    WorkedHours rejectWorkedHours(Long workedHoursId, Long managerId);

    /**
     * Returns a paginated list of WorkedHours objects.
     * @param pageNo the page number.
     * @param pageSize the size of the page.
     * @param sortField the field by which to sort the WorkedHours objects.
     * @param sortDirection the direction of the sort (asc or desc).
     * @return a Page of WorkedHours objects.
     */
    Page<WorkedHours> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);

    /**
     * Returns a paginated and filtered list of WorkedHours objects.
     * @param spec the specification defining the filtering criteria.
     * @param pageable the paging parameters.
     * @return a Page of WorkedHours objects that match the specification.
     */
    Page<WorkedHours> findFilteredAndPaginated(Specification<WorkedHours> spec, Pageable pageable);

    /**
     * Prepares a Specification object based on provided filter criteria.
     * @param employee the ID of the employee the hours are associated with.
     * @param startDate the start date for the worked hours.
     * @param endDate the end date for the worked hours.
     * @param approvalStatus the status of the worked hours.
     * @return a Specification object.
     */
    Specification<WorkedHours> prepareSpecification(Employee employee, LocalDate startDate, LocalDate endDate, String approvalStatus);
}
